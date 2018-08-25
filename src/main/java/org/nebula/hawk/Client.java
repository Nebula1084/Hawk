package org.nebula.hawk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    interface Listener {
        void handle(Message message);
    }

    private SocketChannel socketChannel;

    public Client(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.socketChannel = SocketChannel.open(address);

    }

    public void subscribe(Topic topic, Listener listener) throws IOException {
        ByteBuffer buffer= ByteBuffer.wrap("Try subscribe".getBytes());
        socketChannel.write(buffer);
        buffer.clear();
    }

    public void publish(Topic topic, Message message) {

    }
}
