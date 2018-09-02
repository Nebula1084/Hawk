package org.nebula.hawk.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class ServerEventLoop extends EventLoop {
    public ServerEventLoop(Decoder decoder, Encoder encoder, Handler handler) throws IOException {
        super(decoder, encoder, handler);
    }

    public void bind(int port) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        channel = serverSocket;
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    protected void processKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handleAccept(key);
        } else if (key.isReadable()) {
            handleRead(key);
        } else if (key.isWritable()) {
            handleWrite(key);
        }
    }

}
