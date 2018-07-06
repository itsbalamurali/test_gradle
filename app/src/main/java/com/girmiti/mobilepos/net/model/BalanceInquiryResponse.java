package com.girmiti.mobilepos.net.model;

/**
 * Created by nayan on 11/10/17.
 */
public class BalanceInquiryResponse extends Response {
    private String customerBalance;
    private String currency;
    private String txnId;
    private String terminalId;
    private String merchantId;
    private String procTxnId;

    public BalanceInquiryResponse(String response) {
        super( response );
    }

    public String getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(String customerBalance) {
        this.customerBalance = customerBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProcTxnId() {
        return procTxnId;
    }

    public void setProcTxnId(String procTxnId) {
        this.procTxnId = procTxnId;
    }
}
