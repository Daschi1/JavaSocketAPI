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
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    Client(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void connect() throws IOException {
        //check if socket is initialised
        if (socket == null) {
            //initialise socket
            this.socket = new Socket(this.hostname, this.port);
        }
        //start reading and writing
        this.inputStreamThread = new InputStreamThread(this);
        this.inputStreamThread.start();
        this.outputStreamThread = new OutputStreamThread(this);
        this.outputStreamThread.start();
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

    public void send(Packet packet) {
        this.outputStreamThread.send(packet);
    }
}
