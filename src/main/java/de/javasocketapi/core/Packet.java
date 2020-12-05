package de.javasocketapi.core;

import java.util.UUID;

public abstract class Packet {

    private final UUID connectionUUID;

    public UUID getConnectionUUID() {
        return this.connectionUUID;
    }

    public Packet(final UUID connectionUUID) {
        this.connectionUUID = connectionUUID;
    }

    public abstract void send(WritingByteBuffer writingByteBuffer);

    public abstract void recieve(ReadingByteBuffer readingByteBuffer);

}
