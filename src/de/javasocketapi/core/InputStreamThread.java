package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

class InputStreamThread extends Thread {

    private Client client;
    private Socket socket;
    private Timer timer;

    {
        this.timer = new Timer();
    }

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    @Override
    public void run() {
        super.run();
        //initialise inputStream
        InputStream inputStream = null;
        try {
            inputStream = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[][] bytes = new byte[1][1];
        //start reading byte arrays
        InputStream finalInputStream = inputStream;
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (socket.isClosed()) {
                        //interrupt thread
                        interrupt();
                        return;
                    }
                    //check if finalInputStream is null
                    assert finalInputStream != null;
                    if (finalInputStream.available() > 0) {
                        int b = finalInputStream.read();
                        if (b != -1) {
                            bytes[0] = new byte[b];
                            //receive bytes
                            finalInputStream.read(bytes[0], 0, b);
                            ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(bytes[0]);
                            //read packetId
                            int packetId = readingByteBuffer.readInt();
                            //check if packet is UpdateUUIDPacket
                            if (packetId == -2) {
                                //read connectionUUID
                                UUID connectionUUID = readingByteBuffer.readUUID();
                                //set updated connectionUUID
                                client.getConnectionUUID().set(connectionUUID);
                            } else {
                                //get packet
                                Class<? extends Packet> packet = PacketRegistry.get(packetId);
                                //read connectionUUID
                                UUID connectionUUID = readingByteBuffer.readUUID();
                                //initialise packet
                                packet.getConstructor(UUID.class).newInstance(connectionUUID).recieve(readingByteBuffer);
                            }
                        } else {
                            //close socket
                            socket.close();
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    interrupt();
                }
            }
        }, 0, 1);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.timer.cancel();
    }
}
