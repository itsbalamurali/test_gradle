package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Response implements Serializable {
    private static Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    protected String errorCode;
    protected String errorMessage;
    protected String deviceLocalTxnTime;
    protected String txnRefNumber;
    protected String authId;
    protected String cgRefNumber;
    protected String merchantCode;
    protected String merchantName;

    // parse the xml data
    public Response(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            errorCode = obj.getString("errorCode");
            errorMessage = obj.getString("errorMessage");
            deviceLocalTxnTime = obj.getString("txnDateTime");
            txnRefNumber = obj.getString("txnRefNumber");
            authId = obj.getString("authId");
            cgRefNumber = obj.getString("cgRefNumber");
            merchantCode = obj.getString("merchantCode");
            merchantName = obj.getString("merchantName");
        } catch (JSONException e) {
            logger.severe("JSONException" + e);
        }
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDeviceLocalTxnTime() {
        return deviceLocalTxnTime;
    }

    public String getTxnRefNumber() {
        return txnRefNumber;
    }

    public String getAuthId() {
        return authId;
    }

    public String getCgRefNumber() {
        return cgRefNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

}
