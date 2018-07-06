package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nayan on 10/10/16.
 */

public class LoadFundRequest extends Request {
    private  Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    private String entryMode;
    private Long totalTxnAmount;
    private String mobileNumber;
    private String accountNumber;

    @Override
    public String createRequest() {

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("entryMode", entryMode);
            requestObject.put("terminalId", terminalId);
            requestObject.put("transactionType", transactionType);
            requestObject.put("merchantId", merchantId);
            requestObject.put("totalTxnAmount", totalTxnAmount);
            requestObject.put("mobileNumber", mobileNumber);

        } catch (JSONException e) {
            logger.severe("JSONException"+ e);
        }
        return requestObject.toString();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public Long getTotalTxnAmount() {
        return totalTxnAmount;
    }

    public void setTotalTxnAmount(Long totalTxnAmount) {
        this.totalTxnAmount = totalTxnAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
