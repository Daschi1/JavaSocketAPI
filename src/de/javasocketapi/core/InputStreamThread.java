package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;

class InputStreamThread extends Thread {

    private Client client;
    private Socket socket;

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
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
                    //interrupt thread
                    interrupt();
                    break;
                }
                int b = inputStream.read();
                if (b == -1) {
                    //close socket
                    this.socket.close();
                    continue;
                }
                bytes = new byte[b];
                //receive bytes
                inputStream.read(bytes, 0, b);
                ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(bytes);
                //read packetId
                int packetId = readingByteBuffer.readInt();
                //check if packet is UpdateUUIDPacket
                if (packetId == -2) {
                    //read connectionUUID
                    UUID connectionUUID = readingByteBuffer.readUUID();
                    //set updated connectionUUID
                    this.client.getConnectionUUID().set(connectionUUID);
                } else {
                    //get packet
                    Class<? extends Packet> packet = PacketRegistry.get(packetId);
                    //read connectionUUID
                    UUID connectionUUID = readingByteBuffer.readUUID();
                    //initialise packet
                    packet.getConstructor(UUID.class).newInstance(connectionUUID).recieve(readingByteBuffer);
                }
            }
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
