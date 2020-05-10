package de.javasocketapi.core;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {

    private String hostname;
    private int port;
    private Socket socket;
    private InputStreamThread inputStreamThread;
    private OutputStreamThread outputStreamThread;

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Client(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    Client(final Socket socket) {
        this.socket = socket;
    }


    @Override
    public void connect() throws IOException {
        //check if socket is initialised
        if (this.socket == null) {
            //initialise socket
            this.socket = new Socket(this.hostname, this.port);
            this.socket.setTcpNoDelay(true);
            this.socket.setKeepAlive(true);
            this.socket.setPerformancePreferences(0, 1, 2);
        }
        //start reading and writing
        this.inputStreamThread = new InputStreamThread(this);
        this.inputStreamThread.run();
        this.outputStreamThread = new OutputStreamThread(this);
        this.outputStreamThread.run();
    }

    @Override
    public void disconnect() throws IOException {
        //interrupt reading and writing
        this.inputStreamThread.interrupt();
        this.outputStreamThread.interrupt();

        //check if socket is closed
        if (!this.socket.isClosed()) {
            //closed socket
            this.socket.close();
        }
    }

    public void send(final Packet packet) {
        this.outputStreamThread.send(packet);
    }
}
