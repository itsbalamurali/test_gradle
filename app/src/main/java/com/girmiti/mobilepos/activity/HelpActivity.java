package com.girmiti.mobilepos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView toolBar;
        LinearLayout linearLayout;
        ImageView menuIcn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_help);
        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new);
        toolBar = (TextView) linearLayout.findViewById(R.id.tvToolbar);
        toolBar.setText(R.string.help_name);

        menuIcn = (ImageView) linearLayout.findViewById(R.id.menuicon);
        menuIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        //help list
        String[] items = getResources().getStringArray(R.array.help_items);
        ListView listView = (ListView) findViewById(R.id.items);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    String number = getString(R.string.agent_phone_number);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(HelpActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        });
        settingMenu(HelpActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HelpActivity.this, SlidingMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
