package com.lipisoft;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TcpFactoryTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTCP() throws Exception {
        testTcpSynStream();
        testSynAckStream();
        testTcpAckStream();
//        testTcpDataStream();
    }

    private void testSynAckStream() {
        final byte[] synAckPacketStream = new byte[] {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x00,
                (byte)0x2e, (byte)0x06, (byte)0x33, (byte)0xde, (byte)0x11, (byte)0xfd, (byte)0x45, (byte)0xce,
                (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b, (byte)0x00, (byte)0x50, (byte)0xe0, (byte)0x3a,
                (byte)0xdd, (byte)0x50, (byte)0x58, (byte)0xf7, (byte)0x6b, (byte)0x79, (byte)0xcb, (byte)0x6e,
                (byte)0xa0, (byte)0x12, (byte)0x38, (byte)0x90, (byte)0xac, (byte)0x8f, (byte)0x00, (byte)0x00,
                (byte)0x02, (byte)0x04, (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a,
                (byte)0xe4, (byte)0x1e, (byte)0xa4, (byte)0x30, (byte)0x29, (byte)0x28, (byte)0x4a, (byte)0xbe,
                (byte)0x01, (byte)0x03, (byte)0x03, (byte)0x08
        };

        final int sourceAddress = 0x11fd45ce;
        final int destinationAddress = 0xc0a8006b;
        final short sourcePort = 80;
        final short destinationPort = (short) 0xe03a;
        final int sequence = 0xdd5058f7;
        final int acknowledgement = 1803144046;
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

        final Tcp.OutgoingTcpBuilder builder = new Tcp.OutgoingTcpBuilder(sourceAddress, destinationAddress, sourcePort,
                destinationPort, sequence, acknowledgement, NS, CWR, ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize,
                urgentPointer);

        final short maxSegmentSize = 1460;
        final boolean selectiveAckPermitted = true;
        final int sender = 0xe41ea430;
        final int echoReply = 690506430;
        final TimeStamp time = new TimeStamp(sender, echoReply);
        final byte windowScale = 8;

        final Tcp tcp = builder.applyMaxSegmentSize(maxSegmentSize).applySelectiveAckPermitted(selectiveAckPermitted)
                .applyTimeStamp(time).applyWindowScale(windowScale).build();

        final byte version = 4;
        final byte internetHeaderLength = 5;
        final byte differentiatedServiceCodePoint = 0;
        final byte explicitCongestionNotification = 0;

//        final short totalLength = (short) (IpV4.IPV4_HEADER_SIZE + tcp.getTcpHeaderStream().array().length +
//                tcp.getTcpOptionStream().array().length + tcp.getTcpPayloadStream().array().length);
        final short totalLength = 60;
        final short identification = 0;
        final boolean doNotFragment = true;
        final boolean moreFragment = false;
        final short fragmentOffset = 0;
        final byte timeToLive = 46;
        final byte protocol = 6;

        final IpV4 ip = new IpV4.OutGoingIpV4Builder(version, internetHeaderLength,
                differentiatedServiceCodePoint, explicitCongestionNotification, totalLength, identification,
                doNotFragment, moreFragment, fragmentOffset, timeToLive, protocol, sourceAddress, destinationAddress,
                tcp).build();
        final ByteBuffer packetStream = ByteBuffer.allocate(60);
        packetStream.put(ip.getStream()).put(tcp.getTcpHeaderStream()).put(tcp.getTcpOptionStream()).put(tcp.getTcpPayloadStream());
        packetStream.rewind();

        testStream(packetStream, version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification,
                doNotFragment, moreFragment, fragmentOffset, timeToLive, protocol, ip.getHeaderChecksum(),
                sourceAddress, destinationAddress, sourcePort, destinationPort,
                sequence, acknowledgement, tcp.getDataOffset(), NS, CWR, ECE, URG,
                ACK, PSH, RST, SYN, FIN, windowSize, tcp.getChecksum(), urgentPointer,
                maxSegmentSize, windowScale, selectiveAckPermitted, sender, echoReply, ByteBuffer.allocate(0));

    }

    private void testStream(@NotNull ByteBuffer stream, byte version, byte internetHeaderLength,
                            byte differentiatedServiceCodePoint, byte explicitCongestionNotification, short totalLength,
                            short identification, boolean doNotFragment, boolean moreFragment, short fragmentOffset,
                            byte timeToLive, byte protocol, short headerChecksum, int sourceAddress,
                            int destinationAddress, short sourcePort, short destinationPort, int sequenceNumber,
                            int acknowledgeNumber, byte dataOffset, boolean NS, boolean CWR, boolean ECE, boolean URG,
                            boolean ACK, boolean PSH, boolean RST, boolean SYN, boolean FIN, short windowSize,
                            short checksum, short urgentPointer, short maximumSegmentSize, byte windowScale,
                            boolean selectiveAcknowledgePermitted, int senderTime, int echoTime, @NotNull ByteBuffer payload) {
        final IpV4 ipV4Packet = IpV4Factory.createIpV4(stream);
        assertEquals(ipV4Packet.getVersion(), version);
        assertEquals(ipV4Packet.getInternetHeaderLength(), internetHeaderLength);
        assertEquals(ipV4Packet.getDifferentiatedServiceCodePoint(), differentiatedServiceCodePoint);
        assertEquals(ipV4Packet.getExplicitCongestionNotification(), explicitCongestionNotification);
        assertEquals(ipV4Packet.getTotalLength(), totalLength);
        assertEquals(ipV4Packet.getIdentification(), identification);
        assertEquals(ipV4Packet.isDontFragment(), doNotFragment);
        assertEquals(ipV4Packet.isMoreFragments(), moreFragment);
        assertEquals(ipV4Packet.getFragmentOffset(), fragmentOffset);
        assertEquals(ipV4Packet.getTimeToLive(), timeToLive);
        assertEquals(ipV4Packet.getProtocol(), protocol);
        assertEquals(ipV4Packet.getHeaderChecksum(), headerChecksum);
        assertEquals(ipV4Packet.getSourceAddress(), sourceAddress);
        assertEquals(ipV4Packet.getDestinationAddress(), destinationAddress);

        final Tcp tcpPacket = ipV4Packet.getTcp();
        assertEquals(tcpPacket.getSourcePort(), sourcePort);
        assertEquals(tcpPacket.getDestinationPort(), destinationPort);
        assertEquals(tcpPacket.getSequenceNumber(), sequenceNumber);
        assertEquals(tcpPacket.getAcknowledgeNumber(), acknowledgeNumber);
        assertEquals(tcpPacket.getDataOffset(), dataOffset);
        assertEquals(tcpPacket.getNS(), NS);
        assertEquals(tcpPacket.getCWR(), CWR);
        assertEquals(tcpPacket.getECE(), ECE);
        assertEquals(tcpPacket.getURG(), URG);
        assertEquals(tcpPacket.getACK(), ACK);
        assertEquals(tcpPacket.getPSH(), PSH);
        assertEquals(tcpPacket.getRST(), RST);
        assertEquals(tcpPacket.getSYN(), SYN);
        assertEquals(tcpPacket.getFIN(), FIN);
        assertEquals(tcpPacket.getWindowSize(), windowSize);
        assertEquals(tcpPacket.getChecksum(), checksum);
        assertEquals(tcpPacket.getUrgentPointer(), urgentPointer);

        assertEquals(tcpPacket.getMaxSegmentSize(), maximumSegmentSize);
        assertEquals(tcpPacket.getWindowScale(), windowScale);
        assertEquals(tcpPacket.isSelectiveAckPermitted(), selectiveAcknowledgePermitted);
        assertEquals(tcpPacket.getTime().getSender(), senderTime);
        assertEquals(tcpPacket.getTime().getEchoReply(), echoTime);
        assertEquals(tcpPacket.getTcpPayloadStream(), payload);
    }

    private void testTcpSynStream() {
        final byte[] synPacketStream = new byte[] {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0xb1, (byte)0x60, (byte)0x40, (byte)0x00,
                (byte)0x40, (byte)0x06, (byte)0x70, (byte)0x79, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b,
                (byte)0x11, (byte)0xfd, (byte)0x45, (byte)0xce,

                (byte)0xe0, (byte)0x3a, (byte)0x00, (byte)0x50, (byte)0x6b, (byte)0x79, (byte)0xcb, (byte)0x6d,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xb0, (byte)0x02, (byte)0xff, (byte)0xff,
                (byte)0x92, (byte)0xc6, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x04, (byte)0x05, (byte)0xb4,
                (byte)0x01, (byte)0x03, (byte)0x03, (byte)0x05, (byte)0x01, (byte)0x01, (byte)0x08, (byte)0x0a,
                (byte)0x29, (byte)0x28, (byte)0x4a, (byte)0xbe, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x04, (byte)0x02, (byte)0x00, (byte)0x00 };


        testStream(ByteBuffer.wrap(synPacketStream), (byte) 4, (byte) 5, (byte) 0, (byte) 0, (short) 64, (short) 0xb160,
                true, false, (short) 0, (byte)64, (byte) 6, (short) 0x7079,
                0xc0a8006b, 0x11fd45ce, (short) 0xe03a, (short) 80,
                1803144045, 0, (byte) 11, false, false, false, false,
                false, false, false, true, false, (short) 0xffff, (short) 0x92c6, (short) 0,
                (short) 1460, (byte) 5, true, 690506430, 0,
                ByteBuffer.allocate(0));
    }

    private void testTcpAckStream() {
        final byte[] ackPacketStream = new byte[] {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x34, (byte)0x6f, (byte)0xcb, (byte)0x40, (byte)0x00,
                (byte)0x40, (byte)0x06, (byte)0xb2, (byte)0x1a, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b,
                (byte)0x11, (byte)0xfd, (byte)0x45, (byte)0xce,

                (byte)0xe0, (byte)0x3a, (byte)0x00, (byte)0x50, (byte)0x6b,
                (byte)0x79, (byte)0xcb, (byte)0x6e, (byte)0xdd, (byte)0x50, (byte)0x58, (byte)0xf8, (byte)0x80,
                (byte)0x10, (byte)0x10, (byte)0x15, (byte)0x02, (byte)0xed, (byte)0x00, (byte)0x00, (byte)0x01,
                (byte)0x01, (byte)0x08, (byte)0x0a, (byte)0x29, (byte)0x28, (byte)0x4b, (byte)0xa9, (byte)0xe4,
                (byte)0x1e, (byte)0xa4, (byte)0x30 };

        testStream(ByteBuffer.wrap(ackPacketStream), (byte) 4, (byte) 5, (byte) 0, (byte) 0, (short) 52, (short) 0x6fcb,
                true, false, (short) 0, (byte)64, (byte) 6, (short) 0xb21a,
                0xc0a8006b, 0x11fd45ce, (short) 0xe03a, (short) 80,
                1803144046, 0xdd5058f8, (byte) 8, false, false, false, false,
                true, false, false, false, false, (short) 4117, (short) 0x02ed, (short) 0,
                (short) -1, (byte) -1, false, 690506665, 0xe41ea430,
                ByteBuffer.allocate(0));
    }

    private void testTcpDataStream() {
        final byte[] dataPacketStream = new byte[] {
                (byte)0x45, (byte)0x00, (byte)0x03, (byte)0xbc, (byte)0xf5, (byte)0x96, (byte)0x40, (byte)0x00,
                (byte)0x40, (byte)0x06, (byte)0x28, (byte)0xc7, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b,
                (byte)0x11, (byte)0xfd, (byte)0x45, (byte)0xce,

                (byte)0xe0, (byte)0x3a, (byte)0x00, (byte)0x50, (byte)0x6b,
                (byte)0x79, (byte)0xcb, (byte)0x6e, (byte)0xdd, (byte)0x50, (byte)0x58, (byte)0xf8, (byte)0x80,
                (byte)0x18, (byte)0x10, (byte)0x15, (byte)0x76, (byte)0x9d, (byte)0x00, (byte)0x00, (byte)0x01,
                (byte)0x01, (byte)0x08, (byte)0x0a, (byte)0x29, (byte)0x28, (byte)0x4b, (byte)0xab, (byte)0xe4,
                (byte)0x1e, (byte)0xa4, (byte)0x30, (byte)0x47, (byte)0x45, (byte)0x54, (byte)0x20, (byte)0x2f,
                (byte)0x61, (byte)0x70, (byte)0x70, (byte)0x6c, (byte)0x65, (byte)0x2d, (byte)0x61, (byte)0x73,
                (byte)0x73, (byte)0x65, (byte)0x74, (byte)0x73, (byte)0x2d, (byte)0x75, (byte)0x73, (byte)0x2d,
                (byte)0x73, (byte)0x74, (byte)0x64, (byte)0x2d, (byte)0x30, (byte)0x30, (byte)0x30, (byte)0x30,
                (byte)0x30, (byte)0x31, (byte)0x2f };

        final byte[] dataStream = {(byte)0x47, (byte)0x45, (byte)0x54, (byte)0x20, (byte)0x2f,
                (byte)0x61, (byte)0x70, (byte)0x70, (byte)0x6c, (byte)0x65, (byte)0x2d, (byte)0x61, (byte)0x73,
                (byte)0x73, (byte)0x65, (byte)0x74, (byte)0x73, (byte)0x2d, (byte)0x75, (byte)0x73, (byte)0x2d,
                (byte)0x73, (byte)0x74, (byte)0x64, (byte)0x2d, (byte)0x30, (byte)0x30, (byte)0x30, (byte)0x30,
                (byte)0x30, (byte)0x31, (byte)0x2f };

        testStream(ByteBuffer.wrap(dataPacketStream), (byte) 4, (byte) 5, (byte) 0, (byte) 0, (short) 956, (short) 0xf596,
                true, false, (short) 0, (byte)64, (byte) 6, (short) 0x28c7,
                0xc0a8006b, 0x11fd45ce, (short) 0xe03a, (short) 80,
                1803144046, 0xdd5058f8, (byte) 8, false, false, false, false,
                true, true, false, false, false, (short) 4117, (short) 0x769d, (short) 0,
                (short) -1, (byte) -1, false, 690506667, 0xe41ea430,
                ByteBuffer.wrap(dataStream));
    }
}