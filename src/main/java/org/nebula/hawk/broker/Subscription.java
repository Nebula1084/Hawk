package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Varchar;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.channel.Encoder;

import java.nio.channels.SelectionKey;

public class Subscription extends Message implements Command {
    private Varchar topic;

    public Subscription(String topic) {
        super();
        this.topic = new Varchar(topic);
    }

    Subscription(ByteBuf in) {
        super(in);
        this.topic = new Varchar(in);
    }

    @Override
    public void encode(ByteBuf out) {
        out.putInt(4 + topic.size());
        out.putInt(SUBSCRIPTION);
        topic.encode(out);
    }

    public Varchar topic() {
        return topic;
    }

    @Override
    public String toString() {
        return String.format("topic:%s", topic);
    }

    @Override
    public void execute(Broker broker, SelectionKey key, Encoder encoder) {
        broker.subscribe(topic, key);
    }
}
