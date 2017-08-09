package com.lipisoft;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
        testTcpDataStream();
        testSelectiveAckStream();

    }

    private void testSelectiveAckStream() {
        byte[] selectiveAckStream = new byte[] {
                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0xc1, (byte)0x0a, (byte)0x40, (byte)0x00,
                (byte)0x40, (byte)0x06, (byte)0x25, (byte)0xc0, (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b,
                (byte)0x17, (byte)0x21, (byte)0x7b, (byte)0xb9, (byte)0xe0, (byte)0x37, (byte)0x01, (byte)0xbb,
                (byte)0x3d, (byte)0x73, (byte)0x60, (byte)0x23, (byte)0xe1, (byte)0x46, (byte)0x65, (byte)0xb8,
                (byte)0xb0, (byte)0x10, (byte)0x10, (byte)0x00, (byte)0x92, (byte)0x36, (byte)0x00, (byte)0x00,
                (byte)0x01, (byte)0x01, (byte)0x08, (byte)0x0a, (byte)0x29, (byte)0x28, (byte)0x46, (byte)0xa2,
                (byte)0x06, (byte)0x1c, (byte)0x81, (byte)0x2b, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x0a,
                (byte)0xe1, (byte)0x46, (byte)0x64, (byte)0xa1, (byte)0xe1, (byte)0x46, (byte)0x65, (byte)0xb8
        };

        final byte version = 4;
        final byte internetHeaderLength = 5;
        final byte differentiatedServiceCodePoint = 0;
        final byte explicitCongestionNotification = 0;
        final short totalLength = 64;
        final short identification = (short) 0xc10a;
        final boolean doNotFragment = true;
        final boolean moreFragment = false;
        final short fragmentOffset = 0;
        final byte timeToLive = 64;
        final byte protocol = 6;
        final short headerChecksum = 0x25c0;
        final int sourceAddress = 0xc0a8006b;
        final int destinationAddress = 0x17217bb9;

        final IpTest ip = new IpTest(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragment, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress);

        final short sourcePort = (short) 57399;
        final short destinationPort = 443;
        final int sequence = 1030971427;
        final int acknowledgement = 0xe14665b8;
        final byte dataOffset = 11;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = true;
        final boolean PSH = false;
        final boolean RST = false;
        final boolean SYN = false;
        final boolean FIN = false;
        final short windowSize = 4096;
        final short checksum = (short) 0x9236;
        final short urgentPointer = 0;

        final short maxSegmentSize = 0;
        final byte windowScale = 0;
        final boolean selectiveAckPermitted = false;
        final int sender = 0x292846a2;
        final int echoReply = 0x061c812b;
        final List<SelectiveAck> selectiveAcks = new ArrayList<>();
        final SelectiveAck selectiveAck = new SelectiveAck(0xe14664a1, 0xe14665b8);
        selectiveAcks.add(selectiveAck);
        final ByteBuffer payload = ByteBuffer.allocate(0);

        final TcpTest tcp = new TcpTest(sourcePort, destinationPort, sequence, acknowledgement, dataOffset, NS, CWR,
                ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer, maxSegmentSize, windowScale,
                selectiveAckPermitted, sender, echoReply, selectiveAcks, payload);


        testStream(ByteBuffer.wrap(selectiveAckStream), ip, tcp);
    }

    private void testSynAckStream() {
//        final byte[] synAckPacketStream = new byte[] {
//                (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x00,
//                (byte)0x2e, (byte)0x06, (byte)0x33, (byte)0xde, (byte)0x11, (byte)0xfd, (byte)0x45, (byte)0xce,
//                (byte)0xc0, (byte)0xa8, (byte)0x00, (byte)0x6b, (byte)0x00, (byte)0x50, (byte)0xe0, (byte)0x3a,
//                (byte)0xdd, (byte)0x50, (byte)0x58, (byte)0xf7, (byte)0x6b, (byte)0x79, (byte)0xcb, (byte)0x6e,
//                (byte)0xa0, (byte)0x12, (byte)0x38, (byte)0x90, (byte)0xac, (byte)0x8f, (byte)0x00, (byte)0x00,
//                (byte)0x02, (byte)0x04, (byte)0x05, (byte)0xb4, (byte)0x04, (byte)0x02, (byte)0x08, (byte)0x0a,
//                (byte)0xe4, (byte)0x1e, (byte)0xa4, (byte)0x30, (byte)0x29, (byte)0x28, (byte)0x4a, (byte)0xbe,
//                (byte)0x01, (byte)0x03, (byte)0x03, (byte)0x08
//        };

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

        final short headerChecksum = ip.getHeaderChecksum();

        final List<SelectiveAck> selectiveAcks = new ArrayList<>();

        final IpTest ipTest = new IpTest(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragment, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress);

        final byte dataOffset = tcp.getDataOffset();
        final short checksum = tcp.getChecksum();
        final TcpTest tcpTest = new TcpTest(sourcePort, destinationPort, sequence, acknowledgement, dataOffset, NS, CWR,
                ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer, maxSegmentSize, windowScale,
                selectiveAckPermitted, sender, echoReply, selectiveAcks, ByteBuffer.allocate(0));

        testStream(packetStream, ipTest, tcpTest);

    }

    private void testStream(@NotNull ByteBuffer stream, @NotNull IpTest ip, @NotNull TcpTest tcp) {
        final IpV4 ipV4Packet = IpV4Factory.createIpV4(stream);

        assertEquals(ipV4Packet.getVersion(), ip.getVersion());
        assertEquals(ipV4Packet.getInternetHeaderLength(), ip.getInternetHeaderLength());
        assertEquals(ipV4Packet.getDifferentiatedServiceCodePoint(), ip.getDifferentiatedServiceCodePoint());
        assertEquals(ipV4Packet.getExplicitCongestionNotification(), ip.getExplicitCongestionNotification());
        assertEquals(ipV4Packet.getTotalLength(), ip.getTotalLength());
        assertEquals(ipV4Packet.getIdentification(), ip.getIdentification());
        assertEquals(ipV4Packet.isDontFragment(), ip.isDoNotFragment());
        assertEquals(ipV4Packet.isMoreFragments(), ip.isMoreFragment());
        assertEquals(ipV4Packet.getFragmentOffset(), ip.getFragmentOffset());
        assertEquals(ipV4Packet.getTimeToLive(), ip.getTimeToLive());
        assertEquals(ipV4Packet.getProtocol(), ip.getProtocol());
        assertEquals(ipV4Packet.getHeaderChecksum(), ip.getHeaderChecksum());
        assertEquals(ipV4Packet.getSourceAddress(), ip.getSourceAddress());
        assertEquals(ipV4Packet.getDestinationAddress(), ip.getDestinationAddress());

        final Tcp tcpPacket = ipV4Packet.getTcp();
        assertEquals(tcpPacket.getSourcePort(), tcp.getSourcePort());
        assertEquals(tcpPacket.getDestinationPort(), tcp.getDestinationPort());
        assertEquals(tcpPacket.getSequenceNumber(), tcp.getSequenceNumber());
        assertEquals(tcpPacket.getAcknowledgeNumber(), tcp.getAcknowledgeNumber());
        assertEquals(tcpPacket.getDataOffset(), tcp.getDataOffset());
        assertEquals(tcpPacket.getNS(), tcp.isNS());
        assertEquals(tcpPacket.getCWR(), tcp.isCWR());
        assertEquals(tcpPacket.getECE(), tcp.isECE());
        assertEquals(tcpPacket.getURG(), tcp.isURG());
        assertEquals(tcpPacket.getACK(), tcp.isACK());
        assertEquals(tcpPacket.getPSH(), tcp.isPSH());
        assertEquals(tcpPacket.getRST(), tcp.isRST());
        assertEquals(tcpPacket.getSYN(), tcp.isSYN());
        assertEquals(tcpPacket.getFIN(), tcp.isFIN());
        assertEquals(tcpPacket.getWindowSize(), tcp.getWindowSize());
        assertEquals(tcpPacket.getChecksum(), tcp.getChecksum());
        assertEquals(tcpPacket.getUrgentPointer(), tcp.getUrgentPointer());

        assertEquals(tcpPacket.getMaxSegmentSize(), tcp.getMaximumSegmentSize());
        assertEquals(tcpPacket.getWindowScale(), tcp.getWindowScale());
        assertEquals(tcpPacket.isSelectiveAckPermitted(), tcp.isSelectiveAcknowledgePermitted());
        assertEquals(tcpPacket.getTime().getSender(), tcp.getSenderTime());
        assertEquals(tcpPacket.getTime().getEchoReply(), tcp.getEchoTime());
//        assertThat(tcpPacket.getSelectiveAcks(), tcp.getSelectiveAcks());
        assertEqualsList(tcpPacket.getSelectiveAcks(), tcp.getSelectiveAcks());
        assertEquals(tcpPacket.getTcpPayloadStream(), tcp.getPayload());
    }

    private void assertEqualsList(@NotNull List<SelectiveAck> expected, @NotNull List<SelectiveAck> actual) {
here:   for (SelectiveAck expectedOne : expected) {
            final int begin = expectedOne.getBegin();
            final int end = expectedOne.getEnd();

            for (SelectiveAck actualOne : actual) {
                if (begin == actualOne.getBegin() && end == actualOne.getEnd()) {
                    continue here;
                }
            }
            assertEquals(expected, actual);
        }
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

        final byte version = 4;
        final byte internetHeaderLength = 5;
        final byte differentiatedServiceCodePoint = 0;
        final byte explicitCongestionNotification = 0;
        final short totalLength = 64;
        final short identification = (short) 0xb160;
        final boolean doNotFragment = true;
        final boolean moreFragment = false;
        final short fragmentOffset = 0;
        final byte timeToLive = 64;
        final byte protocol = 6;
        final short headerChecksum = 0x7079;
        final int sourceAddress = 0xc0a8006b;
        final int destinationAddress = 0x11fd45ce;

        final IpTest ip = new IpTest(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragment, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress);

        final short sourcePort = (short) 0xe03a;
        final short destinationPort = 80;
        final int sequence = 1803144045;
        final int acknowledgement = 0;
        final byte dataOffset = 11;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = false;
        final boolean PSH = false;
        final boolean RST = false;
        final boolean SYN = true;
        final boolean FIN = false;
        final short windowSize = (short) 0xffff;
        final short checksum = (short) 0x92c6;
        final short urgentPointer = 0;

        final short maxSegmentSize = 1460;
        final byte windowScale = 5;
        final boolean selectiveAckPermitted = true;
        final int sender = 690506430;
        final int echoReply = 0;
        final List<SelectiveAck> selectiveAcks = new ArrayList<>();
        final ByteBuffer payload = ByteBuffer.allocate(0);

        final TcpTest tcp = new TcpTest(sourcePort, destinationPort, sequence, acknowledgement, dataOffset, NS, CWR,
                ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer, maxSegmentSize, windowScale,
                selectiveAckPermitted, sender, echoReply, selectiveAcks, payload);

        testStream(ByteBuffer.wrap(synPacketStream), ip, tcp);
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

        final byte version = 4;
        final byte internetHeaderLength = 5;
        final byte differentiatedServiceCodePoint = 0;
        final byte explicitCongestionNotification = 0;
        final short totalLength = 52;
        final short identification = (short) 0x6fcb;
        final boolean doNotFragment = true;
        final boolean moreFragment = false;
        final short fragmentOffset = 0;
        final byte timeToLive = 64;
        final byte protocol = 6;
        final short headerChecksum = (short) 0xb21a;
        final int sourceAddress = 0xc0a8006b;
        final int destinationAddress = 0x11fd45ce;

        final IpTest ip = new IpTest(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragment, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress);

        final short sourcePort = (short) 0xe03a;
        final short destinationPort = 80;
        final int sequence = 1803144046;
        final int acknowledgement = 0xdd5058f8;
        final byte dataOffset = 8;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = true;
        final boolean PSH = false;
        final boolean RST = false;
        final boolean SYN = false;
        final boolean FIN = false;
        final short windowSize = 4117;
        final short checksum = 0x2ed;
        final short urgentPointer = 0;

        final short maxSegmentSize = 0;
        final byte windowScale = 0;
        final boolean selectiveAckPermitted = false;
        final int sender = 690506665;
        final int echoReply = 0xe41ea430;
        final List<SelectiveAck> selectiveAcks = new ArrayList<>();
        final ByteBuffer payload = ByteBuffer.allocate(0);

        final TcpTest tcp = new TcpTest(sourcePort, destinationPort, sequence, acknowledgement, dataOffset, NS, CWR,
                ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer, maxSegmentSize, windowScale,
                selectiveAckPermitted, sender, echoReply, selectiveAcks, payload);

        testStream(ByteBuffer.wrap(ackPacketStream), ip, tcp);
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

        final byte version = 4;
        final byte internetHeaderLength = 5;
        final byte differentiatedServiceCodePoint = 0;
        final byte explicitCongestionNotification = 0;
        final short totalLength = 956;
        final short identification = (short) 0xf596;
        final boolean doNotFragment = true;
        final boolean moreFragment = false;
        final short fragmentOffset = 0;
        final byte timeToLive = 64;
        final byte protocol = 6;
        final short headerChecksum = (short) 0x28c7;
        final int sourceAddress = 0xc0a8006b;
        final int destinationAddress = 0x11fd45ce;

        final IpTest ip = new IpTest(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragment, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress);

        final short sourcePort = (short) 0xe03a;
        final short destinationPort = 80;
        final int sequence = 1803144046;
        final int acknowledgement = 0xdd5058f8;
        final byte dataOffset = 8;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = true;
        final boolean PSH = true;
        final boolean RST = false;
        final boolean SYN = false;
        final boolean FIN = false;
        final short windowSize = 4117;
        final short checksum = 0x769d;
        final short urgentPointer = 0;

        final short maxSegmentSize = 0;
        final byte windowScale = 0;
        final boolean selectiveAckPermitted = false;
        final int sender = 690506667;
        final int echoReply = 0xe41ea430;
        final List<SelectiveAck> selectiveAcks = new ArrayList<>();
        final ByteBuffer payload = ByteBuffer.wrap(dataStream);

        final TcpTest tcp = new TcpTest(sourcePort, destinationPort, sequence, acknowledgement, dataOffset, NS, CWR,
                ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer, maxSegmentSize, windowScale,
                selectiveAckPermitted, sender, echoReply, selectiveAcks, payload);

        testStream(ByteBuffer.wrap(dataPacketStream), ip, tcp);
    }
}