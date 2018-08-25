package org.nebula.hawk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final int port;
    private ServerSocketChannel socketChannel;
    private Selector selector;

    private ByteBuffer buffer = ByteBuffer.allocate(256);

    Server(int port) throws IOException {
        this.port = port;
        this.socketChannel = ServerSocketChannel.open();
        this.socketChannel.socket().bind(new InetSocketAddress(port));
        this.socketChannel.configureBlocking(false);
        this.selector = Selector.open();

        this.socketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    void start() {
        try {
            LOGGER.info("Server starting....");
            while (this.socketChannel.isOpen()) {
                selector.select();
                Iterator<SelectionKey> keyIterator = this.selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isAcceptable()) {
                        this.handleAccept(key);
                    } else if (key.isReadable()) {
                        this.handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        LOGGER.info("Socket incoming");
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder stringBuilder = new StringBuilder();
        buffer.clear();

        int read;
        while ((read = channel.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            stringBuilder.append(new String(bytes));
            buffer.clear();
        }

        if (read < 0) {
            LOGGER.info("Socket is closed");
            channel.close();
        } else {
            LOGGER.info(stringBuilder.toString());
        }

        buffer.clear();
        ByteBuffer response = ByteBuffer.wrap("Server response\n".getBytes());
        channel.write(response);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8000);
        server.start();
    }

}
