package org.nebula.hawk;

import java.nio.ByteBuffer;

public abstract class Message {
    public Message() {
    }

    public Message(ByteBuffer in) {
    }

    public abstract void encode(ByteBuffer out);
}
