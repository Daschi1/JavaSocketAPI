package de.javasocketapi.core;

import org.boon.primitive.ByteBuf;

import java.util.UUID;

public class WritingByteBuffer {
    private final ByteBuf byteBuf;

    WritingByteBuffer() {
        this.byteBuf = ByteBuf.create(0);
    }

    public void writeBoolean(final boolean value) {
        //writing boolean
        this.byteBuf.addByte(value ? 1 : 0);
    }

    public void writeByte(final byte value) {
        //writing byte
        this.byteBuf.add(value);
    }

    public void writeShort(final short value) {
        //writing short
        this.byteBuf.add(value);
    }

    public void writeInt(final int value) {
        //writing int
        this.byteBuf.add(value);
    }

    public void writeLong(final long value) {
        //writing long
        this.byteBuf.add(value);
    }

    public void writeFloat(final float value) {
        //writing float
        this.byteBuf.add(value);
    }

    public void writeDouble(final double value) {
        //writing double
        this.byteBuf.add(value);
    }

    public void writeChar(final char value) {
        //writing char
        this.writeByte((byte) value);
    }

    public void writeString(final String value) {
        //check value
        WritingByteBuffer.checkInput(value);
        //writing string
        final byte[] bytes = value.getBytes();
        this.writeInt(bytes.length);
        for (final byte b : bytes) {
            this.writeByte(b);
        }
    }

    public void writeUUID(final UUID value) {
        //check value
        WritingByteBuffer.checkInput(value);
        //writing uuid
        this.writeString(value.toString());
    }

    public byte[] toBytes() {
        //convert to byte array
        return this.byteBuf.toBytes();
    }

    private static void checkInput(final Object o) {
        //check o to not be null
        if (o == null) {
            throw new IllegalStateException("The object to be sent may not be null.");
        }
    }
}
