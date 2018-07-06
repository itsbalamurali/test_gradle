package com.girmiti.mobilepos.activity;

/**
 * Created by nayan on 10/10/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;

public class SettingsListActivity extends BaseActivity {

    public TextView tvToolbar;
    private LinearLayout linearLayout;
    private ImageView menuicon;
    protected ListView settingsList;

    String[] itemname ={
            "Change Password",
            "Transaction Settings",
            "Merchant Settings",
            "Device Type"
    };

    Integer[] imgid={
            R.drawable.lock_icon,
            R.drawable.img_transaction,
            R.drawable.img_merchant,
            R.drawable.img_merchant
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.setting_screen );

        linearLayout = (LinearLayout) findViewById( R.id.toolbar_new);
        tvToolbar = (TextView) linearLayout.findViewById( R.id.tvToolbar);
        tvToolbar.setText( R.string.settings_name);

        menuicon = (ImageView) linearLayout.findViewById( R.id.menuicon);
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        settingsList=(ListView)findViewById( R.id.list );
        settingMenu(SettingsListActivity.this);
        initilizeSettingsList();
    }

    private void initilizeSettingsList() {

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        settingsList.setAdapter(adapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentChangePassword = new Intent(SettingsListActivity.this, ChangePasswordActivity.class);
                        startActivity(intentChangePassword);
                        break;
                    case 1:
                        Intent intenttTransactionSettings = new Intent(SettingsListActivity.this, TransactionSettings.class);
                        startActivity(intenttTransactionSettings);
                        break;
                    case 2:
                        Intent intentDeviceSettings = new Intent(SettingsListActivity.this, DeviceSettingsActivity.class);
                        startActivity(intentDeviceSettings);
                        break;
                    case 3:
                        Intent IntentSettings = new Intent(SettingsListActivity.this, SettingsActivity.class);
                        startActivity(IntentSettings);
                }
            }
        });
    }

    private class CustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] itemname;
        private final Integer[] imgid;

        public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
            super(context, R.layout.row_layout, itemname);
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate( R.layout.row_layout, null,true);

            TextView txtTitle = (TextView) rowView.findViewById( R.id.label);
            ImageView imageView = (ImageView) rowView.findViewById( R.id.icon);
            txtTitle.setText(itemname[position]);
            imageView.setImageResource(imgid[position]);
            return rowView;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsListActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
