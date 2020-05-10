package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

class InputStreamThread {

    private final Client client;
    private final Socket socket;
    private final Timer timer;

    {
        this.timer = new Timer();
    }

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() {
        //initialise inputStream
        InputStream inputStream = null;
        try {
            inputStream = this.socket.getInputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final byte[][] bytes = new byte[1][1];
        //start reading byte arrays
        final InputStream finalInputStream = inputStream;
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (InputStreamThread.this.socket.isClosed()) {
                        //interrupt thread
                        InputStreamThread.this.interrupt();
                        return;
                    }
                    //check if finalInputStream is null
                    assert finalInputStream != null;
                    if (finalInputStream.available() > 0) {
                        final int b = finalInputStream.read();
                        if (b != -1) {
                            bytes[0] = new byte[b];
                            //receive bytes
                            finalInputStream.read(bytes[0], 0, b);
                            final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(bytes[0]);
                            //read packetId
                            final int packetId = readingByteBuffer.readInt();
                            //check if packet is UpdateUUIDPacket
                            if (packetId == -2) {
                                //read connectionUUID
                                final UUID connectionUUID = readingByteBuffer.readUUID();
                                //set updated connectionUUID
                                InputStreamThread.this.client.getConnectionUUID().set(connectionUUID);
                            } else {
                                //get packet
                                final Class<? extends Packet> packet = PacketRegistry.get(packetId);
                                //read connectionUUID
                                final UUID connectionUUID = readingByteBuffer.readUUID();
                                //initialise packet
                                packet.getConstructor(UUID.class).newInstance(connectionUUID).recieve(readingByteBuffer);
                            }
                        } else {
                            //close socket
                            InputStreamThread.this.socket.close();
                        }
                    }
                } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (final IOException e) {
                    InputStreamThread.this.interrupt();
                }
            }
        }, 0, 1);
    }

    public void interrupt() {
        this.timer.cancel();
    }
}
