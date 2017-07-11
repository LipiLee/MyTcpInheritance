package com.lipisoft;

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
        testTcpAckStream();
        testTcpDataStream();
        testSynAckStream();
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
        final int sourcePort = 80;
        final int destinationPort = 57402;
        final long sequence = 3713030391L;
        final long acknowledgement = 1803144046;
        final boolean NS = false;
        final boolean CWR = false;
        final boolean ECE = false;
        final boolean URG = false;
        final boolean ACK = true;
        final boolean PSH = false;
        final boolean RST = false;
        final boolean SYN = true;
        final boolean FIN = false;
        final int windowSize = 14480;
        final int urgentPointer = 0;

        final Tcp.OutgoingTcpBuilder builder = new Tcp.OutgoingTcpBuilder(sourceAddress, destinationAddress, sourcePort,
                destinationPort, sequence, acknowledgement, NS, CWR, ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize,
                urgentPointer);
        final long sender = 3827213360L;
        final long echoReply = 690506430;
        final TimeStamp time = new TimeStamp(sender, echoReply);
        final short windowScale = 8;

        builder.applyMaxSegmentSize(1460).applySelectiveAckPermitted(true).applyTimeStamp(time)
                .applyWindowScale(windowScale);
        final Tcp tcp = builder.build();

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

        assertEquals(ByteBuffer.wrap(synAckPacketStream), packetStream);
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

        final IpV4 synTcpIpV4Packet = IpV4Factory.createIpV4(ByteBuffer.wrap(synPacketStream));
        assertEquals(synTcpIpV4Packet.getVersion(), 4);
        assertEquals(synTcpIpV4Packet.getInternetHeaderLength(), 5);
        assertEquals(synTcpIpV4Packet.getDifferentiatedServiceCodePoint(), 0);
        assertEquals(synTcpIpV4Packet.getExplicitCongestionNotification(), 0);
        assertEquals(synTcpIpV4Packet.getTotalLength(), 64);
        assertEquals(synTcpIpV4Packet.getIdentification(), (short) 0xb160);
        assertEquals(synTcpIpV4Packet.isDontFragment(), true);
        assertEquals(synTcpIpV4Packet.isMoreFragments(), false);
        assertEquals(synTcpIpV4Packet.getFragmentOffset(), 0);
        assertEquals(synTcpIpV4Packet.getTimeToLive(), 64);
        assertEquals(synTcpIpV4Packet.getProtocol(), 6);
        assertEquals(synTcpIpV4Packet.getHeaderChecksum(), 0x7079);
        assertEquals(synTcpIpV4Packet.getSourceAddress(), 0xc0a8006b);
        assertEquals(synTcpIpV4Packet.getDestinationAddress(), 0x11fd45ce);

        final Tcp synTcpPacket = synTcpIpV4Packet.getTcp();

//        final Tcp synTcpPacket = TcpFactory.createTCP(ByteBuffer.wrap(synPacketStream));

        assertEquals(synTcpPacket.getSourcePort(), 57402);
        assertEquals(synTcpPacket.getDestinationPort(), 80);
        assertEquals(synTcpPacket.getSeq(), 1803144045);
        assertEquals(synTcpPacket.getAck(), 0);
        assertEquals(synTcpPacket.getDataOffset(), 11);
        assertEquals(synTcpPacket.getNS(), false);
        assertEquals(synTcpPacket.getCWR(), false);
        assertEquals(synTcpPacket.getECE(), false);
        assertEquals(synTcpPacket.getURG(), false);
        assertEquals(synTcpPacket.getACK(), false);
        assertEquals(synTcpPacket.getPSH(), false);
        assertEquals(synTcpPacket.getRST(), false);
        assertEquals(synTcpPacket.getSYN(), true);
        assertEquals(synTcpPacket.getFIN(), false);
        assertEquals(synTcpPacket.getWindowSize(), 65535);
        assertEquals(synTcpPacket.getChecksum(), 0x92c6);
        assertEquals(synTcpPacket.getUrgentPointer(), 0);

        assertEquals(synTcpPacket.getMaxSegmentSize(), 1460);
        assertEquals(synTcpPacket.getWindowScale(), 5);
        assertEquals(synTcpPacket.isSelectiveAckPermitted(), true);
        assertEquals(synTcpPacket.getTime().getSender(), 690506430);
        assertEquals(synTcpPacket.getTime().getEchoReply(), 0);
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
        final IpV4 ackTcpIpV4Packet = IpV4Factory.createIpV4(ByteBuffer.wrap(ackPacketStream));
        assertEquals(ackTcpIpV4Packet.getVersion(), 4);
        assertEquals(ackTcpIpV4Packet.getInternetHeaderLength(), 5);
        assertEquals(ackTcpIpV4Packet.getDifferentiatedServiceCodePoint(), 0);
        assertEquals(ackTcpIpV4Packet.getExplicitCongestionNotification(), 0);
        assertEquals(ackTcpIpV4Packet.getTotalLength(), 52);
        assertEquals(ackTcpIpV4Packet.getIdentification(), (short) 0x6fcb);
        assertEquals(ackTcpIpV4Packet.isDontFragment(), true);
        assertEquals(ackTcpIpV4Packet.isMoreFragments(), false);
        assertEquals(ackTcpIpV4Packet.getFragmentOffset(), 0);
        assertEquals(ackTcpIpV4Packet.getTimeToLive(), 64);
        assertEquals(ackTcpIpV4Packet.getProtocol(), 6);
        assertEquals(ackTcpIpV4Packet.getHeaderChecksum(), (short) 0xb21a);
        assertEquals(ackTcpIpV4Packet.getSourceAddress(), 0xc0a8006b);
        assertEquals(ackTcpIpV4Packet.getDestinationAddress(), 0x11fd45ce);

        final Tcp ackTcpPacket = ackTcpIpV4Packet.getTcp();

//        final Tcp ackTcpPacket = TcpFactory.createTCP(ByteBuffer.wrap(ackPacketStream));

        assertEquals(ackTcpPacket.getSourcePort(), 57402);
        assertEquals(ackTcpPacket.getDestinationPort(), 80);
        assertEquals(ackTcpPacket.getSeq(), 1803144046);
        assertEquals(ackTcpPacket.getAck(), 3713030392L);
        assertEquals(ackTcpPacket.getDataOffset(), 8);
        assertEquals(ackTcpPacket.getNS(), false);
        assertEquals(ackTcpPacket.getCWR(), false);
        assertEquals(ackTcpPacket.getECE(), false);
        assertEquals(ackTcpPacket.getURG(), false);
        assertEquals(ackTcpPacket.getACK(), true);
        assertEquals(ackTcpPacket.getPSH(), false);
        assertEquals(ackTcpPacket.getRST(), false);
        assertEquals(ackTcpPacket.getSYN(), false);
        assertEquals(ackTcpPacket.getFIN(), false);
        assertEquals(ackTcpPacket.getWindowSize(), 4117);
        assertEquals(ackTcpPacket.getChecksum(), 0x02ed);
        assertEquals(ackTcpPacket.getUrgentPointer(), 0);

        assertEquals(ackTcpPacket.getTime().getSender(), 690506665);
        assertEquals(ackTcpPacket.getTime().getEchoReply(), 3827213360L);
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

        final IpV4 dataTcpIpV4Packet = IpV4Factory.createIpV4(ByteBuffer.wrap(dataPacketStream));
        assertEquals(dataTcpIpV4Packet.getVersion(), 4);
        assertEquals(dataTcpIpV4Packet.getInternetHeaderLength(), 5);
        assertEquals(dataTcpIpV4Packet.getDifferentiatedServiceCodePoint(), 0);
        assertEquals(dataTcpIpV4Packet.getExplicitCongestionNotification(), 0);
        assertEquals(dataTcpIpV4Packet.getTotalLength(), 956);
        assertEquals(dataTcpIpV4Packet.getIdentification(), (short) 0xf596);
        assertEquals(dataTcpIpV4Packet.isDontFragment(), true);
        assertEquals(dataTcpIpV4Packet.isMoreFragments(), false);
        assertEquals(dataTcpIpV4Packet.getFragmentOffset(), 0);
        assertEquals(dataTcpIpV4Packet.getTimeToLive(), 64);
        assertEquals(dataTcpIpV4Packet.getProtocol(), 6);
        assertEquals(dataTcpIpV4Packet.getHeaderChecksum(), (short) 0x28c7);
        assertEquals(dataTcpIpV4Packet.getSourceAddress(), 0xc0a8006b);
        assertEquals(dataTcpIpV4Packet.getDestinationAddress(), 0x11fd45ce);

        final Tcp dataTcpPacket = dataTcpIpV4Packet.getTcp();


//        final Tcp dataTcpPacket = TcpFactory.createTCP(ByteBuffer.wrap(dataPacketStream));

        assertEquals(dataTcpPacket.getSourcePort(), 57402);
        assertEquals(dataTcpPacket.getDestinationPort(), 80);
        assertEquals(dataTcpPacket.getSeq(), 1803144046);
        assertEquals(dataTcpPacket.getAck(), 3713030392L);
        assertEquals(dataTcpPacket.getDataOffset(), 8);
        assertEquals(dataTcpPacket.getNS(), false);
        assertEquals(dataTcpPacket.getCWR(), false);
        assertEquals(dataTcpPacket.getECE(), false);
        assertEquals(dataTcpPacket.getURG(), false);
        assertEquals(dataTcpPacket.getACK(), true);
        assertEquals(dataTcpPacket.getPSH(), true);
        assertEquals(dataTcpPacket.getRST(), false);
        assertEquals(dataTcpPacket.getSYN(), false);
        assertEquals(dataTcpPacket.getFIN(), false);
        assertEquals(dataTcpPacket.getWindowSize(), 4117);
        assertEquals(dataTcpPacket.getChecksum(), 0x769d);
        assertEquals(dataTcpPacket.getUrgentPointer(), 0);

        assertEquals(dataTcpPacket.getTime().getSender(), 690506667);
        assertEquals(dataTcpPacket.getTime().getEchoReply(), 3827213360L);
        assertArrayEquals(dataTcpPacket.getTcpPayloadStream().array(), dataStream);
    }
}