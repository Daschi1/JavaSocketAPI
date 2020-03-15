package de.javasocketapi.core;

import org.boon.primitive.ByteBuf;

import java.util.UUID;

public class WritingByteBuffer {
    private ByteBuf byteBuf;

    WritingByteBuffer() {
        this.byteBuf = ByteBuf.create(0);
    }

    public void writeBoolean(boolean value) {
        //writing boolean
        this.byteBuf.addByte(value ? 1 : 0);
    }

    public void writeByte(byte value) {
        //writing byte
        this.byteBuf.add(value);
    }

    public void writeShort(short value) {
        //writing short
        this.byteBuf.add(value);
    }

    public void writeInt(int value) {
        //writing int
        this.byteBuf.add(value);
    }

    public void writeLong(long value) {
        //writing long
        this.byteBuf.add(value);
    }

    public void writeFloat(float value) {
        //writing float
        this.byteBuf.add(value);
    }

    public void writeDouble(double value) {
        //writing double
        this.byteBuf.add(value);
    }

    public void writeChar(char value) {
        //writing char
        this.writeByte((byte) value);
    }

    public void writeString(String value) {
        //writing string
        int length = value.length();
        this.writeInt(length);
        for (byte b : value.getBytes()) {
            this.writeByte(b);
        }
    }

    public void writeUUID(UUID value) {
        //writing uuid
        this.writeString(value.toString());
    }

    public byte[] toBytes() {
        //convert to byte array
        return this.byteBuf.toBytes();
    }

}
