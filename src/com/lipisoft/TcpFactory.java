package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class TcpFactory {
    private static final int END_OF_OPTIONS_LIST = 0;
    private static final int NO_OPERATION = 1;
    private static final int MAX_SEGMENT_SIZE = 2;
    private static final int WINDOW_SCALE = 3;
    private static final int SELECTIVE_ACK_PERMITTED = 4;
    private static final int SELECTIVE_ACK = 5;
    private static final int TIME_STAMP = 8;

    @NotNull
    static Tcp createTCP(@NotNull ByteBuffer packet) throws RuntimeException {
        final short sourcePort = packet.getShort();
        final short destinationPort = packet.getShort();
        final int seq = packet.getInt();
        final int ack = packet.getInt();
        final byte dataOffsetAndNs = packet.get();

        final byte dataOffset = (byte) (((dataOffsetAndNs & 0xF0) >>> 4));
        if (dataOffset < 5 && dataOffset > 15) {
            throw new RuntimeException("DataOffset is invalid in TCP. Packet is corrupted");
        }

        final boolean NS = (dataOffsetAndNs & 0x01) == 1;
        final byte controlBits = packet.get();
        final boolean CWR = ((controlBits >> 7) & 1) == 1;
        final boolean ECE = ((controlBits >> 6) & 1) == 1;
        final boolean URG = ((controlBits >> 5) & 1) == 1;
        final boolean ACK = ((controlBits >> 4) & 1) == 1;
        final boolean PSH = ((controlBits >> 3) & 1) == 1;
        final boolean RST = ((controlBits >> 2) & 1) == 1;
        final boolean SYN = ((controlBits >> 1) & 1) == 1;
        final boolean FIN = (controlBits & 1) == 1;
        final short windowSize = packet.getShort();
        final short checksum = packet.getShort();
        final short urgentPointer = packet.getShort();

        final Tcp.IncomingTcpBuilder builder = new Tcp.IncomingTcpBuilder(sourcePort, destinationPort, seq, ack,
                dataOffset, NS, CWR, ECE, URG, ACK, PSH, RST, SYN, FIN, windowSize, checksum, urgentPointer);

        if (dataOffset > 5) {
            handleTcpOptions(builder, packet, dataOffset * 4 - 20);
        }

        if (packet.hasRemaining()) {
            builder.applyTcpPayload(obtainTcpPayload(packet));
        }

        return builder.build();
    }

/*
    private static void handleTcpOptions(@NotNull Tcp.IncomingTcpBuilder builder, @NotNull ByteBuffer packet) {
        byte optionKind;

        do {
            optionKind = PacketUtility.get8BitsToByte(packet);
            // NOP(No operation), Padding
            if (optionKind == END_OF_OPTIONS_LIST || optionKind == NO_OPERATION) {
                continue;
            }

            final byte size = PacketUtility.get8BitsToByte(packet);
            switch (optionKind) {
                case MAX_SEGMENT_SIZE:
                    builder.applyMaxSegmentSize(PacketUtility.get16BitsToInt(packet));
                    break;
                case WINDOW_SCALE:
                    builder.applyWindowScale(PacketUtility.get8BitsToShort(packet));
                    break;
                case SELECTIVE_ACK_PERMITTED:
                    builder.applySelectiveAckPermitted(true);
                    break;
                case SELECTIVE_ACK:
                    builder.applySelectiveAcks(obtainSelectiveAcks(packet, size));
                    break;
                case TIME_STAMP:
                    builder.applyTimeStamp(obtainTimeStamp(packet));
                    break;
                default:
                    skipRemainingOptions(packet, size);
                    break;
            }
        } while (optionKind != 0);

    }
*/

    private static void handleTcpOptions(@NotNull Tcp.IncomingTcpBuilder builder, @NotNull ByteBuffer packet, int optionsSize) {
        int index = 0;

        while (index < optionsSize) {
            final byte optionKind = packet.get();
            index++;

            if (optionKind == END_OF_OPTIONS_LIST || optionKind == NO_OPERATION) {
                continue;
            }

            final byte size = packet.get();
            index++;

            switch (optionKind) {
                case MAX_SEGMENT_SIZE:
                    builder.applyMaxSegmentSize(packet.getShort());
                    index += 2;
                    break;
                case WINDOW_SCALE:
                    builder.applyWindowScale(packet.get());
                    index++;
                    break;
                case SELECTIVE_ACK_PERMITTED:
                    builder.applySelectiveAckPermitted(true);
                    break;
                case SELECTIVE_ACK:
                    builder.applySelectiveAcks(obtainSelectiveAcks(packet, size));
                    index = index + size - 2;
                    break;
                case TIME_STAMP:
                    builder.applyTimeStamp(obtainTimeStamp(packet));
                    index += 8;
                    break;
                default:
                    skipRemainingOptions(packet, size);
                    index = index + size - 2;
                    break;
            }

        }
    }

    private static void skipRemainingOptions(@NotNull ByteBuffer packet, int size) {
        for (int i = 2; i < size; i++) {
            packet.get();
        }
    }

    private static TimeStamp obtainTimeStamp(@NotNull ByteBuffer packet) {
        final int sender = packet.getInt();
        final int echoReplyTo = packet.getInt();

        return new TimeStamp(sender, echoReplyTo);

    }

    private static List<SelectiveAck> obtainSelectiveAcks(@NotNull ByteBuffer packet, int size) {
        final List<SelectiveAck> selectiveAcks = new ArrayList<>();

        for (byte i = 2; i < size; i += 8) {
            final int begin = packet.getInt();
            final int end = packet.getInt();

            final SelectiveAck selectiveAck = new SelectiveAck(begin, end);
            selectiveAcks.add(selectiveAck);
        }

        return selectiveAcks;

    }

    private static ByteBuffer obtainTcpPayload(@NotNull ByteBuffer packet) {
        final ByteBuffer tcpPayload = ByteBuffer.allocate(packet.remaining());
        tcpPayload.put(packet);
        tcpPayload.rewind();

        return tcpPayload;
    }
}