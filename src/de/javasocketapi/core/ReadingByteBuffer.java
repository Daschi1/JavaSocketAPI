package de.javasocketapi.core;

import org.boon.primitive.ByteBuf;

import java.util.UUID;

public class ReadingByteBuffer {
    private final ByteBuf byteBuffer;

    ReadingByteBuffer(final byte... bytes) {
        this.byteBuffer = ByteBuf.create(bytes);
    }

    public boolean readBoolean() {
        //read boolean
        return this.readByte() == 1;
    }

    public byte readByte() {
        //read byte
        return this.byteBuffer.input().readByte();
    }

    public short readShort() {
        //read short
        return this.byteBuffer.input().readShort();
    }

    public int readInt() {
        //read int
        return this.byteBuffer.input().readInt();
    }

    public long readLong() {
        //read long
        return this.byteBuffer.input().readLong();
    }

    public float readFloat() {
        //read float
        return this.byteBuffer.input().readFloat();
    }

    public double readDouble() {
        //read double
        return this.byteBuffer.input().readDouble();
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
