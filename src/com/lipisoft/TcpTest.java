package com.lipisoft;

import com.lipisoft.tcp.SelectiveAck;
import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.List;

class TcpTest {
    private final short sourcePort;
    private final short destinationPort;
    private final int sequenceNumber;
    private final int acknowledgeNumber;
    private final byte dataOffset;
    private final boolean NS;
    private final boolean CWR;
    private final boolean ECE;
    private final boolean URG;
    private final boolean ACK;
    private final boolean PSH;
    private final boolean RST;
    private final boolean SYN;
    private final boolean FIN;
    private final short windowSize;
    private final short checksum;
    private final short urgentPointer;
    private final short maximumSegmentSize;
    private final byte windowScale;
    private final boolean selectiveAcknowledgePermitted;
    private final int senderTime;
    private final int echoTime;
    @NotNull private final List<SelectiveAck> selectiveAcks;
    @NotNull private final ByteBuffer payload;

    TcpTest(short sourcePort, short destinationPort, int sequenceNumber, int acknowledgeNumber, byte dataOffset,
                   boolean NS, boolean CWR, boolean ECE, boolean URG, boolean ACK, boolean PSH, boolean RST, boolean SYN,
                   boolean FIN, short windowSize, short checksum, short urgentPointer, short maximumSegmentSize,
                   byte windowScale, boolean selectiveAcknowledgePermitted, int senderTime, int echoTime,
                   @NotNull List<SelectiveAck> selectiveAcks, @NotNull ByteBuffer payload) {
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.sequenceNumber = sequenceNumber;
        this.acknowledgeNumber = acknowledgeNumber;
        this.dataOffset = dataOffset;
        this.NS = NS;
        this.CWR = CWR;
        this.ECE = ECE;
        this.URG = URG;
        this.ACK = ACK;
        this.PSH = PSH;
        this.RST = RST;
        this.SYN = SYN;
        this.FIN = FIN;
        this.windowSize = windowSize;
        this.checksum = checksum;
        this.urgentPointer = urgentPointer;
        this.maximumSegmentSize = maximumSegmentSize;
        this.windowScale = windowScale;
        this.selectiveAcknowledgePermitted = selectiveAcknowledgePermitted;
        this.senderTime = senderTime;
        this.echoTime = echoTime;
        this.selectiveAcks = selectiveAcks;
        this.payload = payload;
    }

    short getSourcePort() {
        return sourcePort;
    }

    short getDestinationPort() {
        return destinationPort;
    }

    int getSequenceNumber() {
        return sequenceNumber;
    }

    int getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    byte getDataOffset() {
        return dataOffset;
    }

    boolean isNS() {
        return NS;
    }

    boolean isCWR() {
        return CWR;
    }

    boolean isECE() {
        return ECE;
    }

    boolean isURG() {
        return URG;
    }

    boolean isACK() {
        return ACK;
    }

    boolean isPSH() {
        return PSH;
    }

    boolean isRST() {
        return RST;
    }

    boolean isSYN() {
        return SYN;
    }

    boolean isFIN() {
        return FIN;
    }

    short getWindowSize() {
        return windowSize;
    }

    short getChecksum() {
        return checksum;
    }

    short getUrgentPointer() {
        return urgentPointer;
    }

    short getMaximumSegmentSize() {
        return maximumSegmentSize;
    }

    byte getWindowScale() {
        return windowScale;
    }

    boolean isSelectiveAcknowledgePermitted() {
        return selectiveAcknowledgePermitted;
    }

    int getSenderTime() {
        return senderTime;
    }

    int getEchoTime() {
        return echoTime;
    }

    List<SelectiveAck> getSelectiveAcks() {
        return selectiveAcks;
    }

    ByteBuffer getPayload() {
        return payload;
    }
}
