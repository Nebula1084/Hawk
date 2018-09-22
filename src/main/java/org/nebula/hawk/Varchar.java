package org.nebula.hawk;

import org.nebula.hawk.buffer.ByteBuf;

import java.util.Arrays;

public class Varchar extends Message {
    private byte[] characters;
    private int length;

    public Varchar(String s) {
        super();
        this.characters = s.getBytes();
        this.length = this.characters.length;
    }

    public Varchar(ByteBuf in) {
        super(in);
        this.length = in.getInt();
        characters = new byte[this.length];
        in.get(characters);
    }

    @Override
    public void encode(ByteBuf out) {
        out.putInt(this.length);
        out.put(this.characters);
    }

    public int size() {
        return characters.length + 4;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Varchar) {
            Varchar that = (Varchar) obj;
            return this.length == that.length && Arrays.equals(this.characters, that.characters);
        } else
            return false;
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
