package de.javasocketapi.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class OutputStreamThread extends Thread {

    private Socket socket;
    private List<byte[]> bytes;

    {
        this.bytes = new LinkedList<>();
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
                if (this.bytes.isEmpty()) {
                    continue;
                }
                byte[] bytes = this.bytes.get(0);
                if (bytes != null) {
                    this.bytes.remove(0);
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

    public void send(byte... bytes) {
        this.bytes.add(bytes);
    }
}
