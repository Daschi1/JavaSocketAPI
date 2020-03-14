package de.javasocketapi.core;

import java.io.IOException;

interface Connection {
    void connect() throws IOException;

    void disconnect() throws IOException;
}
