package com.lipisoft;

import com.lipisoft.tcp.Tcp;
import com.lipisoft.tcp.TcpFactory;
import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;

class TestTcpFactory {
    private final Tcp tcp;
    private final int sourcePort;
    private final int destinationPort;
    private final long seq;
    private final long ack;
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
    private final int windowSize;
    private final int checkSum;
    private final int urgentPointer;

    TestTcpFactory(@NotNull byte[] networkStream, int sourcePort, int destinationPort, long seq, long ack,
                          byte dataOffset, boolean NS, boolean CWR, boolean ECE, boolean URG, boolean ACK, boolean PSH,
                          boolean RST, boolean SYN, boolean FIN, int windowSize, int checkSum, int urgentPointer) {
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.seq = seq;
        this.ack = ack;
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
        this.checkSum = checkSum;
        this.urgentPointer = urgentPointer;

        tcp = TcpFactory.createTCP(ByteBuffer.wrap(networkStream));
    }

    private boolean isCorrect() {
        return isEqualSourcePort() && isEqualDestinationPort() && isEqualSequenceNumber() && isEqualAcknowledgeNumber()
                && isEqualDataOffset() && isEqualNS() && isEqualCWR() && isEqualECE() && isEqualURG() && isEqualACK() &&
                isEqualPSH() && isEqualRST() && isEqualSYN() && isEqualFIN() && isEqualWindowSize() && isEqualCheckSum()
                && isEqualUrgentPointer();
    }

    private boolean isCorrectExceptChecksum() {
        return isEqualSourcePort() && isEqualDestinationPort() && isEqualSequenceNumber() && isEqualAcknowledgeNumber()
                && isEqualDataOffset() && isEqualNS() && isEqualCWR() && isEqualECE() && isEqualURG() && isEqualACK() &&
                isEqualPSH() && isEqualRST() && isEqualSYN() && isEqualFIN() && isEqualWindowSize() &&
                isEqualUrgentPointer();
    }

    private boolean isEqualSourcePort() {
        return tcp.getPort().getSource() == sourcePort;
    }

    private boolean isEqualDestinationPort() {
        return tcp.getPort().getDestination() == destinationPort;
    }

    private boolean isEqualSequenceNumber() {
        return tcp.getNumber().getSequence() == seq;
    }

    private boolean isEqualAcknowledgeNumber() {
        return tcp.getNumber().getAcknowledgement() == ack;
    }

    private boolean isEqualDataOffset() {
        return tcp.getDataOffset() == dataOffset;
    }

    private boolean isEqualNS() {
        return tcp.getControlFlags().isNS() == NS;
    }

    private boolean isEqualCWR() {
        return tcp.getControlFlags().isCWR() == CWR;
    }

    private boolean isEqualECE() {
        return tcp.getControlFlags().isECE() == ECE;
    }

    private boolean isEqualURG() {
        return tcp.getControlFlags().isURG() == URG;
    }

    private boolean isEqualACK() {
        return tcp.getControlFlags().isACK() == ACK;
    }

    private boolean isEqualPSH() {
        return tcp.getControlFlags().isPSH() == PSH;
    }

    private boolean isEqualRST() {
        return tcp.getControlFlags().isRST() == RST;
    }

    private boolean isEqualSYN() {
        return tcp.getControlFlags().isSYN() == SYN;
    }

    private boolean isEqualFIN() {
        return tcp.getControlFlags().isFIN() == FIN;
    }

    private boolean isEqualWindowSize() {
        return tcp.getWindowSize() == windowSize;
    }

    private boolean isEqualCheckSum() {
        return tcp.getChecksum() == checkSum;
    }

    private boolean isEqualUrgentPointer() {
        return tcp.getUrgentPointer() == urgentPointer;
    }

    void testResult(@NotNull String testName) {
        if (isCorrect()) {
            System.out.println(testName + " is successful.");
        } else {
            System.out.println(testName + " is NOT successful.");
        }
    }

    void testResultExceptChecksum(@NotNull String testName) {
        if (isCorrectExceptChecksum()) {
            System.out.println(testName + " is successful.");
        } else {
            System.out.println(testName + " is NOT successful.");
        }
    }
}
