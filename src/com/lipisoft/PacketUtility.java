package com.lipisoft;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;

class PacketUtility {
    static String convertAddress(int address) {
        return String.valueOf((address >>> 24) & 0xFF) + "." +
                String.valueOf((address >>> 16) & 0xFF) + "." +
                String.valueOf((address >>> 8) & 0xFF) + "." +
                String.valueOf(address & 0xFF);
    }

    static byte get8BitsToByte(@NotNull ByteBuffer packet) {
        return (byte) (packet.get() & 0xFF);
    }

    static short get8BitsToShort(@NotNull ByteBuffer packet) {
        return (short) (packet.get() & 0xFF);
    }

    static short get16BitsToShort(@NotNull ByteBuffer packet) {
        return (short) (packet.getShort() & 0xFFFF);
    }

    static int get16BitsToInt(@NotNull ByteBuffer packet) {
        return packet.getShort() & 0xFFFF;
    }

    static int get32BitsToInt(@NotNull ByteBuffer packet) {
        return packet.getInt();
    }

    static long get32BitsToLong(@NotNull ByteBuffer packet) {
        return packet.getInt() & 0xFFFFFFFFL;
    }
}
