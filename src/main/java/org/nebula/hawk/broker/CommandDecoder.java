package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.channel.Decoder;

import java.io.IOException;

public class CommandDecoder implements Decoder {

    @Override
    public Message decode(ByteBuf in) throws IOException {
        if (in.remaining() < 4)
            return null;
        in.mark();
        int size = in.getInt();
        if (in.remaining() < size) {
            in.reset();
            return null;
        }
        int type = in.getInt();
        switch (type) {
            case Command.PUBLICATION:
                return new Publication(in);
            case Command.SUBSCRIPTION:
                return new Subscription(in);
        }
        throw new IOException("Parsing failed");
    }
}
