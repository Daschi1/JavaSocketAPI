package de.javasocketapi.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

class ServerSocketAcceptingThread extends Thread {

    private final ServerSocket serverSocket;
    private final List<Client> clients;

    {
        this.clients = new LinkedList<>();
    }

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
                    break;
                }
                //initialise new client socket
                final Socket socket = this.serverSocket.accept();
                final Client client = new Client(socket);
                client.connect();
                this.clients.add(client);
                //update connectionUUID on clioent side
                client.send(new UpdateUUIDPacket(client.getConnectionUUID().get()));
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(final Packet packet, final UUID uuid) {
        //send to client
        for (final Client client : this.clients) {
            if (!client.getConnectionUUID().get().equals(uuid)) {
                continue;
            }
            client.send(packet);
        }
    }

    public void sendToAllClients(final Packet packet) {
        //send to all clients
        for (final Client client : this.clients) {
            client.send(packet);
        }
    }

    public void disconnectClient(final UUID uuid) throws IOException {
        //disconnect client
        for (final Client client : this.clients) {
            if (!client.getConnectionUUID().get().equals(uuid)) {
                continue;
            }
            client.disconnect();
        }
    }

    public void disconnectAllClients() throws IOException {
        //disconnect all clients
        for (final Client client : this.clients) {
            client.disconnect();
        }
        this.clients.clear();
    }

}
