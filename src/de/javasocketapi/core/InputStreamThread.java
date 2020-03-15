package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class InputStreamThread extends Thread {

    private Socket socket;

    public InputStreamThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            //initialise inputStream
            InputStream inputStream = this.socket.getInputStream();
            //read byte arrays
            byte[] bytes;
            while (true) {
                if (this.socket.isClosed()) {
                    interrupt();
                    break;
                }
                int b = inputStream.read();
                if (b == -1) {
                    this.socket.close();
                    continue;
                }
                bytes = new byte[b];
                inputStream.read(bytes, 0, b);
                DynamicByteBuffer dynamicByteBuffer = new DynamicByteBuffer(bytes);
                int packetId = dynamicByteBuffer.getInt();
                Class<? extends Packet> packet = PacketRegistry.get(packetId);
                packet.newInstance().recieve(dynamicByteBuffer);
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
