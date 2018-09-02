package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.channel.Decoder;

public class CommandDecoder implements Decoder {

    @Override
    public Message decode(ByteBuf in) {
        int type = in.getInt();
        switch (type) {
            case Command.PUBLICATION:
                return new Publication(in);
            case Command.SUBSCRIPTION:
                return new Subscription(in);
        }
        return null;
    }
}
