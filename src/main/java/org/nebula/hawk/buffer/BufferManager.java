package org.nebula.hawk.buffer;

import java.nio.ByteBuffer;

public abstract class BufferManager {

    private static final int SEGMENT_SIZE = 1024;

    ByteBuffer allocate() {
        return ByteBuffer.allocate(SEGMENT_SIZE);
    }

    void free(ByteBuffer segment) {
    }

    public abstract ByteBuf create();
}
