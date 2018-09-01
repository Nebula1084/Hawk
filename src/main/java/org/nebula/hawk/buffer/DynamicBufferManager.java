package org.nebula.hawk.buffer;

public class DynamicBufferManager extends BufferManager {
    @Override
    public ByteBuf create() {
        return new DynamicByteBuf(this);
    }
}
