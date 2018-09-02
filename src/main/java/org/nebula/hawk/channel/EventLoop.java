package org.nebula.hawk.channel;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.BufferManager;
import org.nebula.hawk.buffer.StaticBufferManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.logging.Logger;


public class EventLoop implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(EventLoop.class.getName());
    private SelectableChannel channel;
    private Selector selector;
    private BufferManager bufferManager;
    private Decoder decoder;
    private Encoder encoder;
    private Handler handler;

    public EventLoop(Decoder decoder, Encoder encoder, Handler handler) {
        bufferManager = new StaticBufferManager();
        this.decoder = decoder;
        this.encoder = encoder;
        this.handler = handler;
    }

    public void bind(int port) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        channel = serverSocket;
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            while (channel.isOpen()) {
                selector.select();
                Iterator<SelectionKey> keyIterator = this.selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    } else if (key.isWritable()) {
                        handleWrite(key);
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
        channel.register(selector, SelectionKey.OP_READ, new Socket(bufferManager));
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = (Socket) key.attachment();
        socket.inboundBuffer.read(channel);
        Message message;
        while ((message = decoder.decode(socket.inboundBuffer)) != null) {
            handler.handle(message, key, encoder);
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = (Socket) key.attachment();
        socket.outboundBuffer.write(channel);
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

    }
}
