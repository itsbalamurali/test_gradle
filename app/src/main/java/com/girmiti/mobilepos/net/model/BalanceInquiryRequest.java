package com.girmiti.mobilepos.net.model;

import com.girmiti.mobilepos.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nayan on 11/10/17.
 */
public class BalanceInquiryRequest extends Request {
    private static Logger logger = Logger.getNewLogger("com.gmt.net.Network");

    private String cardNumber;
    private String expDate;
    private String cvv;
    private String cardHolderName;
    private CardData cardData;
    private String entryMode;
    private String userName;


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    @Override
    public String createRequest() {
        JSONObject requestObject = new JSONObject();
        try {
            JSONObject cardDataObj = new JSONObject();
            cardDataObj.put( "cardNumber", cardData.cardNumber );
            cardDataObj.put( "expDate", cardData.expDate );
            cardDataObj.put( "cvv", cardData.cvv );
            cardDataObj.put( "cardHolderName", cardData.cardHolderName );
            cardDataObj.put( "cardType", cardData.cardType );
            cardDataObj.put( "emv", cardData.emv );
            cardDataObj.put( "track2", cardData.track2 );
            cardDataObj.put( "uid", cardData.uid );
            requestObject.put( "cardData", cardDataObj );
        }  catch (JSONException e) {
            logger.severe("JSONException"+ e );
        }
        return requestObject.toString();
    }
    public CardData getCardData() {
        return cardData;
    }

    public void setCardData(CardData cardData) {
        this.cardData = cardData;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
