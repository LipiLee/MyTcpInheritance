package com.lipisoft.tcp;

import com.sun.istack.internal.NotNull;

import java.nio.ByteBuffer;
import java.util.List;

public class Options {
    private static final int MAX_TCP_OPTIONS_SIZE_BYTES = 40;

    private final short maxSegmentSize;
    private final byte windowScale;
    private final boolean selectiveAckPermitted;
    private final List<SelectiveAck> selectiveAcks;
    private final TimeStamp timeStamp;

    Options(final short maxSegmentSize, final byte windowScale, final boolean selectiveAckPermitted,
                   final List<SelectiveAck> selectiveAcks, final TimeStamp timeStamp) {
        this.maxSegmentSize = maxSegmentSize;
        this.windowScale = windowScale;
        this.selectiveAckPermitted = selectiveAckPermitted;
        this.selectiveAcks = selectiveAcks;
        this.timeStamp = timeStamp;
    }

    public short getMaxSegmentSize() {
        return maxSegmentSize;
    }

    public byte getWindowScale() {
        return windowScale;
    }

    @NotNull
    ByteBuffer createOptionStream() {
        final ByteBuffer buffer = ByteBuffer.allocate(MAX_TCP_OPTIONS_SIZE_BYTES);

        if (isValidMaxSegmentSize()) {
            writeMaxSegementSize(buffer);
        }

        if (isValidWindowScale()) {
            writeWindowScale(buffer);
        }

        if (isValidSelectiveAckPermitted()) {
            writeSelectiveAckPermitted(buffer);
        }

        if (isValidSelectiveAcks()) {
            writeSelectiveAcks(buffer);
        }

        if (timeStamp.isValid()) {
            writeTimeStamp(buffer);
        }

        // 32-bit boundaries for better performance
        alignFourBytes(buffer);

        return fitAdequateSize(buffer);
    }

    @NotNull private ByteBuffer fitAdequateSize(@NotNull ByteBuffer maxBuffer) {
        final int size = maxBuffer.position();
        final ByteBuffer stream = ByteBuffer.allocate(size);
        maxBuffer.flip();
        stream.put(maxBuffer);
        stream.rewind();

        return stream;
    }

    private void alignFourBytes(@NotNull ByteBuffer stream) {
        while (stream.position() % 4 != 0) {
            // Add NOP(No operation)
            stream.put((byte) 1);
        }
    }

    public boolean isSelectiveAckPermitted() {
        return selectiveAckPermitted;
    }

    public List<SelectiveAck> getSelectiveAcks() {
        return selectiveAcks;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    private boolean isValidMaxSegmentSize() {
        return maxSegmentSize != 0;
    }

    private boolean isValidWindowScale() {
        return windowScale != 0;
    }

    private boolean isValidSelectiveAckPermitted() {
        return selectiveAckPermitted;
    }

    private boolean isValidSelectiveAcks() {
        return selectiveAcks.size() != 0;
    }

    private void writeMaxSegementSize(@NotNull ByteBuffer stream) {
        stream.put((byte) 2);
        stream.put((byte) 4);
        stream.putShort(maxSegmentSize);
    }

    private void writeWindowScale(@NotNull ByteBuffer stream) {
        stream.put((byte) 3);
        stream.put((byte) 3);
        stream.put(windowScale);
    }

    private void writeSelectiveAckPermitted(@NotNull ByteBuffer stream) {
        stream.put((byte) 4);
        stream.put((byte) 2);
    }

    private void writeSelectiveAcks(@NotNull ByteBuffer stream) {
        stream.put((byte) 5);
        stream.put((byte) selectiveAcks.size());

        for (SelectiveAck selectiveAck: selectiveAcks) {
            stream.putInt(selectiveAck.getBegin());
            stream.putInt(selectiveAck.getEnd());
        }
    }

    private void writeTimeStamp(@NotNull ByteBuffer stream) {
        stream.put((byte) 8);
        stream.put((byte) 10);
        stream.putInt(timeStamp.getSender());
        stream.putInt(timeStamp.getEchoReply());
    }
}
