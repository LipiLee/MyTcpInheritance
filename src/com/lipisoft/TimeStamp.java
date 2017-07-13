package com.lipisoft;

class TimeStamp {
    private final int sender;
    private final int echoReply;

    TimeStamp(int sender, int echoReply) {
        this.sender = sender;
        this.echoReply = echoReply;
    }

    int getSender() {
        return sender;
    }

    int getEchoReply() {
        return echoReply;
    }
}
