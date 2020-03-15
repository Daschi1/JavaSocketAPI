package de.javasocketapi.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class OutputStreamThread extends Thread {

    private Client client;
    private Socket socket;
    private List<Packet> packets;

    {
        this.packets = new LinkedList<>();
    }

    public OutputStreamThread(Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    @Override
    public void run() {
        super.run();
        try {
            //initialise outputStream
            OutputStream outputStream = this.socket.getOutputStream();
            //send byte arrays
            while (true) {
                if (this.socket.isClosed()) {
                    //interrupt thread
                    interrupt();
                    break;
                }
                if (this.packets.isEmpty()) {
                    //skip when no packets available to send
                    continue;
                }
                //get next packet available to send
                Packet packet = this.packets.get(0);
                //check if packet is valid
                if (packet != null) {
                    //remove packet
                    this.packets.remove(0);
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
                        writingByteBuffer.writeUUID(this.client.getConnectionUUID().get());
                        //initialise packet
                        packet.send(writingByteBuffer);
                    }
                    //receive bytes
                    byte[] bytes = writingByteBuffer.toBytes();
                    //write bytes length
                    outputStream.write(bytes.length);
                    //write bytes
                    outputStream.write(bytes);
                    //flush outputStream
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Packet packet) {
        this.packets.add(packet);
    }
}
