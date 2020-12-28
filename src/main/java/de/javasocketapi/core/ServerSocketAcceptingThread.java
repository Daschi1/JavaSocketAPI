package de.javasocketapi.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.UUID;

class ServerSocketAcceptingThread extends Thread {

    private final ServerSocket serverSocket;
    private final LinkedList<Client> clients = new LinkedList<>();

    public ServerSocketAcceptingThread(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                if (this.serverSocket.isClosed()) {
                    this.interrupt();
                    return;
                }
                //initialise new client socket
                final Socket socket = this.serverSocket.accept();
                final Client client = new Client(socket);
                client.connect();
                this.clients.add(client);
                //update connectionUUID on client side
                final UpdateUUIDPacket updateUUIDPacket = new UpdateUUIDPacket(client.getConnectionUUID().get());
                client.send(updateUUIDPacket);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendToClient(final Packet packet, final UUID uuid) {
        //send to client
        this.clients.stream().filter(client -> client.getConnectionUUID().get().equals(uuid)).forEach(client -> client.send(packet));
    }

    public void sendToAllClients(final Packet packet) {
        //send to all clients
        this.clients.forEach(client -> client.send(packet));
    }

    public void disconnectClient(final UUID uuid) {
        //disconnect client
        this.clients.stream().filter(client -> client.getConnectionUUID().get().equals(uuid)).forEach(client -> {
            try {
                client.disconnect();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void disconnectAllClients() {
        //disconnect all clients
        this.clients.forEach(client -> {
            try {
                if (client != null){
                    client.disconnect();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        this.clients.clear();
    }

}
