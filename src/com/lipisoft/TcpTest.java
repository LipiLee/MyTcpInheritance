package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.List;

public class TcpTest {
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

    public TcpTest(short sourcePort, short destinationPort, int sequenceNumber, int acknowledgeNumber, byte dataOffset,
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

    public short getSourcePort() {
        return sourcePort;
    }

    public short getDestinationPort() {
        return destinationPort;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    public byte getDataOffset() {
        return dataOffset;
    }

    public boolean isNS() {
        return NS;
    }

    public boolean isCWR() {
        return CWR;
    }

    public boolean isECE() {
        return ECE;
    }

    public boolean isURG() {
        return URG;
    }

    public boolean isACK() {
        return ACK;
    }

    public boolean isPSH() {
        return PSH;
    }

    public boolean isRST() {
        return RST;
    }

    public boolean isSYN() {
        return SYN;
    }

    public boolean isFIN() {
        return FIN;
    }

    public short getWindowSize() {
        return windowSize;
    }

    public short getChecksum() {
        return checksum;
    }

    public short getUrgentPointer() {
        return urgentPointer;
    }

    public short getMaximumSegmentSize() {
        return maximumSegmentSize;
    }

    public byte getWindowScale() {
        return windowScale;
    }

    public boolean isSelectiveAcknowledgePermitted() {
        return selectiveAcknowledgePermitted;
    }

    public int getSenderTime() {
        return senderTime;
    }

    public int getEchoTime() {
        return echoTime;
    }

    public List<SelectiveAck> getSelectiveAcks() {
        return selectiveAcks;
    }

    public ByteBuffer getPayload() {
        return payload;
    }
}
