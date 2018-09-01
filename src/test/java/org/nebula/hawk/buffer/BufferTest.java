package org.nebula.hawk.buffer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Logger;

public abstract class BufferTest {
    private final static Logger LOGGER = Logger.getLogger(BufferTest.class.getName());

    ByteBuf buffer;

    @Test
    public void testChar() {
        Random random = new Random();
        Queue<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                char c = (char) (random.nextInt(26) + 'a');
                queue.add(c);
                buffer.putChar(c);
            }
            for (int j = 0; j < 100; j++) {
                Character character = queue.poll();
                Character c = buffer.getChar();
                Assert.assertEquals(character, c);
            }
        }
    }

    @Test
    public void testShort() {
        LOGGER.info("test short");
    }
}
