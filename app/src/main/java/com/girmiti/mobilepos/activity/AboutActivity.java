package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.util.Constants;

import java.io.IOException;
import java.io.InputStream;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class AboutActivity extends BaseActivity {
    TextView tvToolbar;
    SharedPreferences prefs;
    LinearLayout linearLayout;
    ImageView menuicon;
    WebView webView;
    private Logger logger = getNewLogger(AboutActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.content_about);
        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new);
        tvToolbar = (TextView) linearLayout.findViewById(R.id.tvToolbar);
        tvToolbar.setText(R.string.about_name);
        menuicon = (ImageView) linearLayout.findViewById(R.id.menuicon);
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        webView = (WebView) findViewById(R.id.about_view);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String str = getResources().getConfiguration().locale.getLanguage();

        if ((Constants.ES).equals(str)) {
            try {
                loadAboutScreen(getAssets().open("aboutEs.html"));
            } catch (IOException e) {
                logger.info("Error in reading aboutEs file" + e);
            }
        } else {
            try {
                loadAboutScreen(getAssets().open("about.html"));
            } catch (IOException e) {
                logger.info("Error in reading about file" + e);
            }
        }
    }

    public void loadAboutScreen(InputStream is) {
        String versionNo = prefs.getString("versionnumber", "");
        String versionNumber = null;
        StringBuilder stringbuilder = null;
        try {
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            int data = is.read(buffer);
            logger.info("the no of bytes received is" +data);
            is.close();
            versionNumber = new String(buffer);
            stringbuilder = new StringBuilder(versionNumber);
            stringbuilder.replace(Constants.SIXTEEN, Constants.NINETEEN, versionNo);
            versionNumber = String.valueOf(stringbuilder);

            } catch (IOException e) {
                logger.severe("Input Output Exception: " +e);
            }
            webView.loadDataWithBaseURL("file:///android_asset/", versionNumber, "text/html", "UTF-8", null);
        settingMenu(AboutActivity.this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
