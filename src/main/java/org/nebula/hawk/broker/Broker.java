package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Varchar;
import org.nebula.hawk.channel.Encoder;
import org.nebula.hawk.channel.Socket;

import java.nio.channels.SelectionKey;
import java.util.*;

public class Broker {
    private Map<Varchar, List<SelectionKey>> subscriptionList;

    public Broker() {
        subscriptionList = new HashMap<>();
    }

    public void subscribe(Varchar topic, SelectionKey key) {
        List<SelectionKey> channelList = subscriptionList.computeIfAbsent(topic, k -> new LinkedList<>());
        channelList.add(key);
    }

    public void publish(Varchar topic, Message message, Encoder encoder) {
        List<SelectionKey> channelList = subscriptionList.getOrDefault(topic, new LinkedList<>());
        Iterator<SelectionKey> channelIterator = channelList.iterator();
        while (channelIterator.hasNext()) {
            SelectionKey key = channelIterator.next();
            if (!key.isValid()) {
                channelIterator.remove();
                continue;
            }
            Socket socket = (Socket) key.attachment();
            encoder.encode(message, socket.outboundBuffer);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
    }

}
