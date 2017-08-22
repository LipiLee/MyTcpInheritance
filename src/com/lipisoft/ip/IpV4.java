package com.lipisoft.ip;

import com.lipisoft.tcp.Tcp;
import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;

public class IpV4 {
    static final int IPV4_HEADER_SIZE = 20;

    private final byte version;
    private final byte internetHeaderLength;
    private final byte differentiatedServiceCodePoint;
    private final byte explicitCongestionNotification;
    private final short totalLength;
    private final short identification;
    private final boolean dontFragment;
    private final boolean moreFragments;
    private final short fragmentOffset;
    private final byte timeToLive;
    private final byte protocol;
    private final short headerChecksum;
    private final int sourceAddress;
    private final int destinationAddress;
    private final ByteBuffer stream;
    private final Tcp tcp;

    static class IncomingIpV4Builder extends IpV4Builder {
        IncomingIpV4Builder(final byte version,
                final byte internetHeaderLength,
                final byte differentiatedServiceCodePoint,
                final byte explicitCongestionNotification,
                final short totalLength,
                final short identification,
                final boolean dontFragment,
                final boolean moreFragments,
                final short fragmentOffset,
                final byte timeToLive,
                final byte protocol,
                final short headerChecksum,
                final int sourceAddress,
                final int destinationAddress,
                final ByteBuffer stream,
                final Tcp tcp) {
            this.version = version;
            this.internetHeaderLength = internetHeaderLength;
            this.differentiatedServiceCodePoint = differentiatedServiceCodePoint;
            this.explicitCongestionNotification = explicitCongestionNotification;
            this.totalLength = totalLength;
            this.identification = identification;
            this.doNotFragment = dontFragment;
            this.moreFragments = moreFragments;
            this.fragmentOffset = fragmentOffset;
            this.timeToLive = timeToLive;
            this.protocol = protocol;
            this.headerChecksum = headerChecksum;
            this.sourceAddress = sourceAddress;
            this.destinationAddress = destinationAddress;
            this.stream = stream;
            this.tcp = tcp;
        }

        IpV4 build() {
            return new IpV4(this);
        }
    }

    public static class OutGoingIpV4Builder extends IpV4Builder {
        public OutGoingIpV4Builder(final byte version,
                            final byte internetHeaderLength,
                            final byte differentiatedServiceCodePoint,
                            final byte explicitCongestionNotification,
                            final short totalLength,
                            final short identification,
                            final boolean dontFragment,
                            final boolean moreFragments,
                            final short fragmentOffset,
                            final byte timeToLive,
                            final byte protocol,
//                            final short headerChecksum,
                            final int sourceAddress,
                            final int destinationAddress,
                            final Tcp tcp
        ) {
            this.version = version;
            this.internetHeaderLength = internetHeaderLength;
            this.differentiatedServiceCodePoint = differentiatedServiceCodePoint;
            this.explicitCongestionNotification = explicitCongestionNotification;
            this.totalLength = totalLength;
            this.identification = identification;
            this.doNotFragment = dontFragment;
            this.moreFragments = moreFragments;
            this.fragmentOffset = fragmentOffset;
            this.timeToLive = timeToLive;
            this.protocol = protocol;
            this.sourceAddress = sourceAddress;
            this.destinationAddress = destinationAddress;
            this.tcp = tcp;
        }

        byte getFlags() {
            return (byte) (((doNotFragment ? 1 : 0) << 1) | (moreFragments ? 1 : 0));
        }

        void makeIpStream() {
            stream = ByteBuffer.allocate(IPV4_HEADER_SIZE);
            stream.put((byte)((version << 4) | internetHeaderLength));
            stream.put((byte)((differentiatedServiceCodePoint << 2) | explicitCongestionNotification));
            stream.putShort(totalLength);
            stream.putShort(identification);
            stream.putShort((short)((getFlags() << 13) | fragmentOffset));
            stream.put(timeToLive);
            stream.put(protocol);
            stream.putShort((short) 0);
            stream.putInt(sourceAddress);
            stream.putInt(destinationAddress);

            stream.rewind();
        }

        void setChecksum() {
            int sum = 0;

            while (stream.hasRemaining()) {
                sum += stream.getShort();
            }

            while ((sum >> 16) > 0) {
                sum = (sum & 0xfff) + (sum >> 16);
            }

            sum = ~sum;

            headerChecksum = (short) sum;
            stream.putShort(10, headerChecksum);

            stream.rewind();
        }

        private int tcpSize() {
            int tcpSize = tcp.getTcpHeaderStream().array().length;

            if (tcp.getTcpOptionStream() != null) {
                tcpSize += tcp.getTcpOptionStream().array().length;
            }

            if (tcp.getTcpPayloadStream() != null) {
                tcpSize += tcp.getTcpPayloadStream().array().length;
            }

            return tcpSize;
        }

        @NotNull public IpV4 build() {
            totalLength = (short) (IPV4_HEADER_SIZE + tcpSize());
            makeIpStream();
            setChecksum();
            return new IpV4(this);
        }
    }

    private IpV4(@NotNull IpV4Builder builder) {
        this.version = builder.version;
        this.internetHeaderLength = builder.internetHeaderLength;
        this.differentiatedServiceCodePoint = builder.differentiatedServiceCodePoint;
        this.explicitCongestionNotification = builder.explicitCongestionNotification;
        this.totalLength = builder.totalLength;
        this.identification = builder.identification;
        this.dontFragment = builder.doNotFragment;
        this.moreFragments = builder.moreFragments;
        this.fragmentOffset = builder.fragmentOffset;
        this.timeToLive = builder.timeToLive;
        this.protocol = builder.protocol;
        this.headerChecksum = builder.headerChecksum;
        this.sourceAddress = builder.sourceAddress;
        this.destinationAddress = builder.destinationAddress;
        this.stream = builder.stream;
        this.tcp = builder.tcp;
    }

    public byte getVersion() {
        return version;
    }

    public byte getInternetHeaderLength() {
        return internetHeaderLength;
    }

    public byte getDifferentiatedServiceCodePoint() {
        return differentiatedServiceCodePoint;
    }

    public byte getExplicitCongestionNotification() {
        return explicitCongestionNotification;
    }

    public short getTotalLength() {
        return totalLength;
    }

    public short getIdentification() {
        return identification;
    }

    public boolean isDontFragment() {
        return dontFragment;
    }

    public boolean isMoreFragments() {
        return moreFragments;
    }

    public short getFragmentOffset() {
        return fragmentOffset;
    }

    public byte getTimeToLive() {
        return timeToLive;
    }

    public byte getProtocol() {
        return protocol;
    }

    public short getHeaderChecksum() {
        return headerChecksum;
    }

    public int getSourceAddress() {
        return sourceAddress;
    }

    public int getDestinationAddress() {
        return destinationAddress;
    }

    @NotNull public ByteBuffer getStream() {
        return stream;
    }

    @NotNull public Tcp getTcp() {
        return tcp;
    }
}
