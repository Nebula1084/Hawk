package org.nebula.hawk.broker;

import org.nebula.hawk.Message;
import org.nebula.hawk.channel.Encoder;
import org.nebula.hawk.channel.Handler;

import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

public class CommandHandler implements Handler {
    private final static Logger LOGGER = Logger.getLogger(CommandHandler.class.getName());
    private Broker broker;

    public CommandHandler() {
        broker = new Broker();
    }

    @Override
    public void handle(Message message, SelectionKey key, Encoder encoder) {
        Command command = (Command) message;
        command.execute(broker, key, encoder);
    }
}
