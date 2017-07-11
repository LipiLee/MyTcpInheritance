package com.lipisoft;

public class SelectiveAck {
    private final long begin;
    private final long end;

    SelectiveAck(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }
}
