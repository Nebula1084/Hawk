package org.nebula.hawk;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Varchar extends Message {
    private byte[] characters;
    private int length;

    public Varchar(String s) {
        super();
        this.characters = s.getBytes();
        this.length = this.characters.length;
    }

    public Varchar(ByteBuffer in) {
        super(in);
        this.length = in.getInt();
        characters = new byte[this.length];
        in.get(characters);
    }

    public void encode(ByteBuffer out){
        out.putInt(this.length);
        out.put(this.characters);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.characters);
    }

    @Override
    public String toString() {
        return new String(this.characters);
    }
}
