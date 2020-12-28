package de.javasocketapi.core;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

abstract class Connection {

    private final AtomicReference<UUID> connectionUUID = new AtomicReference<>(UUID.randomUUID());

    public AtomicReference<UUID> getConnectionUUID() {
        return this.connectionUUID;
    }

    public abstract void connect() throws IOException;

    public abstract void disconnect() throws IOException;

}
