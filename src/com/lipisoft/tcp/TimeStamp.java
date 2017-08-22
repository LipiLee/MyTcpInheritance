package com.lipisoft.tcp;

public class TimeStamp {
    private final int sender;
    private final int echoReply;

    public TimeStamp(int sender, int echoReply) {
        this.sender = sender;
        this.echoReply = echoReply;
    }

    public int getSender() {
        return sender;
    }

    public int getEchoReply() {
        return echoReply;
    }

    boolean isValid() {
        return sender != 0 || echoReply != 0;
    }
}
