package com.girmiti.mobilepos.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.girmiti.mobilepos.net.model.LoginResponse;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by nayan on 7/2/17.
 */

public class CurrencyFormat {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    protected Context context;
    private static final String PREF_NAME = "currencyFormat";
    private static final String CURRENCY_COMMA_SEPARATER = "currencyCommaSeparater";
    private static final String CURRENCY_CODE_ALPHA = "currencyCodeAlpha";
    private static final String CURRENCY_CODE_NUMERIC = "currencyCodeNumeric";
    private static final String CURRENCY_CODE_EXPONENT = "currencyCodeExponent";
    private static final String CURRENCY_CODE_MINOR_UNIT = "currencyCodeMinorUnit";
    private static final String CURRENCY_CODE_THOUSAND_UNIT = "currencyCodeThousandUnit";
    public static final String VALUES="#0.00";


    public CurrencyFormat(Context context) {
        this.context = context;
        initilizePref(context);
    }

    private static void initilizePref(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }

    public void setCurrencyData(LoginResponse loginResponse){
        editor.putString(CURRENCY_COMMA_SEPARATER, loginResponse.getCurrencyDTO().getCurrencySeparatorPosition());
        editor.putString(CURRENCY_CODE_ALPHA, loginResponse.getCurrencyDTO().getCurrencyCodeAlpha());
        editor.putString(CURRENCY_CODE_NUMERIC, loginResponse.getCurrencyDTO().getCurrencyCodeNumeric());
        editor.putString( CURRENCY_CODE_EXPONENT,loginResponse.getCurrencyDTO().getCurrencyExponent() );
        editor.putString( CURRENCY_CODE_MINOR_UNIT,loginResponse.getCurrencyDTO().getCurrencyMinorUnit() );
        editor.putString( CURRENCY_CODE_THOUSAND_UNIT,loginResponse.getCurrencyDTO().getCurrencyThousandsUnit() );
        editor.commit();
    }

    // get currency comma separater
    public static String getCurrencySeparater(){ return pref.getString(CURRENCY_COMMA_SEPARATER, "");
    }

    // get currency currency code aplha
    public static String getCurrencyCodeAlplha(){ return pref.getString(CURRENCY_CODE_ALPHA, "");
    }

    // get currency currency code MinorUnit
    public static String getCurrencyCodeMinorUnit(){ return pref.getString(CURRENCY_CODE_MINOR_UNIT, "");
    }

    // get currency currency code ThousandUnit
    public static String getCurrencyCodeThousandUnit(){ return pref.getString(CURRENCY_CODE_THOUSAND_UNIT, "");
    }

    public static String getFormattedCurrency(String currency){

        String curr = currency.replace(",",".");
        Double dobCurrency = Double.valueOf(curr);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(CurrencyFormat.getCurrencyCodeMinorUnit().charAt( 0 ));
        symbols.setGroupingSeparator(CurrencyFormat.getCurrencyCodeThousandUnit().charAt(0));
        DecimalFormat formatter = new DecimalFormat(VALUES,symbols);
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(Integer.parseInt(CurrencyFormat.getCurrencySeparater()));
        String formated = formatter.format( dobCurrency );
        if(formated.contains( "-" )) {
            return CurrencyFormat.getCurrencyCodeAlplha() + "  " + formated;
        } else {
            return CurrencyFormat.getCurrencyCodeAlplha() + " " + formated;
        }

    }

    public static String getFormattedCurrency( String currency, String locale){
        String curr = "";
        if(locale.equals( "es" )) {
            curr = currency.replace( ",", "." );
        }
        else {
            curr = currency;
        }
        Double dobCurrency = Double.valueOf(curr);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(CurrencyFormat.getCurrencyCodeMinorUnit().charAt( 0 ));
        symbols.setGroupingSeparator(CurrencyFormat.getCurrencyCodeThousandUnit().charAt(0));
        DecimalFormat formatter = new DecimalFormat(VALUES,symbols);
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(Integer.parseInt(CurrencyFormat.getCurrencySeparater()));
        String formated = formatter.format( dobCurrency );

        return CurrencyFormat.getCurrencyCodeAlplha() + " " + formated;

    }
}
