package de.javasocketapi.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

class OutputStreamThread {

    private final Client client;
    private final Socket socket;
    private final LinkedList<Packet> packets = new LinkedList<>();
    private final Timer timer = new Timer();

    public OutputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() throws IOException {
        //initialise outputStream
        final OutputStream finalOutputStream = this.socket.getOutputStream();
        //start sending send byte arrays
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (OutputStreamThread.this.socket.isClosed()) {
                        //interrupt thread
                        OutputStreamThread.this.interrupt();
                        return;
                    }
                    //skip when no packets available to send
                    if (!OutputStreamThread.this.packets.isEmpty()) {
                        //get next packet available to send
                        final Packet packet = OutputStreamThread.this.packets.get(0);
                        //check if packet is valid
                        if (packet != null) {
                            //remove packet
                            OutputStreamThread.this.packets.remove(0);
                            final WritingByteBuffer writingByteBuffer = new WritingByteBuffer();
                            //check if packet is UpdateUUIDPacket
                            if (packet.getClass().equals(UpdateUUIDPacket.class)) {
                                writingByteBuffer.writeInt(-2);
                                writingByteBuffer.writeUUID(packet.getConnectionUUID());
                            } else {
                                //get packetId
                                final int packetId = PacketRegistry.indexOf(packet.getClass());
                                //write packetId
                                writingByteBuffer.writeInt(packetId);
                                //write connectionUuid
                                writingByteBuffer.writeUUID(OutputStreamThread.this.client.getConnectionUUID().get());
                                //initialise packet
                                packet.send(writingByteBuffer);
                            }
                            try {
                                //receive bytes
                                final byte[] bytes = writingByteBuffer.toBytes();
                                //check if outputstream is null
                                assert finalOutputStream != null;
                                //write bytes length
                                finalOutputStream.write(bytes.length);
                                //write bytes
                                finalOutputStream.write(bytes);
                                //flush outputStream
                                finalOutputStream.flush();
                            } catch (final SocketException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                } catch (final IOException | NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
        }, 0, 1);
    }

    public void interrupt() {
        this.timer.cancel();
    }

    public void send(final Packet packet) {
        this.packets.add(packet);
    }
}
