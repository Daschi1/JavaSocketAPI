package de.javasocketapi.core;

import java.util.UUID;

public abstract class Packet {

    private UUID connectionUUID;

    public UUID getConnectionUUID() {
        return connectionUUID;
    }

    public Packet(UUID connectionUUID) {
        this.connectionUUID = connectionUUID;
    }

    public abstract void send(WritingByteBuffer writingByteBuffer);

    public abstract void recieve(ReadingByteBuffer readingByteBuffer);

}
