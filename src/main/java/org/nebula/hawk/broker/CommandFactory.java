package org.nebula.hawk.broker;

import java.nio.ByteBuffer;

public class CommandFactory {
    public static Command create(ByteBuffer in){
        int type = in.getInt();
        switch (type){
            case Command.PUBLICATION:
                return new Publication(in);
            case Command.SUBSCRIPTION:
                return new Subscription(in);
        }
        return null;
    }
}
