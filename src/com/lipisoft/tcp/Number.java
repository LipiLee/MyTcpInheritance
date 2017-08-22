package com.lipisoft.tcp;

public class Number {
    private final int sequence;
    private final int acknowledgement;

    public Number(final int sequence, final int acknowledgement) {
        this.sequence = sequence;
        this.acknowledgement = acknowledgement;
    }

    public int getSequence() {
        return sequence;
    }

    public int getAcknowledgement() {
        return acknowledgement;
    }
}
