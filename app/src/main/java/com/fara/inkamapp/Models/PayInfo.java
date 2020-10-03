package com.fara.inkamapp.Models;

public class PayInfo {
    private long RRN;
    private int TrcNo;
    private String CardNo;
    private int Status;
    private String PayDTime;

    public long getRRN() {
        return RRN;
    }

    public void setRRN(long RRN) {
        this.RRN = RRN;
    }

    public int getTrcNo() {
        return TrcNo;
    }

    public void setTrcNo(int trcNo) {
        TrcNo = trcNo;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getPayDTime() {
        return PayDTime;
    }

    public void setPayDTime(String payDTime) {
        PayDTime = payDTime;
    }
}
