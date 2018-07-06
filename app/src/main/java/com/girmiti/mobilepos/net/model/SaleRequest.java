package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class SaleRequest extends Request {
    private static Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    protected String invoiceNumber;
    protected String merchantAmount;
    protected String feeAmount;
    protected String totalTxnAmount;
    protected String registerNumber;
    protected String orderId;
    protected String merchantName;
    protected CardData cardData;
    protected BillingData billingData;
    protected String entryMode;
    protected String userName;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    protected String qrCode;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public SaleRequest() {
        transactionType = "SALE";
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }


    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public String getMerchantAmount() {
        return merchantAmount;
    }


    public void setMerchantAmount(String merchantAmount) {
        this.merchantAmount = merchantAmount;
    }


    public String getFeeAmount() {
        return feeAmount;
    }


    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }


    public String getTotalTxnAmount() {
        return totalTxnAmount;
    }


    public void setTotalTxnAmount(String totalTxnAmount) {
        this.totalTxnAmount = totalTxnAmount;
    }


    public String getRegisterNumber() {
        return registerNumber;
    }


    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }


    public String getOrderId() {
        return orderId;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getMerchatntName() {
        return merchantName;
    }


    public void setMerchatntName(String merchantName) {
        this.merchantName = merchantName;
    }


    public CardData getCardData() {
        return cardData;
    }


    public void setCardData(CardData cardData) {
        this.cardData = cardData;
    }


    public BillingData getBillingData() {
        return billingData;
    }


    public void setBillingData(BillingData billingData) {
        this.billingData = billingData;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String createRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("merchantId", merchantId);
            requestObject.put("terminalId", terminalId);
            requestObject.put("invoiceNumber", invoiceNumber);
            requestObject.put("merchantAmount", merchantAmount);
            requestObject.put("feeAmount", feeAmount);
            requestObject.put("totalTxnAmount", totalTxnAmount);
            requestObject.put("transactionType", transactionType);
            requestObject.put("registerNumber", registerNumber);
            requestObject.put("orderId", orderId);
            requestObject.put("merchantName", merchantName);
            requestObject.put("entryMode", entryMode);

            JSONObject cardDataObj = new JSONObject();
            cardDataObj.put("cardNumber", cardData.cardNumber);
            cardDataObj.put("expDate", cardData.expDate);
            cardDataObj.put("cvv", cardData.cvv);
            cardDataObj.put("cardHolderName", cardData.cardHolderName);
            cardDataObj.put("cardType", cardData.cardType);
            cardDataObj.put("emv", cardData.emv);
            cardDataObj.put("track2", cardData.track2);
            cardDataObj.put("uid", cardData.uid);
            requestObject.put("cardData", cardDataObj);

            JSONObject billingDataObj = new JSONObject();
            billingDataObj.put("address1", billingData.address1);
            billingDataObj.put("address2", billingData.address2);
            billingDataObj.put("city", billingData.city);
            billingDataObj.put("country", billingData.country);
            billingDataObj.put("email", billingData.email);
            billingDataObj.put("state", billingData.state);
            billingDataObj.put("zipCode", billingData.zipCode);
            requestObject.put("billingData", billingDataObj);

        } catch (JSONException e) {
            logger.severe("JSONException"+ e );
        }
        return requestObject.toString();
    }

}
