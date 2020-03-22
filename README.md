# JavaSocketAPI

## An api for java with the native Java socket technology and the [boonproject](https://github.com/boonproject/boon).

### Implementation

Download the latest jar file from the releases tab and implement it in your project. Make sure to also implement the jar file when building your project.

### Example

First, we need to create the server and the client in for example the main method and call the connect method on both. We're also going to add a shutdown hook to disconnect both again. The packets need to be registered before the server or the client start. The packets must be always registered in the same order, on both the server and the client-side.

Server:

```java
package de.javasocketapitest.explanation;

import de.javasocketapi.core.PacketRegistry;
import de.javasocketapi.core.Server;

import java.io.IOException;

public class ServerMain {

    private static Server server;

    public static Server getServer() {
        return server;
    }

    public static void main(String[] args) throws IOException {
        PacketRegistry.registerPacket(RequestTimePacket.class);
        PacketRegistry.registerPacket(ReturnTimePacket.class);

        ServerMain.server = new Server(19503);
        ServerMain.server.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ServerMain.server.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
```

Client:

```java
package de.javasocketapitest.explanation;

import de.javasocketapi.core.Client;
import de.javasocketapi.core.PacketRegistry;

import java.io.IOException;

public class ClientMain {

    private static Client client;

    public static Client getClient() {
        return client;
    }

    public static void main(String[] args) throws IOException {
        PacketRegistry.registerPacket(RequestTimePacket.class);
        PacketRegistry.registerPacket(ReturnTimePacket.class);

        ClientMain.client = new Client("localhost", 19503);
        ClientMain.client.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ClientMain.client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        ClientMain.client.send(new RequestTimePacket(ClientMain.client.getConnectionUUID().get()));
    }
}
```

Let's continue by creating two packets, one for requesting the current time and one for actually returning it. We do this by creating the classes and extending it from Packet. After we did this, we need to implement the methods and the constructor. Note that the default constructor has to be there because it is used for system recognition. You can implement as many custom constructors as you like, but inside them, you need to always set the connectionUUID to null in the super call. In the send method, you just write every variable you want to send to the writingByteBuffer and in the recieve method you initialize them again reading them from the readingByteBuffer. You must read and write the variables in the same order because otherwise, the system couldn't handle it correctly.

RequestTimePacket:

```java
package de.javasocketapitest.explanation;

import de.javasocketapi.core.Packet;
import de.javasocketapi.core.ReadingByteBuffer;
import de.javasocketapi.core.WritingByteBuffer;

import java.util.UUID;

public class RequestTimePacket extends Packet {
    public RequestTimePacket(UUID connectionUUID) {
        super(connectionUUID);
    }

    @Override
    public void send(WritingByteBuffer writingByteBuffer) {

    }

    @Override
    public void recieve(ReadingByteBuffer readingByteBuffer) {
        ServerMain.getServer().sendToClient(new ReturnTimePacket(System.currentTimeMillis()), getConnectionUUID());
    }
}
```

ReturnTimePacket:

```java
package de.javasocketapitest.explanation;

import de.javasocketapi.core.Packet;
import de.javasocketapi.core.ReadingByteBuffer;
import de.javasocketapi.core.WritingByteBuffer;

import java.util.UUID;

public class ReturnTimePacket extends Packet {

    private long currentTimeMillis;

    public ReturnTimePacket(UUID connectionUUID) {
        super(connectionUUID);
    }

    public ReturnTimePacket(long currentTimeMillis) {
        super(null);
        this.currentTimeMillis = currentTimeMillis;
    }

    @Override
    public void send(WritingByteBuffer writingByteBuffer) {
        writingByteBuffer.writeLong(this.currentTimeMillis);
    }

    @Override
    public void recieve(ReadingByteBuffer readingByteBuffer) {
        this.currentTimeMillis = readingByteBuffer.readLong();

        System.out.println("current time millis: " + this.currentTimeMillis);
    }
}
```

When you want to send a packet to all clients, you can simply use the method server.sendToAllClients. Or if you want to disconnecta single client server.disconnectClient or all clients, you can simply call server.disconnectAllClients.
