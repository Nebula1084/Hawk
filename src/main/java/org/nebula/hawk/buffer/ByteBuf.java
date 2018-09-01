package org.nebula.hawk.buffer;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ByteBuf {

    ByteBuf mark();

    ByteBuf reset();

    int remaining();

    ByteBuf get(byte[] dst);

    ByteBuf put(byte[] src);

    char getChar();

    ByteBuf putChar(char value);

    short getShort();

    ByteBuf putShort(short value);

    int getInt();

    ByteBuf putInt(int value);

    long getLong();

    ByteBuf putLong(long value);

    float getFloat();

    ByteBuf putFloat(float value);

    double getDouble();

    ByteBuf putDouble(double value);

    int read(SocketChannel channel) throws IOException;

    boolean endOfStream();

    int write(SocketChannel channel) throws IOException;

}
