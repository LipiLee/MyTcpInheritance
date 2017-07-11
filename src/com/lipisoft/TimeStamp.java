package com.lipisoft;

public class TimeStamp {
    private final long sender;
    private final long echoReply;

    TimeStamp(long sender, long echoReply) {
        this.sender = sender;
        this.echoReply = echoReply;
    }

    public long getSender() {
        return sender;
    }

    public long getEchoReply() {
        return echoReply;
    }
}
