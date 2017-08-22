package com.lipisoft.ip;

import com.lipisoft.tcp.Tcp;
import com.lipisoft.tcp.TcpFactory;
import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;

public class IpV4Factory {
    @NotNull public static IpV4 createIpV4(@NotNull ByteBuffer stream) {
        final byte versionAndiHL = stream.get();
        final byte version = (byte) ((versionAndiHL & 0xf0) >> 4);
        final byte internetHeaderLength = (byte) (versionAndiHL & 0x0f);
        final byte dscpAndEcn = stream.get();
        final byte differentiatedServiceCodePoint = (byte) (dscpAndEcn >> 2);
        final byte explicitCongestionNotification = (byte) (dscpAndEcn & 0x3);
        final short totalLength = stream.getShort();
        final short identification = stream.getShort();
        final short flagsAndFragmentOffset = stream.getShort();
        final boolean doNotFragment = (flagsAndFragmentOffset & 0x4000) != 0;
        final boolean moreFragments = (flagsAndFragmentOffset & 0x2) != 0;
        final short fragmentOffset = (short) (flagsAndFragmentOffset & 0x1fff);
        final byte timeToLive = stream.get();
        final byte protocol = stream.get();
        final short headerChecksum = stream.getShort();
        final int sourceAddress = stream.getInt();
        final int destinationAddress = stream.getInt();

        final Tcp tcp = TcpFactory.createTCP(stream);
        final ByteBuffer ipStream = ByteBuffer.allocate(IpV4.IPV4_HEADER_SIZE);
        stream.rewind();
        ipStream.put(stream.array(), 0, IpV4.IPV4_HEADER_SIZE);

        return new IpV4.IncomingIpV4Builder(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragments, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress, ipStream, tcp).build();
    }
}
