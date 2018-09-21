package org.nebula.hawk.buffer;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class ByteBuf {

    protected static final int READ = 0;
    protected static final int WRITE = 1;

    protected int mode;


    public void readMode() {
        checkModeAndFlip(READ);
    }

    public void writeMode() {
        checkModeAndFlip(WRITE);
    }

    abstract protected void checkModeAndFlip(int mode);

    abstract public ByteBuf mark();

    abstract public ByteBuf reset();

    abstract public int remaining();

    abstract public ByteBuf get(byte[] dst);

    abstract public ByteBuf put(byte[] src);

    abstract public char getChar();

    abstract public ByteBuf putChar(char value);

    abstract public short getShort();

    abstract public ByteBuf putShort(short value);

    abstract public int getInt();

    abstract public ByteBuf putInt(int value);

    abstract public long getLong();

    abstract public ByteBuf putLong(long value);

    abstract public float getFloat();

    abstract public ByteBuf putFloat(float value);

    abstract public double getDouble();

    abstract public ByteBuf putDouble(double value);

    abstract public int read(SocketChannel channel) throws IOException;

    abstract public boolean endOfStream();

    abstract public int write(SocketChannel channel) throws IOException;

}
