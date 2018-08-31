package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Varchar;

import java.nio.ByteBuffer;

public class Publication extends Message implements Command{
    private Varchar content;
    private Varchar topic;

    public Publication(String topic, String content) {
        super();
        this.topic = new Varchar(topic);
        this.content = new Varchar(content);
    }

    Publication(ByteBuffer in) {
        super(in);
        this.topic = new Varchar(in);
        this.content = new Varchar(in);
    }

    @Override
    public void encode(ByteBuffer out) {
        out.putInt(PUBLICATION);
        topic.encode(out);
        content.encode(out);
    }

    @Override
    public String toString() {
        return String.format("topic:%s, content:%s",topic,content);
    }
}
