package com.girmiti.mobilepos.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;

public class TransactionSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_transaction_settings);
        TextView tvToolbar;
        ImageView leftArrow;
        FragmentManager manager = getFragmentManager();
        PreferenceFragments preferenceFragment = (PreferenceFragments) manager.findFragmentById( R.id.preference_fragment);
        preferenceFragment.setSettingsResource( R.xml.transaction_settings);
        tvToolbar = (TextView) findViewById( R.id.tvToolbar_left);
        tvToolbar.setText( R.string.transaction_settings);

        leftArrow = (ImageView) findViewById(R.id.img_left_toolbar_arrow);
        leftArrow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionSettings.this.onBackPressed();
            }
        } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TransactionSettings.this, SettingsListActivity.class);
        startActivity(intent);
        finish();
    }
}

