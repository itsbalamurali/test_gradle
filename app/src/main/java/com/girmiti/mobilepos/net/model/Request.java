package com.girmiti.mobilepos.net.model;


public abstract class Request {

    protected String merchantId;
    protected String terminalId;
    protected String transactionType;
    protected String timeZoneOffset;
    protected String timeZoneRegion;

    // need to be implemented by all transactions
    public abstract String createRequest();

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTxnType() {
        return transactionType;
    }

    public void setTxnType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public String getTimeZoneRegion() {
        return timeZoneRegion;
    }

    public void setTimeZoneRegion(String timeZoneRegion) {
        this.timeZoneRegion = timeZoneRegion;
    }
}
