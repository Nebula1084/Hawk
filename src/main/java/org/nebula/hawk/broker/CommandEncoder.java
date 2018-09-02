package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.channel.Encoder;

public class CommandEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf out) {
        message.encode(out);
    }
}
