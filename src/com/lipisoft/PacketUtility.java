package com.lipisoft;

class PacketUtility {
    static String convertAddress(int address) {
        return ((address >>> 24) & 0xFF) + "." + ((address >> 16) & 0xFF) + "." + ((address >> 8) & 0xFF) + "." +
                (address & 0xFF);
    }
}
