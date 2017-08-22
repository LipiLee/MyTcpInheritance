package com.lipisoft.tcp;

public class SelectiveAck {
    private final int begin;
    private final int end;

    public SelectiveAck(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    int getBegin() {
        return begin;
    }

    int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectiveAck that = (SelectiveAck) o;

        return begin == that.begin && end == that.end;
    }
}
