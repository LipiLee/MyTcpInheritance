package com.lipisoft.tcp;

public class Port {
    private final short source;
    private final short destination;

    public Port(final short source, final short destination) {
        this.source = source;
        this.destination = destination;
    }

    public short getSource() {
        return source;
    }

    public short getDestination() {
        return destination;
    }
}
