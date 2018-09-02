package org.nebula.hawk;

import org.nebula.hawk.buffer.ByteBuf;

public abstract class Message {
    public Message() {
    }

    public Message(ByteBuf in) {
    }

    public abstract void encode(ByteBuf out);
}
