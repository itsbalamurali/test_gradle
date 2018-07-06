/**
 *
 */
package com.girmiti.mobilepos.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.activity.SlidingMenuActivity;
import com.girmiti.mobilepos.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class Utils {

    private static Logger logger = Logger.getNewLogger("com.girmiti.mobilepos.util.Utils");
    public static final String CHARSET_ISO = "iso-8859-1";
    public static final String CHARSET_UTF = "UTF-8";
    public static final String SHA1_HASHING = "SHA-1";
    public static final String ALERT_TITLE = "Alert";
    public static final Boolean DEBUG = true;

    public enum CARD_TYPE {MY_CARDS, DELETE_CARD, SUSPEND_CARD, DEFAULT_CARD}

    public static boolean hasNFCHardware(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature("android.hardware.nfc");
    }

    public static void loadProperties(int resourceId, Properties properties) {
        InputStream rawResource = null;
        try {
            Resources resources = FacadeApplication.getAppContext().getResources();

            rawResource = resources.openRawResource(resourceId);
            properties.load(rawResource);
        } catch (IOException e) {
            logger.info( "Exception :::"+e);
            logger.info("loadProperties, Did not find raw resource: " + resourceId);
        } finally {
            if (rawResource != null)
                try {
                    rawResource.close();
                } catch (IOException e) {
                    logger.severe("Error Closing Inputstream in loadProperties," + "Failed to close " + e);
                }
        }
    }

    public static String getMaskedCardNumber(String cardNumber) {
        if (cardNumber != null && !cardNumber.equals("")) {
            cardNumber =  cardNumber.substring( 0,Constants.FOUR )+ " **** **** " + cardNumber.substring(cardNumber.length() - Constants.FOUR);

            return cardNumber;
        }
        return "";
    }

    public static String getMaskedCardNumberLastFourDigit(String cardNumber) {
        if (!isEmptyString(cardNumber)) {
            cardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - Constants.FOUR);
            return cardNumber;
        }
        return "";
    }

    public static boolean isEmptyString(CharSequence str) {
        return str == null || (str.toString().trim()).length() == 0||str.equals("");
    }

    public static String getPreference(String key) {
        Context context = FacadeApplication.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);
    }

    public static Drawable visaOrMasterCardImage(Context context, String cardNumber) {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_onepay_txn);
        if (cardNumber != null && cardNumber.startsWith("5")) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_onepay_txn);
        }
        return drawable;
    }

    public static String getFormattedAmount(float amount) {
        return String.format("%.2f", amount).replace(",", ".");
    }

    public static String getFormattedAmount(String amount) {
        return amount.replace(",", ".");
    }

    public static void clearTop(Context context) {
        Intent intent = new Intent(context, SlidingMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static String formatAmount(String amount) {

        return amount.replaceAll("[+.^:,]", "");
    }


    public static String currentTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(String.valueOf(R.string.timezone)), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z", Locale.getDefault());
        String localTime = date.format(currentLocalTime);
        logger.severe("DateTester.main(): " + localTime);
        return "GMT" + localTime;
    }

    public static String currentTimeZoneinGmt() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        return tz.getID();
    }
}
