package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

class InputStreamThread {

    private final Client client;
    private final Socket socket;
    private final Timer timer = new Timer();

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() throws IOException {
        //initialise inputStream
        final InputStream finalInputStream = this.socket.getInputStream();

        final AtomicReference<byte[]> bytes = new AtomicReference<>(null);
        //start reading byte arrays
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
                            bytes.set(new byte[b]);
                            //receive bytes
                            finalInputStream.read(bytes.get(), 0, b);
                            final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(bytes.get());
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
                } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                    exception.printStackTrace();
                } catch (final IOException ignored) {
                    InputStreamThread.this.interrupt();
                }
            }
        }, 0, 1);
    }

    public void interrupt() {
        this.timer.cancel();
    }
}
