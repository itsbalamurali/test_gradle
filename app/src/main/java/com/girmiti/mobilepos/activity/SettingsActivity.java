package com.girmiti.mobilepos.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.girmiti.mobilepos.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView toolBar;
        ImageView leftArrow;
        LinearLayout linearLayout;
        TransactionFragment preferenceFragment;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);

        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new);
        toolBar = (TextView) linearLayout.findViewById(R.id.tvToolbar_left);
        toolBar.setText(R.string.merchant_settings);

        leftArrow = (ImageView) findViewById(R.id.img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        FragmentManager manager = getFragmentManager();
        preferenceFragment = (TransactionFragment) manager.findFragmentById(R.id.preference_fragment);
        preferenceFragment.setSettingsResource(R.xml.content_settings);
        settingMenu(SettingsActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, SettingsListActivity.class);
        startActivity(intent);
        finish();
    }
}