package org.nebula.hawk.channel;

import org.jctools.queues.SpscUnboundedArrayQueue;
import org.nebula.hawk.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.logging.Logger;

public class ClientEventLoop extends EventLoop {
    private final static Logger LOGGER = Logger.getLogger(ClientEventLoop.class.getName());
    private SelectionKey selectionKey;
    private Queue<Message> queue;


    public ClientEventLoop(Decoder decoder, Encoder encoder, Handler handler) throws IOException {
        super(decoder, encoder, handler);
        queue = new SpscUnboundedArrayQueue<>(1024);
    }

    public void connect(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        channel = SocketChannel.open(address);
        channel.configureBlocking(false);
        selectionKey = channel.register(selector, SelectionKey.OP_READ, new Socket(bufferManager));
    }

    @Override
    protected void processKey(SelectionKey key) throws IOException {
        if (key.isReadable()) {
            handleRead(key);
        } else if (key.isWritable()) {
            handleWrite(key);
        }
    }

    @Override
    protected void beforeProcessKey() {
        Message message;
        Socket socket = (Socket) selectionKey.attachment();
        LOGGER.info("here");
        boolean hasMessage = false;
        while ((message = queue.poll()) != null) {
            encoder.encode(message, socket.outboundBuffer);
            hasMessage = true;
        }
        if (hasMessage)
            selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
    }

    public void send(Message message) {
        queue.offer(message);
    }
}
