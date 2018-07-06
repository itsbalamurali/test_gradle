package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class VoidRequest extends Request {
    private static Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    private String txnRefNumber;
    private String cgRefNumber;

    @Override
    public String createRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("merchantId", merchantId);
            requestObject.put("terminalId", terminalId);
            requestObject.put("transactionType", transactionType);
            requestObject.put("txnRefNumber", txnRefNumber);
            requestObject.put("cgRefNumber", cgRefNumber);

        } catch (JSONException e) {
            logger.severe( "JSONException"+e );
        }
        return requestObject.toString();
    }

    public VoidRequest() {
        transactionType = "VOID";
    }

    public String getTxnRefNumber() {
        return txnRefNumber;
    }

    public void setTxnRefNumber(String txnRefNumber) {
        this.txnRefNumber = txnRefNumber;
    }

    public String getCgRefNumber() {
        return cgRefNumber;
    }

    public void setCgRefNumber(String cgRefNumber) {
        this.cgRefNumber = cgRefNumber;
    }


}
