package de.javasocketapi.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class OutputStreamThread extends Thread {

    private Client client;
    private Socket socket;
    private List<Packet> packets;
    private Timer timer;

    {
        this.packets = new LinkedList<>();
        this.timer = new Timer();
    }

    public OutputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    @Override
    public void run() {
        super.run();
        //initialise outputStream
        OutputStream outputStream = null;
        try {
            outputStream = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream finalOutputStream = outputStream;
        //start sending send byte arrays
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (socket.isClosed()) {
                        //interrupt thread
                        interrupt();
                        return;
                    }
                    //skip when no packets available to send
                    if (!packets.isEmpty()) {
                        //get next packet available to send
                        Packet packet = packets.get(0);
                        //check if packet is valid
                        if (packet != null) {
                            //remove packet
                            packets.remove(0);
                            WritingByteBuffer writingByteBuffer = new WritingByteBuffer();
                            //check if packet is UpdateUUIDPacket
                            if (packet.getClass().equals(UpdateUUIDPacket.class)) {
                                writingByteBuffer.writeInt(-2);
                                writingByteBuffer.writeUUID(packet.getConnectionUUID());
                            } else {
                                //get packetId
                                int packetId = PacketRegistry.indexOf(packet.getClass());
                                //write packetId
                                writingByteBuffer.writeInt(packetId);
                                //write connectionUuid
                                writingByteBuffer.writeUUID(client.getConnectionUUID().get());
                                //initialise packet
                                packet.send(writingByteBuffer);
                            }
                            try {
                                //receive bytes
                                byte[] bytes = writingByteBuffer.toBytes();
                                //check if outputstream is null
                                assert finalOutputStream != null;
                                //write bytes length
                                finalOutputStream.write(bytes.length);
                                //write bytes
                                finalOutputStream.write(bytes);
                                //flush outputStream
                                finalOutputStream.flush();
                            } catch (SocketException ignored) {

                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {

                }
            }
        }, 0, 1);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.timer.cancel();
    }

    public void send(Packet packet) {
        this.packets.add(packet);
    }
}
