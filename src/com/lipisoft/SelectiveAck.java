package com.lipisoft;

class SelectiveAck {
    private final int begin;
    private final int end;

    SelectiveAck(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    int getBegin() {
        return begin;
    }

    int getEnd() {
        return end;
    }
}
