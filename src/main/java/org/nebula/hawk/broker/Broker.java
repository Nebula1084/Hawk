package org.nebula.hawk.broker;

import org.nebula.hawk.Varchar;
import org.nebula.hawk.channel.Encoder;
import org.nebula.hawk.channel.Socket;

import java.nio.channels.SelectionKey;
import java.util.*;
import java.util.logging.Logger;

public class Broker {
    private final static Logger LOGGER = Logger.getLogger(Broker.class.getName());
    private Map<Varchar, List<SelectionKey>> subscriptionList;

    public Broker() {
        subscriptionList = new HashMap<>();
    }

    public void subscribe(Varchar topic, SelectionKey key) {
        List<SelectionKey> channelList = subscriptionList.computeIfAbsent(topic, k -> new LinkedList<>());
        channelList.add(key);
    }

    public void publish(Publication publication, Encoder encoder) {
        List<SelectionKey> channelList = subscriptionList.getOrDefault(publication.topic(), new LinkedList<>());
        Iterator<SelectionKey> channelIterator = channelList.iterator();

        while (channelIterator.hasNext()) {
            SelectionKey key = channelIterator.next();
            if (!key.isValid()) {
                channelIterator.remove();
                continue;
            }
            Socket socket = (Socket) key.attachment();
            encoder.encode(publication, socket.outboundBuffer);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
    }

}
