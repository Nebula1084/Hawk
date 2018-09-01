package org.nebula.hawk.buffer;

import java.nio.channels.SocketChannel;

public class DynamicByteBuf implements ByteBuf {
    @Override
    public ByteBuf get(byte[] dst) {
        return this;
    }

    @Override
    public ByteBuf put(byte[] src) {
        return this;
    }

    @Override
    public char getChar() {
        return 0;
    }

    @Override
    public ByteBuf putChar(char value) {
        return this;
    }

    @Override
    public short getShort() {
        return 0;
    }

    @Override
    public ByteBuf putShort(short value) {
        return this;
    }

    @Override
    public int getInt() {
        return 0;
    }

    @Override
    public ByteBuf putInt(int value) {
        return this;
    }

    @Override
    public long getLong() {
        return 0;
    }

    @Override
    public ByteBuf putLong(long value) {
        return this;
    }

    @Override
    public float getFloat() {
        return 0;
    }

    @Override
    public ByteBuf putFloat(float value) {
        return this;
    }

    @Override
    public double getDouble() {
        return 0;
    }

    @Override
    public ByteBuf putDouble(double value) {
        return this;
    }

    @Override
    public int read(SocketChannel channel) {
        return 0;
    }

    @Override
    public boolean endOfStream() {
        return false;
    }

    @Override
    public int write(SocketChannel channel) {
        return 0;
    }
}
