package com.lipisoft;

import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        final int sourceAddress = 0x11fd45ce;
        final int destinationAddress = 0xc0a8006b;
        final short sourcePort = 80;
        final short destinationPort = (short) 0xe03a;
        final int seq = 0xdd5058f7;
        final int ack = 1803144046;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = true;
        final boolean PSH = false;
        final boolean RST = false;
        final boolean SYN = true;
        final boolean FIN = false;

        final short windowSize = 14480;
        final short urgentPointer = 0;

        final short maxSegmentSize = 1460;
        final boolean selectiveAckPermitted = true;
        final TimeStamp timeStamp = new TimeStamp(0xe41ea430, 690506430);
        final byte windowScale = 8;

        final Tcp synAckTcp = new Tcp.OutgoingTcpBuilder(sourceAddress, destinationAddress, sourcePort, destinationPort,
                seq, ack, NS, CWR, ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, urgentPointer)
                .applyMaxSegmentSize(maxSegmentSize)
                .applyWindowScale(windowScale)
                .applySelectiveAckPermitted(selectiveAckPermitted)
                .applyTimeStamp(timeStamp)
                .build();

        final ByteBuffer synAckTcpHeaderStream = synAckTcp.getTcpHeaderStream();
        final ByteBuffer synAckTcpOptionsStream = synAckTcp.getTcpOptionStream();
        final ByteBuffer synAckTcpPayloadStream = synAckTcp.getTcpPayloadStream();

        int headerSize = 0;
        if (synAckTcpHeaderStream != null) {
            headerSize = synAckTcpHeaderStream.array().length;
            synAckTcpHeaderStream.flip();
        }

        int optionsSize = 0;
        if (synAckTcpOptionsStream != null) {
            optionsSize = synAckTcpOptionsStream.limit();
            synAckTcpOptionsStream.flip();
        }

        int payloadSize = 0;
        if (synAckTcpPayloadStream != null) {
            payloadSize = synAckTcpPayloadStream.array().length;
            synAckTcpPayloadStream.flip();
        }

        final int totalSize = headerSize + optionsSize + payloadSize;
        final ByteBuffer tcpStream = ByteBuffer.allocate(totalSize);

        if (synAckTcpHeaderStream != null) {
            tcpStream.put(synAckTcpHeaderStream);
        }

        if (synAckTcpOptionsStream != null) {
            tcpStream.put(synAckTcpOptionsStream);
        }

        if (synAckTcpPayloadStream != null) {
            tcpStream.put(synAckTcpPayloadStream);
        }

        tcpStream.rewind();

        final Tcp reSynAckTcp = TcpFactory.createTCP(tcpStream);
        TestTcpFactory testSynAckStream = new TestTcpFactory(tcpStream.array(), sourcePort, destinationPort, seq, ack,
                (byte) 10, NS, CWR, ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, 0xac8f, urgentPointer);
        testSynAckStream.testResultExceptChecksum("SYN/ACK TEST");
    }
}
