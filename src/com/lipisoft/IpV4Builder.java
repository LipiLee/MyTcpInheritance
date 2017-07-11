package com.lipisoft;

import java.nio.ByteBuffer;

abstract class IpV4Builder {
    byte version;
    byte internetHeaderLength;
    byte differentiatedServiceCodePoint;
    byte explicitCongestionNotification;
    short totalLength;
    short identification;
    boolean doNotFragment;
    boolean moreFragments;
    short fragmentOffset;
    byte timeToLive;
    byte protocol;
    short headerChecksum;
    int sourceAddress;
    int destinationAddress;

    ByteBuffer stream;
    Tcp tcp;

    abstract IpV4 build();
}
