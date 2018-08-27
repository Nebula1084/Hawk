package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.Topic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Broker {
    private Map<Topic, List<SocketChannel>> subscriptionList;
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private ByteBuffer buffer;

    public Broker() {
        subscriptionList = new HashMap<>();
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public void subscribe(Topic topic, SocketChannel channel) {
        List<SocketChannel> channelList = subscriptionList.computeIfAbsent(topic, k -> new LinkedList<>());
        channelList.add(channel);
    }

    public void publish(Topic topic, Message message) throws IOException {
        List<SocketChannel> channelList = subscriptionList.getOrDefault(topic, new LinkedList<>());
        Iterator<SocketChannel> channelIterator = channelList.iterator();
        buffer.clear();
        buffer.put(message.toBytes());
        while (channelIterator.hasNext()) {
            SocketChannel channel = channelIterator.next();
            if (channel.isConnected()) {
                channel.write(buffer);
            } else {
                channelIterator.remove();
            }
        }
    }

}
