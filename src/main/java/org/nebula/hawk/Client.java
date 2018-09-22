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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
    private ClientEventLoop eventLoop;
    private Map<Varchar, Consumer> consumerMap;

    interface Consumer {
        void handle(Message message);
    }

    class MessageHandler implements Handler {

        @Override
        public void handle(Message message, SelectionKey key, Encoder encoder) {
            Publication publication = (Publication) message;
            Consumer consumer = consumerMap.get(publication.topic());
            if (consumer != null) {
                consumer.handle(publication.content());
            }
        }
    }


    public Client(String host, int port) throws IOException {
        Decoder decoder = new CommandDecoder();
        Encoder encoder = new CommandEncoder();
        Handler handler = new MessageHandler();
        consumerMap = new HashMap<>();
        eventLoop = new ClientEventLoop(decoder, encoder, handler);
        eventLoop.connect(host, port);
        new Thread(eventLoop).start();
    }

    public void subscribe(String topic, Consumer consumer) {
        Subscription subscription = new Subscription(topic);
        consumerMap.put(subscription.topic(), consumer);
        eventLoop.send(subscription);
    }

    public void publish(String topic, String content) {
        Publication publication = new Publication(topic, content);
        eventLoop.send(publication);
    }

    public void shutdown() throws IOException {
        eventLoop.shutdown();
    }
}
