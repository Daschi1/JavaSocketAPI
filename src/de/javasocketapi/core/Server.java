package de.javasocketapi.core;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Connection {

    private int port;
    private ServerSocket serverSocket;
    private ServerSocketAcceptingThread serverSocketAcceptingThread;

    public int getPort() {
        return port;
    }

    public Server(int port) {
        this.port = port;
    }


    @Override
    public void connect() throws IOException {
        //check if serverSocket is initialised
        if (serverSocket == null) {
            //initialise serverSocket
            this.serverSocket = new ServerSocket(this.port);
        }
        //start accepting clients
        this.serverSocketAcceptingThread = new ServerSocketAcceptingThread(this.serverSocket);
        this.serverSocketAcceptingThread.start();
    }

    @Override
    public void disconnect() throws IOException {
        //disconnect all clients
        this.disconnectAllClients();
        //interrupt accepting clients
        this.serverSocketAcceptingThread.interrupt();

        //check if serverSocket is closed
        if (!this.serverSocket.isClosed()) {
            //disconnect serverSocket
            this.serverSocket.close();
        }
    }

    public void sendToAllClients(byte... bytes) {
        //send to all clients
        this.serverSocketAcceptingThread.sendToAllClients(bytes);
    }

    public void disconnectAllClients() throws IOException {
        //disconnect all clients
        this.serverSocketAcceptingThread.disconnectAllClients();
    }
}
