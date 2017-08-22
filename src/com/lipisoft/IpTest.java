package com.lipisoft;

class IpTest {
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

    IpTest(byte version, byte internetHeaderLength, byte differentiatedServiceCodePoint,
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

    byte getVersion() {
        return version;
    }

    byte getInternetHeaderLength() {
        return internetHeaderLength;
    }

    byte getDifferentiatedServiceCodePoint() {
        return differentiatedServiceCodePoint;
    }

    byte getExplicitCongestionNotification() {
        return explicitCongestionNotification;
    }

    short getTotalLength() {
        return totalLength;
    }

    short getIdentification() {
        return identification;
    }

    boolean isDoNotFragment() {
        return doNotFragment;
    }

    boolean isMoreFragment() {
        return moreFragment;
    }

    short getFragmentOffset() {
        return fragmentOffset;
    }

    byte getTimeToLive() {
        return timeToLive;
    }

    byte getProtocol() {
        return protocol;
    }

    short getHeaderChecksum() {
        return headerChecksum;
    }

    int getSourceAddress() {
        return sourceAddress;
    }

    int getDestinationAddress() {
        return destinationAddress;
    }
}
