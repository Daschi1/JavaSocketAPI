package de.javasocketapi.core;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ReadingByteBuffer {
    private final ByteBuffer byteBuffer;

    ReadingByteBuffer(final byte... bytes) {
        this.byteBuffer = ByteBuffer.wrap(bytes);
    }

    public boolean readBoolean() {
        //read boolean
        return this.readByte() == 1;
    }

    public byte readByte() {
        //read byte
        return this.byteBuffer.get();
    }

    public short readShort() {
        //read short
        return this.byteBuffer.getShort();
    }

    public int readInt() {
        //read int
        return this.byteBuffer.getInt();
    }

    public long readLong() {
        //read long
        return this.byteBuffer.getLong();
    }

    public float readFloat() {
        //read float
        return this.byteBuffer.getFloat();
    }

    public double readDouble() {
        //read double
        return this.byteBuffer.getDouble();
    }

    public char readChar() {
        //read chars
        return (char) this.readByte();
    }

    public String readString() {
        //read string
        final int length = this.readInt();
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = this.readByte();
        }
        return new String(bytes);
    }

    public UUID readUUID() {
        //read uuid
        return UUID.fromString(this.readString());
    }
}
