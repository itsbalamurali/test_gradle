package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;


public class SplashScreenActivity extends AppCompatActivity {

    private final Logger logger = getNewLogger(SplashScreenActivity.class.getName());
    private static final int MAX_TRANSACTION_COUNT=100;
    public static final int HANDLER_DELAY_TIME = 3000;
    private static final String STARTDATE = "startDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataStoreHelper db;
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = new DataStoreHelper(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy");
        String strDate = sdf.format(c.getTime());
        String[] da = strDate.split(":", Constants.TWO);
        String startDate = da[0];
        String lastDate = prefs.getString(STARTDATE, "");
        try {
            int tranasactionCount = db.retrieveTransactions().size();

            if (tranasactionCount > MAX_TRANSACTION_COUNT && startDate != lastDate && !startDate.equals(lastDate)) {
                        db.deleteAllTransaction();
                        editor.putString(STARTDATE, startDate);
                        editor.apply();
            } else {
                editor.putString(STARTDATE, startDate);
                editor.apply();
            }
        } catch (Exception e) {
            logger.severe("Error in retriving transaction" +e);
        }

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                SplashScreenActivity.this.startActivity( new Intent( SplashScreenActivity.this, LoginActivity.class ) );
                SplashScreenActivity.this.finish();
                SplashScreenActivity.this.overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
            }
        }, HANDLER_DELAY_TIME);
    }
}
