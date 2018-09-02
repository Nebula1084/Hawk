package org.nebula.hawk.channel;

import org.nebula.hawk.buffer.BufferManager;
import org.nebula.hawk.buffer.ByteBuf;

public class Socket {
    public ByteBuf inboundBuffer;
    public ByteBuf outboundBuffer;

    public Socket(BufferManager bufferManager) {
        inboundBuffer = bufferManager.create();
        outboundBuffer = bufferManager.create();
    }


}
