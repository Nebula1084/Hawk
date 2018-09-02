package org.nebula.hawk.channel;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.ByteBuf;

public interface Encoder {
    void encode(Message message, ByteBuf out);
}
