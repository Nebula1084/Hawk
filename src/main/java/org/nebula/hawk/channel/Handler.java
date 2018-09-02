package org.nebula.hawk.channel;

import org.nebula.hawk.Message;

import java.nio.channels.SelectionKey;

public interface Handler {
    void handle(Message message, SelectionKey key, Encoder encoder);
}
