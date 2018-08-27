package org.nebula.hawk;

public class TopicImpl implements Topic {

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}
