package org.nebula.hawk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegrateTest {
    private final static Logger LOGGER = Logger.getLogger(IntegrateTest.class.getName());

    private static int port = 18000;

    @BeforeClass
    public static void setupServer() throws InterruptedException {
        Server server;

        while (true) {
            try {
                server = new Server(port);
                break;
            } catch (IOException e) {
                port += 1;
                LOGGER.log(Level.INFO, e.getMessage());
                LOGGER.info(String.format("Try port %d\n", port));
            }
        }
        new Thread(server::start).start();
        Thread.sleep(1000);
    }

    @Test(timeout = 5000)
    public void testSubscribe() throws IOException, InterruptedException {
        Client client = new Client("localhost", port);
        client.subscribe("BLACK DESERT", null);
        Thread.sleep(1000);
        client.shutdown();
        LOGGER.info("shutdown");
    }


    @Test(timeout = 5000)
    public void testPublish() throws IOException, InterruptedException {
        Client producer = new Client("localhost", port);
        Client consumer = new Client("localhost", port);
        final Queue<Message> messages = new ArrayBlockingQueue<>(10);
        consumer.subscribe("TOPIC1", messages::add);
        Thread.sleep(1000);
        producer.publish("TOPIC1", "Content1");
        Thread.sleep(1000);
        for (Message message : messages) {
            LOGGER.info(message.toString());
        }
        Assert.assertEquals(messages.size(), 1);
        producer.shutdown();
        consumer.shutdown();
        LOGGER.info("shutdown");
    }


}
