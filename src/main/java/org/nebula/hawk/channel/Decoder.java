package org.nebula.hawk.channel;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.ByteBuf;

public interface Decoder {
    Message decode(ByteBuf in);
}
