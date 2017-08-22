package com.lipisoft.tcp;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.nio.ByteBuffer;

public class Tcp {
    private static final int TCP_HEADER_SIZE_BYTES = 20;

    private final Port port;
    private final Number number;

    private final byte dataOffset;
    private final ControlFlags controlFlags;

    private final short windowSize;
    private final short checksum;
    private final short urgentPointer;

    private final Options options;

    private final ByteBuffer tcpHeaderStream;
    private final ByteBuffer tcpOptionStream;
    private final ByteBuffer tcpPayloadStream;

    public static class IncomingTcpBuilder extends TcpBuilder {
        IncomingTcpBuilder(final Port port, final Number number, final byte dataOffset, final ControlFlags controlFlags,
                           short windowSize, short checksum, short urgentPointer) {
            this.port = port;
            this.number = number;
            this.dataOffset = dataOffset;
            this.controlFlags = controlFlags;
            this.windowSize = windowSize;
            this.checksum = checksum;
            this.urgentPointer = urgentPointer;
        }

        @Override public Tcp build() {
            return new Tcp(this);
        }
    }

    public static class OutgoingTcpBuilder extends TcpBuilder {
        // for generating the checksum
        private final int sourceAddress;
        private final int destinationAddress;

        public OutgoingTcpBuilder(int sourceAddress, int destinationAddress,
                                  final Port port,
                                  final Number number,
//                           byte dataOffset,
                            final ControlFlags controlFlags,
                            short windowSize,
//                           short checksum,
                                  short urgentPointer) {
            this.sourceAddress = sourceAddress;
            this.destinationAddress = destinationAddress;

            this.port = port;
            this.number = number;
            //            this.dataOffset = dataOffset;
            this.controlFlags = controlFlags;
            this.windowSize = windowSize;
//            this.checksum = checksum;
            this.urgentPointer = urgentPointer;
        }

        private void createTcpHeaderStream() {
            tcpHeaderStream = ByteBuffer.allocate(TCP_HEADER_SIZE_BYTES);
            tcpHeaderStream.putShort(port.getSource());
            tcpHeaderStream.putShort(port.getDestination());

            tcpHeaderStream.putInt(number.getSequence());
            tcpHeaderStream.putInt(number.getAcknowledgement());

            // temporarily set dataOffset to 0 for occupying its space
            // tcpHeaderStream.put(dataOffset);
            tcpHeaderStream.put((byte) 0);

            final byte controlBits = (byte) ((controlFlags.isCWR() ? 1 : 0) << 7 |
                    (controlFlags.isECE() ? 1 : 0) << 6 |
                    (controlFlags.isURG() ? 1 : 0) << 5 |
                    (controlFlags.isACK() ? 1 : 0) << 4 |
                    (controlFlags.isPSH() ? 1 : 0) << 3 |
                    (controlFlags.isRST() ? 1 : 0) << 2 |
                    (controlFlags.isSYN() ? 1 : 0) << 1 |
                    (controlFlags.isFIN() ? 1 : 0));
            tcpHeaderStream.put(controlBits);

            tcpHeaderStream.putShort(windowSize);
            tcpHeaderStream.putShort((short) 0); // checksum

            // Don't use urgent pointer
            tcpHeaderStream.putShort((short) 0); // urgent pointer

            tcpHeaderStream.rewind();
        }

        private void setDataOffset() {
            final int optionsLength = tcpOptionStream.capacity();
            dataOffset = (byte) ((TCP_HEADER_SIZE_BYTES + optionsLength) / 4);
            final byte dataOffsetAndNs = (byte) (dataOffset << 4 + (controlFlags.isNS() ? 1 : 0));
            tcpHeaderStream.put(12, dataOffsetAndNs);
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

        @NotNull public Tcp build() {
            createTcpHeaderStream();
            tcpOptionStream = options.createOptionStream();
            setDataOffset();
            setChecksum();

            return new Tcp(this);
        }
    }

    Tcp(@NotNull TcpBuilder builder) {
        this.port = builder.port;
        this.number = builder.number;
        this.dataOffset = builder.dataOffset;
        this.controlFlags = builder.controlFlags;
        this.windowSize = builder.windowSize;
        this.checksum = builder.checksum;
        this.urgentPointer = builder.urgentPointer;
        this.options = builder.options;
        this.tcpHeaderStream = builder.tcpHeaderStream;
        this.tcpOptionStream = builder.tcpOptionStream;
        this.tcpPayloadStream = builder.tcpPayloadStream;
    }

    @NotNull public Port getPort() {
        return port;
    }

    @NotNull public Number getNumber() {
        return number;
    }

    public byte getDataOffset() {
        return dataOffset;
    }

    @NotNull public ControlFlags getControlFlags() {
        return controlFlags;
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

    @NotNull public Options getOptions() {
        return options;
    }
    @NotNull public ByteBuffer getTcpHeaderStream() {
        return tcpHeaderStream;
    }

    @Nullable public ByteBuffer getTcpOptionStream() {
        return tcpOptionStream;
    }

    @Nullable public ByteBuffer getTcpPayloadStream() {
        return tcpPayloadStream;
    }

//    public  boolean checkSynOnly() {
//        return SYN && !ACK && !FIN && !PSH && !RST;
//    }
}