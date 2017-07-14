package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

abstract class TcpBuilder {
    int sourcePort;
    int destinationPort;

    int seq;
    int ack;

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

    short windowSize;
    short checksum;
    short urgentPointer;

    // optional
    short maxSegmentSize = -1;
    byte windowScale = -1;
    boolean selectiveAckPermitted = false;
    List<SelectiveAck> selectiveAcks = new ArrayList<>();
    TimeStamp time = new TimeStamp(-1, -1);

    ByteBuffer tcpStream;
    ByteBuffer tcpHeaderStream = null;
    ByteBuffer tcpOptionStream = ByteBuffer.allocate(0);
    ByteBuffer tcpPayloadStream = ByteBuffer.allocate(0);

    abstract Tcp build();

    @NotNull
    TcpBuilder applyMaxSegmentSize(short maxSegmentSize) {
        this.maxSegmentSize = maxSegmentSize;
        return this;
    }

    @NotNull
    TcpBuilder applyWindowScale(byte windowScale) {
        this.windowScale = windowScale;
        return this;
    }

    @NotNull
    TcpBuilder applySelectiveAckPermitted(boolean selectiveAckPermitted) {
        this.selectiveAckPermitted = selectiveAckPermitted;
        return this;
    }

    @NotNull
    TcpBuilder applySelectiveAcks(@NotNull List<SelectiveAck> selectiveAcks) {
        this.selectiveAcks = selectiveAcks;
        return this;
    }

    @NotNull
    TcpBuilder applyTimeStamp(@NotNull TimeStamp time) {
        this.time = time;
        return this;
    }

    @NotNull
    TcpBuilder applyTcpOption(@NotNull ByteBuffer tcpOptionStream) {
        this.tcpOptionStream = tcpOptionStream;
        return this;
    }

    @NotNull
    TcpBuilder applyTcpPayload(@NotNull ByteBuffer tcpPayloadStream) {
        this.tcpPayloadStream = tcpPayloadStream;
        return this;
    }
}
