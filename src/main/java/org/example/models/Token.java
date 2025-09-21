package org.example.models;

public class Token {
    public enum Type {
        LITERAL,
        MATCH
    }

    private final Type type;
    private final byte literal;
    private final int offset;
    private final int length;

    public Token(byte literal){
        this.type = Type.LITERAL;
        this.literal = literal;
        this.offset = 0;
        this.length = 0;
    }

    public Token(int offset, int length){
        this.type = Type.MATCH;
        this.literal = 0;
        this.offset = offset;
        this.length = length;
    }

    public Type getType() {
        return type;
    }

    public byte getLiteral() {
        return literal;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
