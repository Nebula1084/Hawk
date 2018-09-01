package org.nebula.hawk.buffer;

import org.junit.Before;

public class StaticBufferTest extends BufferTest {

    @Before
    public void initialize() {
        buffer = new StaticByteBuf(256);
    }


}
