package de.javasocketapi.core;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

abstract class Connection {

    private final AtomicReference<UUID> connectionUUID;

    public AtomicReference<UUID> getConnectionUUID() {
        return this.connectionUUID;
    }

    {
        this.connectionUUID = new AtomicReference<>(UUID.randomUUID());
    }

    public abstract void connect() throws IOException;

    public abstract void disconnect() throws IOException;

}
