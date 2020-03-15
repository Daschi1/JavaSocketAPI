package de.javasocketapi.core;

import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.util.UUID;

abstract class Connection {

    private volatile SimpleObjectProperty<UUID> connectionUUID;

    public SimpleObjectProperty<UUID> getConnectionUUID() {
        return connectionUUID;
    }

    {
        this.connectionUUID = new SimpleObjectProperty<>(UUID.randomUUID());
    }

    public abstract void connect() throws IOException;

    public abstract void disconnect() throws IOException;

}
