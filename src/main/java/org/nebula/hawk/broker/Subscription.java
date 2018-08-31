package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Varchar;

import java.nio.ByteBuffer;

public class Subscription extends Message implements Command {
    private Varchar topic;

    public Subscription(String topic){
        super();
        this.topic = new Varchar(topic);
    }

    Subscription(ByteBuffer in){
        super(in);
        this.topic = new Varchar(in);
    }

    @Override
    public void encode(ByteBuffer out) {
        out.putInt(SUBSCRIPTION);
        this.topic.encode(out);
    }

    @Override
    public String toString() {
        return String.format("topic:%s",topic);
    }
}
