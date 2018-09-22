package org.nebula.hawk.channel;

import org.nebula.hawk.Message;
import org.nebula.hawk.buffer.BufferManager;
import org.nebula.hawk.buffer.StaticBufferManager;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.logging.Logger;


public abstract class EventLoop implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(EventLoop.class.getName());
    protected SelectableChannel channel;
    protected Selector selector;
    protected BufferManager bufferManager;
    protected Decoder decoder;
    protected Encoder encoder;
    protected Handler handler;

    public EventLoop(Decoder decoder, Encoder encoder, Handler handler) throws IOException {
        bufferManager = new StaticBufferManager();
        this.decoder = decoder;
        this.encoder = encoder;
        this.handler = handler;
        selector = Selector.open();
    }

    @Override
    public void run() {
        try {
            while (channel.isOpen()) {
                beforeProcessKey();
                selector.selectNow();
                Iterator<SelectionKey> keyIterator = this.selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    processKey(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void beforeProcessKey() {
    }

    protected void processKey(SelectionKey key) throws IOException {
    }

    protected void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        LOGGER.info("Socket incoming");
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, new Socket(bufferManager));
    }

    protected void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = (Socket) key.attachment();
        socket.inboundBuffer.writeMode();
        socket.inboundBuffer.read(channel);
        Message message;
        socket.inboundBuffer.readMode();
        socket.outboundBuffer.writeMode();
        while ((message = decoder.decode(socket.inboundBuffer)) != null) {
            handler.handle(message, key, encoder);
        }
    }

    protected void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = (Socket) key.attachment();
        socket.outboundBuffer.readMode();
        socket.outboundBuffer.write(channel);
        if (socket.outboundBuffer.remaining() == 0)
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
    }

    public void shutdown() throws IOException {
        channel.close();
    }
}
