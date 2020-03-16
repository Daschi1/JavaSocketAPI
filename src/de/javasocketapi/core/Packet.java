package de.javasocketapi.core;

import java.util.UUID;

public abstract class Packet {

    private UUID connectionUUID;

    public UUID getConnectionUUID() {
        return connectionUUID;
    }

    public Packet(final UUID connectionUUID) {
        this.connectionUUID = connectionUUID;
    }

    public abstract void send(final WritingByteBuffer writingByteBuffer);

    public abstract void recieve(final ReadingByteBuffer readingByteBuffer);

}
