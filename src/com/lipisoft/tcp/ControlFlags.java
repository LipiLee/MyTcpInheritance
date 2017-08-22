package com.lipisoft.tcp;

public class ControlFlags {
    private final boolean NS;
    private final boolean CWR;
    private final boolean ECE;
    private final boolean URG;
    private final boolean ACK;
    private final boolean PSH;
    private final boolean RST;
    private final boolean SYN;
    private final boolean FIN;

    public ControlFlags(final boolean NS, final boolean CWR, final boolean ECE, final boolean URG, final boolean ACK,
            final boolean PSH, final boolean RST, final boolean SYN, final boolean FIN) {
        this.NS = NS;
        this.CWR = CWR;
        this.ECE = ECE;
        this.URG = URG;
        this.ACK = ACK;
        this.PSH = PSH;
        this.RST = RST;
        this.SYN = SYN;
        this.FIN = FIN;
    }

    public boolean isNS() {
        return NS;
    }

    public boolean isCWR() {
        return CWR;
    }

    public boolean isECE() {
        return ECE;
    }

    public boolean isURG() {
        return URG;
    }

    public boolean isACK() {
        return ACK;
    }

    public boolean isPSH() {
        return PSH;
    }

    public boolean isRST() {
        return RST;
    }

    public boolean isSYN() {
        return SYN;
    }

    public boolean isFIN() {
        return FIN;
    }
}
