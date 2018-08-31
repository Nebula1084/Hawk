package org.nebula.hawk;

import org.nebula.hawk.broker.Publication;
import org.nebula.hawk.broker.Subscription;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Client {
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private ByteBuffer buffer;
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

    interface Listener {
        void handle(Message message);
    }

    private SocketChannel socketChannel;


    public Client(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.socketChannel = SocketChannel.open(address);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);

    }

    public void subscribe(String topic, Listener listener) throws IOException {
        buffer.clear();
        Subscription subscription = new Subscription(topic);
        subscription.encode(buffer);
        buffer.flip();
        socketChannel.write(buffer);
    }

    public void publish(String topic, String content) throws IOException {
        buffer.clear();
        Publication publication = new Publication(topic,content);
        publication.encode(buffer);
        socketChannel.write(buffer);
    }

    public void close() throws IOException {
        this.socketChannel.close();
    }
}
