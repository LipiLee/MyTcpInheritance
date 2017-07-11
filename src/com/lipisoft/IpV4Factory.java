package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;

class IpV4Factory {
    @NotNull
    static IpV4 createIpV4(@NotNull ByteBuffer stream) {
        final byte versionAndiHL = PacketUtility.get8BitsToByte(stream);
        final byte version = (byte) ((versionAndiHL & 0xf0) >> 4);
        final byte internetHeaderLength = (byte) (versionAndiHL & 0x0f);
        final byte dscpAndEcn = PacketUtility.get8BitsToByte(stream);
        final byte differentiatedServiceCodePoint = (byte) (dscpAndEcn >> 2);
        final byte explicitCongestionNotification = (byte) (dscpAndEcn & 0x3);
        final short totalLength = PacketUtility.get16BitsToShort(stream);
        final short identification = PacketUtility.get16BitsToShort(stream);
        final short flagsAndFragmentOffset = PacketUtility.get16BitsToShort(stream);
        final boolean doNotFragment = (flagsAndFragmentOffset & 0x4000) != 0;
        final boolean moreFragments = (flagsAndFragmentOffset & 0x2) != 0;
        final short fragmentOffset = (short) (flagsAndFragmentOffset & 0x1fff);
        final byte timeToLive = PacketUtility.get8BitsToByte(stream);
        final byte protocol = PacketUtility.get8BitsToByte(stream);
        final short headerChecksum = PacketUtility.get16BitsToShort(stream);
        final int sourceAddress = PacketUtility.get32BitsToInt(stream);
        final int destinationAddress = PacketUtility.get32BitsToInt(stream);

        final Tcp tcp = TcpFactory.createTCP(stream);
        final ByteBuffer ipStream = ByteBuffer.allocate(IpV4.IPV4_HEADER_SIZE);
        stream.rewind();
        ipStream.put(stream.array(), 0, 20);

        return new IpV4.IncomingIpV4Builder(version, internetHeaderLength, differentiatedServiceCodePoint,
                explicitCongestionNotification, totalLength, identification, doNotFragment, moreFragments, fragmentOffset,
                timeToLive, protocol, headerChecksum, sourceAddress, destinationAddress, ipStream, tcp).build();
    }
}
