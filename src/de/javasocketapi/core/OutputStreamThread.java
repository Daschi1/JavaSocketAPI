package de.javasocketapi.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class OutputStreamThread extends Thread {

    private Socket socket;
    private List<Packet> packets;

    {
        this.packets = new LinkedList<>();
    }

    public OutputStreamThread(Socket socket) {
        this.socket = socket;
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
                    interrupt();
                    break;
                }
                if (this.packets.isEmpty()) {
                    continue;
                }
                Packet packet = this.packets.get(0);
                if (packet != null) {
                    this.packets.remove(0);
                    DynamicByteBuffer dynamicByteBuffer = new DynamicByteBuffer();
                    int packetId = PacketRegistry.indexOf(packet.getClass());
                    dynamicByteBuffer.putInt(packetId);
                    packet.send(dynamicByteBuffer);
                    byte[] bytes = dynamicByteBuffer.array();
                    outputStream.write(bytes.length);
                    outputStream.write(bytes);
                    outputStream.flush();
                }
                Thread.sleep(0, 1);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(Packet packet) {
        this.packets.add(packet);
    }
}
