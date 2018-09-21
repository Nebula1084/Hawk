package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Varchar;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.channel.Encoder;

import java.nio.channels.SelectionKey;

public class Publication extends Message implements Command {
    private Varchar content;
    private Varchar topic;

    public Publication(String topic, String content) {
        super();
        this.topic = new Varchar(topic);
        this.content = new Varchar(content);
    }

    Publication(ByteBuf in) {
        super(in);
        this.topic = new Varchar(in);
        this.content = new Varchar(in);
    }

    @Override
    public void encode(ByteBuf out) {
        out.putInt(4 + topic.size() + content.size());
        out.putInt(PUBLICATION);
        topic.encode(out);
        content.encode(out);
    }

    @Override
    public void execute(Broker broker, SelectionKey key, Encoder encoder) {
        broker.publish(topic, content, encoder);
    }

    @Override
    public String toString() {
        return String.format("topic:%s, content:%s", topic, content);
    }
}
