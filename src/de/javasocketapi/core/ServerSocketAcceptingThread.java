package de.javasocketapi.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class ServerSocketAcceptingThread extends Thread {

    private ServerSocket serverSocket;
    private List<Client> clients;

    {
        this.clients = new LinkedList<>();
    }

    public ServerSocketAcceptingThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (true) {
                if (this.serverSocket.isClosed()) {
                    interrupt();
                    break;
                }
                //initialise new client socket
                Socket socket = this.serverSocket.accept();
                Client client = new Client(socket);
                client.connect();
                this.clients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAllClients(byte... bytes) {
        //send to all clients
        for (Client client : this.clients) {
            client.send(bytes);
        }
    }

    public void disconnectAllClients() throws IOException {
        //disconnect all clients
        for (Client client : this.clients) {
            client.disconnect();
        }
        this.clients.clear();
    }

}
