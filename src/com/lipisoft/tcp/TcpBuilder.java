package com.lipisoft.tcp;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

abstract class TcpBuilder {
    Port port;
    Number number;
    byte dataOffset;
    ControlFlags controlFlags;

    short windowSize;
    short checksum;
    short urgentPointer;

    // optional
    @NotNull Options options = new Options((short) 0, (byte) 0, false, new ArrayList<>(),
            new TimeStamp(0, 0));

    ByteBuffer tcpHeaderStream = null;
    @NotNull ByteBuffer tcpOptionStream = ByteBuffer.allocate(0);
    @NotNull ByteBuffer tcpPayloadStream = ByteBuffer.allocate(0);

    abstract Tcp build();

    @NotNull public void applyMaxSegmentSize(short maxSegmentSize) {
        options = new Options(maxSegmentSize, options.getWindowScale(), options.isSelectiveAckPermitted(),
                options.getSelectiveAcks(), options.getTimeStamp());
    }

    @NotNull public void applyWindowScale(byte windowScale) {
        options = new Options(options.getMaxSegmentSize(), windowScale, options.isSelectiveAckPermitted(),
                options.getSelectiveAcks(), options.getTimeStamp());
    }

    @NotNull public void applySelectiveAckPermitted(boolean selectiveAckPermitted) {
        options = new Options(options.getMaxSegmentSize(), options.getWindowScale(), selectiveAckPermitted,
                options.getSelectiveAcks(), options.getTimeStamp());
    }

    @NotNull public void applySelectiveAcks(@NotNull List<SelectiveAck> selectiveAcks) {
        options = new Options(options.getMaxSegmentSize(), options.getWindowScale(), options.isSelectiveAckPermitted(),
                selectiveAcks, options.getTimeStamp());
    }

    @NotNull public void applyTimeStamp(@NotNull TimeStamp time) {
        options = new Options(options.getMaxSegmentSize(), options.getWindowScale(), options.isSelectiveAckPermitted(),
                options.getSelectiveAcks(), time);
    }

//    @NotNull
//    TcpBuilder applyTcpOption(@NotNull ByteBuffer tcpOptionStream) {
//        this.tcpOptionStream = tcpOptionStream;
//        return this;
//    }

    @NotNull public void applyTcpPayload(@NotNull ByteBuffer tcpPayloadStream) {
        this.tcpPayloadStream = tcpPayloadStream;
    }
}
