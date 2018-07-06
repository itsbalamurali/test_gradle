package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rahul on 5/10/16.
 */
public class RefundRequest extends Request {
    private static Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    private String expDate;
    private String invoiceNumber;
    private String totalTxnAmount;
    private String cardNum;
    private String txnRefNumber;
    private String cgRefNumber;
    private String merchantAmount;

    @Override
    public String createRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("txnRefNumber", txnRefNumber);
            requestObject.put("cgRefNumber", cgRefNumber);
            requestObject.put("merchantId", merchantId);
            requestObject.put("terminalId", terminalId);
            requestObject.put("transactionType", transactionType);
            requestObject.put("totalTxnAmount", totalTxnAmount);
            requestObject.put("merchantAmount", merchantAmount);
        } catch (JSONException e) {
            logger.severe("JSONException"+ e);
        }
        return requestObject.toString();
    }

    public RefundRequest() {
        transactionType = "REFUND";
    }

    public String getMerchantAmount() {
        return merchantAmount;
    }

    public void setMerchantAmount(String merchantAmount) {
        this.merchantAmount = merchantAmount;
    }

    public String getCgRefNumber() {
        return cgRefNumber;
    }

    public void setCgRefNumber(String cgRefNumber) {
        this.cgRefNumber = cgRefNumber;
    }

    public String getTxnRefNumber() {
        return txnRefNumber;
    }

    public void setTxnRefNumber(String txnRefNumber) {
        this.txnRefNumber = txnRefNumber;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getTotalTxnAmount() {
        return totalTxnAmount;
    }

    public void setTotalTxnAmount(String totalTxnAmount) {
        this.totalTxnAmount = totalTxnAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }


}
