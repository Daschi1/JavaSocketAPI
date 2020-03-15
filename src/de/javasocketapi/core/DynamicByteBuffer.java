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

    public String getString() {
        int lenght = getInt();
        byte[] bytes = new byte[lenght];
        for (int i = 0; i < lenght; i++) {
            bytes[i] = get();
        }
        return new String(bytes);
    }

    public ByteOrder order() {
        return byteBuffer.order();
    }

    public ByteBuffer order(ByteOrder bo) {
        return byteBuffer.order(bo);
    }

    public void put(byte b) {
        ensureSpace(1 + 1);
        int index = this.writeIndex;
        this.writeIndex += 1;
        byteBuffer.put(index, b);
    }

    public void put(byte[] src) {
        ensureSpace(src.length);
        byteBuffer.put(src);
    }

    public void put(ByteBuffer src) {
        ensureSpace(src.remaining());
        byteBuffer.put(src);
    }

    public void putChar(char value) {
        ensureSpace(2 + 1);
        int index = this.writeIndex;
        this.writeIndex += 2;
        byteBuffer.putChar(index, value);
    }

    public void putDouble(double value) {
        ensureSpace(8 + 1);
        int index = this.writeIndex;
        this.writeIndex += 8;
        byteBuffer.putDouble(index, value);
    }

    public void putFloat(float value) {
        ensureSpace(4 + 1);
        int index = this.writeIndex;
        this.writeIndex += 4;
        byteBuffer.putFloat(index, value);
    }

    public void putInt(int value) {
        ensureSpace(4 + 1);
        int index = this.writeIndex;
        this.writeIndex += 4;
        byteBuffer.putInt(index, value);
    }

    public void putLong(long value) {
        ensureSpace(8 + 1);
        int index = this.writeIndex;
        this.writeIndex += 8;
        byteBuffer.putLong(index, value);
    }

    public void putShort(short value) {
        ensureSpace(2 + 1);
        int index = this.writeIndex;
        this.writeIndex += 2;
        byteBuffer.putShort(index, value);
    }

    public void putString(String value) {
        putInt(value.length());
        for (byte b : value.getBytes()) {
            put(b);
        }
    }

    public static void main(String[] args) {
        DynamicByteBuffer dynamicByteBuffer = new DynamicByteBuffer();
        /*dynamicByteBuffer.putString("Hello World!");
        dynamicByteBuffer.putString("Bye World!");
        System.out.println(dynamicByteBuffer.getString());
        System.out.println(dynamicByteBuffer.getString());*/
        dynamicByteBuffer.putInt(0);
        dynamicByteBuffer.putInt(1);
        dynamicByteBuffer.putInt(2);
        dynamicByteBuffer.putInt(3);
        dynamicByteBuffer.putInt(4);
        dynamicByteBuffer.putInt(5);
        dynamicByteBuffer.putInt(6);
        dynamicByteBuffer.putInt(7);
        dynamicByteBuffer.putInt(8);
        dynamicByteBuffer.putInt(9);
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
        System.out.println(dynamicByteBuffer.getInt());
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
