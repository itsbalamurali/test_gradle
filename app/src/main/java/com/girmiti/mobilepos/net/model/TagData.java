package com.girmiti.mobilepos.net.model;

/**
 * Created by nayan on 25/10/17.
 */
public class TagData {

    private String nfcData;
    private String cardUid;

    public String getNfcData() {
        return nfcData;
    }

    public void setNfcData(String nfcData) {
        this.nfcData = nfcData;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }
}
