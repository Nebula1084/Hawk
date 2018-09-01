package org.nebula.hawk.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class StaticByteBuf implements ByteBuf {
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private static final int READ = 0;
    private static final int WRITE = 1;

    private ByteBuffer buffer;
    private int mode;
    private boolean endOfStream;

    StaticByteBuf() {
        this(BUFFER_SIZE);
    }

    StaticByteBuf(int size) {
        buffer = ByteBuffer.allocate(size);
        mode = WRITE;
    }

    private void checkRemainingAndCompact(int size) {
        if (buffer.remaining() < size)
            buffer.compact();
    }

    private void checkModeAndFlip(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            buffer.flip();
        }
    }

    @Override
    public ByteBuf mark() {
        buffer.mark();
        return this;
    }

    @Override
    public ByteBuf reset() {
        buffer.reset();
        return this;
    }

    @Override
    public int remaining() {
        return buffer.remaining();
    }

    @Override
    public ByteBuf get(byte[] dst) {
        checkModeAndFlip(READ);
        buffer.get(dst);
        return this;
    }

    @Override
    public ByteBuf put(byte[] src) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(src.length);
        buffer.put(src);
        return this;
    }

    @Override
    public char getChar() {
        checkModeAndFlip(READ);
        return buffer.getChar();
    }

    @Override
    public ByteBuf putChar(char value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(2);
        buffer.putChar(value);
        return this;
    }

    @Override
    public short getShort() {
        checkModeAndFlip(READ);
        return buffer.getShort();
    }

    @Override
    public ByteBuf putShort(short value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(2);
        buffer.putShort(value);
        return this;
    }

    @Override
    public int getInt() {
        this.checkModeAndFlip(READ);
        return buffer.getInt();
    }

    @Override
    public ByteBuf putInt(int value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(4);
        buffer.putInt(value);
        return this;
    }

    @Override
    public long getLong() {
        checkModeAndFlip(READ);
        return buffer.getLong();
    }

    @Override
    public ByteBuf putLong(long value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(8);
        buffer.putLong(value);
        return this;
    }

    @Override
    public float getFloat() {
        this.checkModeAndFlip(READ);
        return buffer.getFloat();
    }

    @Override
    public ByteBuf putFloat(float value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(4);
        buffer.putFloat(value);
        return this;
    }

    @Override
    public double getDouble() {
        checkModeAndFlip(READ);
        return buffer.getDouble();
    }

    @Override
    public ByteBuf putDouble(double value) {
        checkModeAndFlip(WRITE);
        checkRemainingAndCompact(8);
        buffer.putDouble(value);
        return this;
    }

    @Override
    public int read(SocketChannel channel) throws IOException {
        this.checkModeAndFlip(WRITE);
        endOfStream = false;
        int bytesRead = channel.read(buffer);
        int totalBytesRead = bytesRead;

        while (bytesRead > 0) {
            bytesRead = channel.read(buffer);
            totalBytesRead += bytesRead;
        }
        if (bytesRead == -1) {
            endOfStream = true;
        }

        return totalBytesRead;
    }

    @Override
    public boolean endOfStream() {
        return endOfStream;
    }

    @Override
    public int write(SocketChannel channel) throws IOException {
        this.checkModeAndFlip(READ);
        int bytesWritten = channel.write(buffer);
        int totalBytesWritten = bytesWritten;
        while (bytesWritten > 0 && buffer.hasRemaining()) {
            bytesWritten = channel.write(buffer);
            totalBytesWritten += bytesWritten;
        }
        return totalBytesWritten;
    }
}
