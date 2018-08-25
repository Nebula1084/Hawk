package org.nebula.hawk;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
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
    public void testSubscribe() throws IOException {
        Client client = new Client("localhost", port);
        client.subscribe(null, null);

    }
}
