package de.javasocketapi.core;

public interface Packet {

    void recieve(DynamicByteBuffer dynamicByteBuffer);

    void send(DynamicByteBuffer dynamicByteBuffer);

}
