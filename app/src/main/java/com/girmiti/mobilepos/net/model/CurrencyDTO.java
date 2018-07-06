package com.girmiti.mobilepos.net.model;

/**
 * Created by nayan on 7/2/17.
 */

public class CurrencyDTO {

    private String currencyThousandsUnit;
    private String currencyMinorUnit;
    private String currencyExponent;
    private String currencyCodeNumeric;
    private String currencySeparatorPosition;
    private String currencyCodeAlpha;

    public String getCurrencyThousandsUnit() {
        return currencyThousandsUnit;
    }

    public void setCurrencyThousandsUnit(String currencyThousandsUnit) {
        this.currencyThousandsUnit = currencyThousandsUnit;
    }

    public String getCurrencyMinorUnit() {
        return currencyMinorUnit;
    }

    public void setCurrencyMinorUnit(String currencyMinorUnit) {
        this.currencyMinorUnit = currencyMinorUnit;
    }

    public String getCurrencyExponent() {
        return currencyExponent;
    }

    public void setCurrencyExponent(String currencyExponent) {
        this.currencyExponent = currencyExponent;
    }

    public String getCurrencyCodeAlpha() {
        return currencyCodeAlpha;
    }

    public void setCurrencyCodeAlpha(String currencyCodeAlpha) {
        this.currencyCodeAlpha = currencyCodeAlpha;
    }

    public String getCurrencyCodeNumeric() {
        return currencyCodeNumeric;
    }

    public void setCurrencyCodeNumeric(String currencyCodeNumeric) {
        this.currencyCodeNumeric = currencyCodeNumeric;
    }

    public String getCurrencySeparatorPosition() {
        return currencySeparatorPosition;
    }

    public void setCurrencySeparatorPosition(String currencySeparatorPosition) {
        this.currencySeparatorPosition = currencySeparatorPosition;
    }


}
