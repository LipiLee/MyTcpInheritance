package com.lipisoft;

public class IpTest {
    private final byte version;
    private final byte internetHeaderLength;
    private final byte differentiatedServiceCodePoint;
    private final byte explicitCongestionNotification;
    private final short totalLength;
    private final short identification;
    private final boolean doNotFragment;
    private final boolean moreFragment;
    private final short fragmentOffset;
    private final byte timeToLive;
    private final byte protocol;
    private final short headerChecksum;
    private final int sourceAddress;
    private final int destinationAddress;

    public IpTest(byte version, byte internetHeaderLength, byte differentiatedServiceCodePoint,
                  byte explicitCongestionNotification, short totalLength, short identification, boolean doNotFragment,
                  boolean moreFragment, short fragmentOffset, byte timeToLive, byte protocol, short headerChecksum,
                  int sourceAddress, int destinationAddress) {
        this.version = version;
        this.internetHeaderLength = internetHeaderLength;
        this.differentiatedServiceCodePoint = differentiatedServiceCodePoint;
        this.explicitCongestionNotification = explicitCongestionNotification;
        this.totalLength = totalLength;
        this.identification = identification;
        this.doNotFragment = doNotFragment;
        this.moreFragment = moreFragment;
        this.fragmentOffset = fragmentOffset;
        this.timeToLive = timeToLive;
        this.protocol = protocol;
        this.headerChecksum = headerChecksum;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
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

    public boolean isDoNotFragment() {
        return doNotFragment;
    }

    public boolean isMoreFragment() {
        return moreFragment;
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
}
