package com.girmiti.mobilepos.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.girmiti.mobilepos.R;


/**
 * Author Girmiti Software
 * Created on  8/12/17.
 */
public class DeviceSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView toolBar;
        ImageView leftArrow;
        LinearLayout linearLayout;
        DeviceSettingFragment preferenceFragment;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);

        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new_left);
        toolBar = (TextView) linearLayout.findViewById(R.id.tvToolbar_left);
        toolBar.setText(R.string.device_type_settings);

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
        preferenceFragment = (DeviceSettingFragment) manager.findFragmentById(R.id.device_preference_fragment);
        preferenceFragment.setSettingsResource(R.xml.device_type_settings);
        settingMenu(DeviceSettingsActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DeviceSettingsActivity.this, SettingsListActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}
