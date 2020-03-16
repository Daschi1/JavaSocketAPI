package de.javasocketapi.core;

import java.util.UUID;

class UpdateUUIDPacket extends Packet {
    public UpdateUUIDPacket(final UUID connectionUUID) {
        super(connectionUUID);
    }

    @Override
    public void send(final WritingByteBuffer writingByteBuffer) {

    }

    @Override
    public void recieve(final ReadingByteBuffer readingByteBuffer) {

    }
}
