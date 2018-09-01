package org.nebula.hawk.buffer;

public class StaticBufferManager extends BufferManager {
    @Override
    public ByteBuf create() {
        return new StaticByteBuf();
    }
}
