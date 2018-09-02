package org.nebula.hawk;

import org.nebula.hawk.broker.Publication;
import org.nebula.hawk.broker.Subscription;
import org.nebula.hawk.buffer.BufferManager;
import org.nebula.hawk.buffer.ByteBuf;
import org.nebula.hawk.buffer.StaticBufferManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Client {
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private ByteBuf buffer;
    private BufferManager bufferManager;
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

    interface Listener {
        void handle(Message message);
    }

    private SocketChannel socketChannel;


    public Client(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.socketChannel = SocketChannel.open(address);
        bufferManager = new StaticBufferManager();
        buffer = bufferManager.create();

    }

    public void subscribe(String topic, Listener listener) throws IOException {
        Subscription subscription = new Subscription(topic);
        subscription.encode(buffer);
        buffer.write(socketChannel);
    }

    public void publish(String topic, String content) throws IOException {
        Publication publication = new Publication(topic, content);
        publication.encode(buffer);
        buffer.write(socketChannel);
    }

    public void close() throws IOException {
        this.socketChannel.close();
    }
}
