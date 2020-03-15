package de.javasocketapi.core;

import java.util.UUID;

class UpdateUUIDPacket extends Packet {
    public UpdateUUIDPacket(UUID connectionUUID) {
        super(connectionUUID);
    }

    @Override
    public void send(WritingByteBuffer writingByteBuffer) {

    }

    @Override
    public void recieve(ReadingByteBuffer readingByteBuffer) {

    }
}
