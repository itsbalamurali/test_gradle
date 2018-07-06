package com.girmiti.mobilepos.util;

/**
 * Created by Girmiti on 9/11/16.
 */

public class UIValidation {

    private UIValidation() {
    }

    public static boolean isCardHolderNameNotEmpty(String name) {
        if (name == null || name.equals("") || name.length() == 0)
            return false;
        else
            return true;
    }

    public static boolean isExpDateNotEmpty(String expDate) {
        if (expDate == null || expDate.length() < Constants.FOUR || expDate.equals(""))
            return false;
        else
            return true;
    }

    public static boolean isPanNotEmpty(String pan) {
        if (pan == null || pan.length() < Constants.FOUR || pan.equals(""))
            return false;
        else
            return true;
    }

    public static boolean isPanValid(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < Constants.THIRTEEN
                || !CommonUtils.validateCardNumber(cardNumber.trim()))
            return false;
        else
            return true;
    }

    public static boolean isValidAccountNumber(String accountNumber) {

        if (accountNumber == null || accountNumber.isEmpty() || accountNumber.length() < Constants.TEN)
            return false;
        else
            return true;
    }

    public static boolean isValidAmount(String amount) {

        if (amount == null || amount.isEmpty() || amount.equals("0.00"))
            return false;
        else
            return true;
    }

    public static boolean isValidManualTipAmount(String amount) {

        if (amount == null || amount.isEmpty() || amount.equals("0.00") || amount.equals("."))
            return false;
        else
            return true;
    }

    public static boolean isCVVEmpty(String cvvData) {
        if (cvvData == null)
            return false;

        if(cvvData.trim().length() < Constants.THREE)
            return false;

        try {
            Integer.valueOf(cvvData);
        } catch(NumberFormatException e) {
            return false;
        }

        return true;

    }
}
