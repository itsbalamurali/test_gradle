package com.girmiti.mobilepos.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * -----------------------------------------------------------------------------
 * CommonUtils : This class is used to convert all the primitive data
 * types in Java to bytes and vice-versa. Could be important while passing
 * information over the network
 * -----------------------------------------------------------------------------
 */

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        //empty constructor to hide implicit public one
    }

    public static String hexToString(String hex) {

        StringBuilder sb = new StringBuilder();

        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += Constants.TWO) {

            // grab the hex in pairs
            String output = hex.substring( i, (i + Constants.TWO) );
            // convert hex to decimal
            int decimal = Integer.parseInt( output, Constants.SIXTEEN );
            // convert the decimal to character
            sb.append( (char) decimal );
        }

        return sb.toString();
    }


    public static boolean validateCardNumber(String cardNumber) {
        int sum = 0;
        int digit = 0;
        int addend = 0;
        boolean doubled = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt( cardNumber.substring( i, i + 1 ) );
            if (doubled) {
                addend = digit * Constants.TWO;
                if (addend > Constants.NINE) {
                    addend -= Constants.NINE;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            doubled = !doubled;
        }
        return (sum % Constants.TEN) == 0;
    }

    public static String getCCType(String ccNumber) {

        String visaRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex = "^5[1-5][0-9]{14}$";
        String amexRegex = "^3[47][0-9]{13}$";
        String dinersClubrRegex = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String discoverRegex = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String jcbRegex = "^(?:2131|1800|35\\d{3})\\d{11}$";

        try {
            ccNumber = ccNumber.replaceAll( "\\D", "" );
            String jcbCCNumber = ccNumber.matches( jcbRegex ) ? "JC" : "IP";
            String discoverCCNumber = ccNumber.matches( discoverRegex ) ? "DI" : jcbCCNumber;
            String dinersClubCCNumber = ccNumber.matches( dinersClubrRegex ) ? "DC" : discoverCCNumber;
            String amexCCNumber = ccNumber.matches( amexRegex ) ? "AX" : dinersClubCCNumber;
            String masterCCNumber = ccNumber.matches( masterRegex ) ? "MC" : amexCCNumber;
            return (ccNumber.matches( visaRegex ) ? "VI" : masterCCNumber);
        } catch (Exception e) {
            Log.i( TAG, "Error in ccnumber" + e );
        }
        return null;
    }

    public static String removeLastCharacters(String data, int len) {
        return data.substring( 0, data.length() - len );
    }

    public static String[] substringsBetween(String str, String open, String close) {

        if (str == null || "".equals( open ) || "".equals( close )) {
            return new String[0];
        }
        int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }
        int closeLen = close.length();
        int openLen = open.length();
        List list = new ArrayList();
        int pos = 0;
        while (pos < (strLen - closeLen)) {
            int start = str.indexOf( open, pos );
            int strt = start + openLen;
            int end = str.indexOf( close, strt );
            if (start < 0 || end < 0) {
                break;
            }

            list.add( str.substring( strt, end ) );
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return new String[0];
        }
        return (String[]) list.toArray( new String[list.size()] );
    }

    public static String decimalToHex(int number) {
        return Integer.toHexString( number );
    }
}
