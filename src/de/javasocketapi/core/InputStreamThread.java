package de.javasocketapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

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
                System.out.println(Arrays.toString(bytes)); //TODO change to packet
                System.out.println(new String(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
