package com.lipisoft;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.nio.ByteBuffer;
import java.util.List;

public class Tcp {
    private static final int TCP_HEADER_SIZE_BYTES = 20;
    private static final int MAX_TCP_OPTIONS_SIZE_BYTES = 40;

    private final int sourcePort;
    private final int destinationPort;

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

    // The window size is enough 16 bits,
    // but their type is int, not short because Java can NOT support unsigned int
    private final short windowSize;
    private final short checksum;
    private final short urgentPointer;

    private final short maxSegmentSize;
    private final byte windowScale;
    private final boolean selectiveAckPermitted;
    private final List<SelectiveAck> selectiveAcks;
    private final TimeStamp time;

    private final ByteBuffer tcpHeaderStream;
    private final ByteBuffer tcpOptionStream;
    private final ByteBuffer tcpPayloadStream;

    static class IncomingTcpBuilder extends TcpBuilder {
        IncomingTcpBuilder(int sourcePort, int destinationPort, int seq, int ack,
                                  byte dataOffset, boolean NS, boolean CWR, boolean ECE, boolean URG,
                                  boolean ACK, boolean PSH, boolean RST, boolean SYN, boolean FIN,
                                  short windowSize, short checksum, short urgentPointer) {
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
            this.checksum = checksum;
            this.urgentPointer = urgentPointer;
        }

        @Override
        Tcp build() {
            return new Tcp(this);
        }
    }

    static class OutgoingTcpBuilder extends TcpBuilder {
        // for generating the checksum
        private final int sourceAddress;
        private final int destinationAddress;

        OutgoingTcpBuilder(int sourceAddress, int destinationAddress,
                                  short sourcePort, short destinationPort,
                                  int seq, int ack,
//                           byte dataOffset,
                                  boolean NS, boolean CWR, boolean ECE,
                                  boolean URG,
                                  boolean ACK, boolean PSH, boolean RST, boolean SYN,
                                  boolean FIN, short windowSize,
//                           short checksum,
                                  short urgentPointer) {
            this.sourceAddress = sourceAddress;
            this.destinationAddress = destinationAddress;

            this.sourcePort = sourcePort;
            this.destinationPort = destinationPort;
            this.seq = seq;
            this.ack = ack;
//            this.dataOffset = dataOffset;
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
//            this.checksum = checksum;
            this.urgentPointer = urgentPointer;
        }

        private void createTcpHeaderStream() {
            tcpHeaderStream = ByteBuffer.allocate(TCP_HEADER_SIZE_BYTES);
            tcpHeaderStream.putShort((short) sourcePort);
            tcpHeaderStream.putShort((short) destinationPort);

            tcpHeaderStream.putInt(seq);
            tcpHeaderStream.putInt(ack);

            // temporarily set dataOffset to 0 for occupying its space
            // tcpHeaderStream.put(dataOffset);
            tcpHeaderStream.put((byte) 0);

            final byte controlBits = (byte) ((CWR ? 1 : 0) << 7 |
                    (ECE ? 1 : 0) << 6 |
                    (URG ? 1 : 0) << 5 |
                    (ACK ? 1 : 0) << 4 |
                    (PSH ? 1 : 0) << 3 |
                    (RST ? 1 : 0) << 2 |
                    (SYN ? 1 : 0) << 1 |
                    (FIN ? 1 : 0));
            tcpHeaderStream.put(controlBits);

            tcpHeaderStream.putShort(windowSize);
            tcpHeaderStream.putShort((short) 0); // checksum

            // Don't use urgent pointer
            tcpHeaderStream.putShort((short) 0); // urgent pointer

            tcpHeaderStream.rewind();
        }

        private void createTcpOptionStream() {
            // maximum size can be 40 bytes for options
            ByteBuffer buffer = ByteBuffer.allocate(MAX_TCP_OPTIONS_SIZE_BYTES);
            if (isAssignedMaxSegmentSize()) {
                writeMaxSegementSize(buffer);
            }

            if (isAssignedWindowScale()) {
                writeWindowScale(buffer);
            }

            if (isAssignedSelectiveAckPermitted()) {
                writeSelectiveAckPermitted(buffer);
            }

            if (isAssignedSelectiveAcks()) {
                writeSelectiveAcks(buffer);
            }

            if (isAssignedTimeStamp()) {
                writeTimeStamp(buffer);
            }

            // 32-bit boundaries for better performance
            while (buffer.position() % 4 != 0) {
                // Add NOP(No operation)
                buffer.put((byte) 1);
            }

            int size = buffer.position();
            tcpOptionStream = ByteBuffer.allocate(size);
            buffer.flip();
            tcpOptionStream.put(buffer);
        }

        private void setDataOffset() {
            final int optionsLength = tcpOptionStream.capacity();
            dataOffset = (byte) ((TCP_HEADER_SIZE_BYTES + optionsLength) / 4);
            final byte dataOffsetAndNs = (byte) (dataOffset << 4 + (NS ? 1 : 0));
            tcpHeaderStream.put(12, dataOffsetAndNs);

            // set limit and rewind
            tcpOptionStream.flip();

        }

        private void setChecksum() {
            final ByteBuffer psuedoIpHeader = createPsuedoIpHeader();
            int sum = 0;

            while (psuedoIpHeader.hasRemaining()) {
                sum += psuedoIpHeader.getShort();
            }

            while (tcpHeaderStream.hasRemaining()) {
                sum += tcpHeaderStream.getShort();
            }
            tcpHeaderStream.rewind();

            while (tcpOptionStream.hasRemaining()) {
                sum += tcpOptionStream.getShort();
            }
            tcpOptionStream.rewind();

            while (tcpPayloadStream.hasRemaining()) {
                sum += tcpPayloadStream.getShort();
            }
            tcpPayloadStream.rewind();


            //carry over one's complement
            while((sum >> 16) > 0) {
                sum = (sum & 0xffff) + (sum >> 16);
            }
            //flip the bit to get one's complement
            sum = ~sum;

            checksum = (short) sum;
            tcpHeaderStream.putShort(16, checksum);
        }

        private ByteBuffer createPsuedoIpHeader() {
            final ByteBuffer header = ByteBuffer.allocate(12);
            header.putInt(sourceAddress);
            header.putInt(destinationAddress);
            header.put((byte) 0);
            header.put((byte) 6); // TCP
            header.putShort((short) dataOffset);

            header.rewind();

            return header;
        }

        private void writeTimeStamp(@NotNull ByteBuffer stream) {
            stream.put((byte) 8);
            stream.put((byte) 10);
            stream.putInt(time.getSender());
            stream.putInt(time.getEchoReply());
        }

        private void writeSelectiveAcks(@NotNull ByteBuffer stream) {
            stream.put((byte) 5);
            stream.put((byte) selectiveAcks.size());

            for (SelectiveAck selectiveAck: selectiveAcks) {
                stream.putInt(selectiveAck.getBegin());
                stream.putInt(selectiveAck.getEnd());
            }
        }

        private void writeSelectiveAckPermitted(@NotNull ByteBuffer stream) {
            stream.put((byte) 4);
            stream.put((byte) 2);
        }

        private void writeWindowScale(@NotNull ByteBuffer stream) {
            stream.put((byte) 3);
            stream.put((byte) 3);
            stream.put(windowScale);
        }

        private void writeMaxSegementSize(@NotNull ByteBuffer stream) {
            stream.put((byte) 2);
            stream.put((byte) 4);
            stream.putShort(maxSegmentSize);
        }

        private boolean isAssignedMaxSegmentSize() {
            return maxSegmentSize != 0;
        }

        private boolean isAssignedWindowScale() {
            return windowScale != 0;
        }

        private boolean isAssignedSelectiveAckPermitted() {
            return selectiveAckPermitted;
        }

        private boolean isAssignedSelectiveAcks() {
            return selectiveAcks.size() != 0;
        }

        private boolean isAssignedTimeStamp() {
            return time.getSender() != 0 && time.getEchoReply() != 0;
        }

        Tcp build() {
            createTcpHeaderStream();
            createTcpOptionStream();
            setDataOffset();
            setChecksum();

            return new Tcp(this);
        }
    }

    Tcp(@NotNull TcpBuilder builder) {
        this.sourcePort = builder.sourcePort;
        this.destinationPort = builder.destinationPort;
        this.sequenceNumber = builder.seq;
        this.acknowledgeNumber = builder.ack;
        this.dataOffset = builder.dataOffset;
        this.NS = builder.NS;
        this.CWR = builder.CWR;
        this.ECE = builder.ECE;
        this.URG = builder.URG;
        this.ACK = builder.ACK;
        this.PSH = builder.PSH;
        this.RST = builder.RST;
        this.SYN = builder.SYN;
        this.FIN = builder.FIN;
        this.windowSize = builder.windowSize;
        this.checksum = builder.checksum;
        this.urgentPointer = builder.urgentPointer;
        this.maxSegmentSize = builder.maxSegmentSize;
        this.windowScale = builder.windowScale;
        this.selectiveAckPermitted = builder.selectiveAckPermitted;
        this.selectiveAcks = builder.selectiveAcks;
        this.time = builder.time;
        this.tcpHeaderStream = builder.tcpHeaderStream;
        this.tcpOptionStream = builder.tcpOptionStream;
        this.tcpPayloadStream = builder.tcpPayloadStream;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(sport: ").append(sourcePort).append(", dport: ");
        sb.append(destinationPort).append(" ");
        if (SYN) sb.append("S");
        if (ACK) sb.append("A");
        if (PSH) sb.append("P");
        if (FIN) sb.append("F");
        if (RST) sb.append("R");
        // int(signed) is 32bit, so extend it to long(64bit) for printing
        long unsignedFormat = sequenceNumber & 0xFFFF;
        sb.append(", sequenceNumber: ").append(unsignedFormat);
        unsignedFormat = acknowledgeNumber & 0xFFFF;
        sb.append(", acknowledgeNumber: ").append(unsignedFormat);
        sb.append(")");
        return sb.toString();
    }

    int getSourcePort() {
        return sourcePort;
    }

    int getDestinationPort() {
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

    boolean getNS() {
        return NS;
    }

    boolean getCWR() {
        return CWR;
    }

    boolean getECE() {
        return ECE;
    }

    boolean getURG() {
        return URG;
    }

    boolean getACK() {
        return ACK;
    }

    boolean getPSH() {
        return PSH;
    }

    boolean getRST() {
        return RST;
    }

    boolean getSYN() {
        return SYN;
    }

    boolean getFIN() {
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

    short getMaxSegmentSize() {
        return maxSegmentSize;
    }

    byte getWindowScale() {
        return windowScale;
    }

    boolean isSelectiveAckPermitted() {
        return selectiveAckPermitted;
    }

    public List<SelectiveAck> getSelectiveAcks() {
        return selectiveAcks;
    }

    TimeStamp getTime() {
        return time;
    }

    @NotNull
    ByteBuffer getTcpHeaderStream() {
        return tcpHeaderStream;
    }

    @Nullable
    ByteBuffer getTcpOptionStream() {
        return tcpOptionStream;
    }

    @Nullable
    ByteBuffer getTcpPayloadStream() {
        return tcpPayloadStream;
    }

//    public  boolean checkSynOnly() {
//        return SYN && !ACK && !FIN && !PSH && !RST;
//    }
}