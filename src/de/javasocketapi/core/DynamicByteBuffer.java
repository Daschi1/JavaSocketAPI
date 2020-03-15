package de.javasocketapi.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DynamicByteBuffer implements Comparable<ByteBuffer> {
    private ByteBuffer byteBuffer;
    private float expandFactor;
    private int writeIndex;
    private int readIndex;

    DynamicByteBuffer(int initialCapacity, float expandFactor) {
        if (expandFactor < 2) {
            throw new IllegalArgumentException(
                    "The expand factor must be greater or equal to 2!");
        }
        this.byteBuffer = ByteBuffer.allocate(initialCapacity);
        this.expandFactor = expandFactor;
        this.writeIndex = 0;
        this.readIndex = 0;
    }

    DynamicByteBuffer(int initialCapacity) {
        this(initialCapacity, 2);
    }

    DynamicByteBuffer() {
        this(16);
    }

    DynamicByteBuffer(int initialCapacity, float expandFactor, byte... bytes) {
        this(initialCapacity, expandFactor);
        put(bytes);
    }

    DynamicByteBuffer(int initialCapacity, byte... bytes) {
        this(initialCapacity, 2, bytes);
    }

    DynamicByteBuffer(byte... bytes) {
        this(16, 2, bytes);
    }

    public byte[] array() {
        return byteBuffer.array();
    }

    public int compareTo(ByteBuffer that) {
        return byteBuffer.compareTo(that);
    }

    public int remaining() {
        return byteBuffer.remaining();
    }

    public boolean equals(Object ob) {
        return byteBuffer.equals(ob);
    }

    public byte get() {
        int index = this.readIndex;
        this.readIndex += 1;
        return byteBuffer.get(index);
    }

    public char getChar() {
        int index = this.readIndex;
        this.readIndex += 2;
        return byteBuffer.getChar(index);
    }

    public double getDouble() {
        int index = this.readIndex;
        this.readIndex += 8;
        return byteBuffer.getDouble(index);
    }

    public float getFloat() {
        int index = this.readIndex;
        this.readIndex += 4;
        return byteBuffer.getFloat(index);
    }

    public int getInt() {
        int index = this.readIndex;
        this.readIndex += 4;
        return byteBuffer.getInt(index);
    }

    public long getLong() {
        int index = this.readIndex;
        this.readIndex += 8;
        return byteBuffer.getLong(index);
    }

    public short getShort() {
        int index = this.readIndex;
        this.readIndex += 2;
        return byteBuffer.getShort(index);
    }

    public ByteOrder order() {
        return byteBuffer.order();
    }

    public ByteBuffer order(ByteOrder bo) {
        return byteBuffer.order(bo);
    }

    public ByteBuffer put(byte b) {
        ensureSpace(1 + 1);
        int index = this.writeIndex;
        this.writeIndex += 1;
        return byteBuffer.put(index, b);
    }

    public ByteBuffer put(byte[] src) {
        ensureSpace(src.length);
        return byteBuffer.put(src);
    }

    public ByteBuffer put(ByteBuffer src) {
        ensureSpace(src.remaining());
        return byteBuffer.put(src);
    }

    public ByteBuffer putChar(char value) {
        ensureSpace(2 + 1);
        int index = this.writeIndex;
        this.writeIndex += 2;
        return byteBuffer.putChar(index, value);
    }

    public ByteBuffer putDouble(double value) {
        ensureSpace(8 + 1);
        int index = this.writeIndex;
        this.writeIndex += 8;
        return byteBuffer.putDouble(index, value);
    }

    public ByteBuffer putFloat(float value) {
        ensureSpace(4 + 1);
        int index = this.writeIndex;
        this.writeIndex += 4;
        return byteBuffer.putFloat(index, value);
    }

    public ByteBuffer putInt(int value) {
        ensureSpace(4 + 1);
        int index = this.writeIndex;
        this.writeIndex += 4;
        return byteBuffer.putInt(index, value);
    }

    public ByteBuffer putLong(long value) {
        ensureSpace(8 + 1);
        int index = this.writeIndex;
        this.writeIndex += 8;
        return byteBuffer.putLong(index, value);
    }

    public ByteBuffer putShort(short value) {
        ensureSpace(2 + 1);
        int index = this.writeIndex;
        this.writeIndex += 2;
        return byteBuffer.putShort(index, value);
    }

    @Override
    public int hashCode() {
        return byteBuffer.hashCode();
    }

    @Override
    public String toString() {
        return byteBuffer.toString();
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public float getExpandFactor() {
        return expandFactor;
    }

    public int getWriteIndex() {
        return writeIndex;
    }

    public int getReadIndex() {
        return readIndex;
    }

    private void ensureSpace(int needed) {
        if (remaining() >= needed) {
            return;
        }
        int newCapacity = (int) (byteBuffer.capacity() * expandFactor);
        while (newCapacity < (byteBuffer.capacity() + needed)) {
            newCapacity *= expandFactor;
        }
        ByteBuffer expanded = ByteBuffer.allocate(newCapacity);
        expanded.order(byteBuffer.order());
        expanded.put(byteBuffer.array());
        byteBuffer = expanded;
    }
}
