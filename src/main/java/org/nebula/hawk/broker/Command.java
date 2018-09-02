package org.nebula.hawk.broker;

import org.nebula.hawk.channel.Encoder;

import java.nio.channels.SelectionKey;

public interface Command {
    int PUBLICATION = 1;
    int SUBSCRIPTION = 2;

    void execute(Broker broker, SelectionKey key, Encoder encoder);
}
