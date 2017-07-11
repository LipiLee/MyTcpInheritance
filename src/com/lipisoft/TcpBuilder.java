package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.List;

abstract class TcpBuilder {
    int sourcePort;
    int destinationPort;

    // The size of seq and ack is enough 32 bits(int size),
    // but their type is long, not int because its value can be changed
    long seq;
    long ack;

    byte dataOffset;
    boolean NS;
    boolean CWR;
    boolean ECE;
    boolean URG;
    boolean ACK;
    boolean PSH;
    boolean RST;
    boolean SYN;
    boolean FIN;

    // The window size is enough 16 bits,
    // but their type is int, not short because its value can be changed
    int windowSize;
    int checksum;
    int urgentPointer;

    // optional
    int maxSegmentSize = 0;
    short windowScale = 0;
    boolean selectiveAckPermitted = false;
    List<SelectiveAck> selectiveAcks = null;
    TimeStamp time = null;

    ByteBuffer tcpHeaderStream;
    ByteBuffer tcpOptionStream = null;
    ByteBuffer tcpPayloadStream = null;

    abstract Tcp build();

    TcpBuilder applyMaxSegmentSize(int maxSegmentSize) {
        this.maxSegmentSize = maxSegmentSize;
        return this;
    }

    TcpBuilder applyWindowScale(short windowScale) {
        this.windowScale = windowScale;
        return this;
    }

    TcpBuilder applySelectiveAckPermitted(boolean selectiveAckPermitted) {
        this.selectiveAckPermitted = selectiveAckPermitted;
        return this;
    }

    TcpBuilder applySelectiveAcks(@NotNull List<SelectiveAck> selectiveAcks) {
        this.selectiveAcks = selectiveAcks;
        return this;
    }

    TcpBuilder applyTimeStamp(@NotNull TimeStamp time) {
        this.time = time;
        return this;
    }

    TcpBuilder applyTcpPayload(@NotNull ByteBuffer tcpPayloadStream) {
        this.tcpPayloadStream = tcpPayloadStream;
        return this;
    }
}
