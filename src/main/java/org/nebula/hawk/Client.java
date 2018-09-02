package org.nebula.hawk;

import org.nebula.hawk.broker.CommandDecoder;
import org.nebula.hawk.broker.CommandEncoder;
import org.nebula.hawk.broker.Publication;
import org.nebula.hawk.broker.Subscription;
import org.nebula.hawk.channel.ClientEventLoop;
import org.nebula.hawk.channel.Decoder;
import org.nebula.hawk.channel.Encoder;
import org.nebula.hawk.channel.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
    private ClientEventLoop eventLoop;

    interface Listener {
        void handle(Message message);
    }

    class MessageHandler implements Handler {

        @Override
        public void handle(Message message, SelectionKey key, Encoder encoder) {
            LOGGER.info(message.toString());
        }
    }


    public Client(String host, int port) throws IOException {
        Decoder decoder = new CommandDecoder();
        Encoder encoder = new CommandEncoder();
        Handler handler = new MessageHandler();
        eventLoop = new ClientEventLoop(decoder, encoder, handler);
        eventLoop.connect(host, port);
        new Thread(eventLoop).start();
    }

    public void subscribe(String topic, Listener listener) throws IOException {
        Subscription subscription = new Subscription(topic);
        eventLoop.send(subscription);
    }

    public void publish(String topic, String content) throws IOException {
        Publication publication = new Publication(topic, content);
        eventLoop.send(publication);
    }

    public void shutdown() throws IOException {
        eventLoop.shutdown();
    }
}
