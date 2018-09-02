package org.nebula.hawk;

import org.nebula.hawk.broker.CommandDecoder;
import org.nebula.hawk.broker.CommandEncoder;
import org.nebula.hawk.broker.CommandHandler;
import org.nebula.hawk.channel.Decoder;
import org.nebula.hawk.channel.Encoder;
import org.nebula.hawk.channel.EventLoop;
import org.nebula.hawk.channel.Handler;

import java.io.IOException;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    private EventLoop eventLoop;

    Server(int port) throws IOException {

        Decoder decoder = new CommandDecoder();
        Encoder encoder = new CommandEncoder();
        Handler handler = new CommandHandler();
        eventLoop = new EventLoop(decoder, encoder, handler);
        eventLoop.bind(port);
    }

    void start() {
        eventLoop.run();
    }


    public static void main(String[] args) throws IOException {
        Server server = new Server(8000);
        server.start();
    }

}
