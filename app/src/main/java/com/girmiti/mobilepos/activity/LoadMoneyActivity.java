package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.keypad.KeyPadView;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.util.UIValidation;
import com.girmiti.mobilepos.util.Utils;

import static com.girmiti.mobilepos.R.id.menuicon;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 7/11/16.
 */

public class LoadMoneyActivity extends BaseActivity {

    private String amount;
    private KeyPadView keyView;
    private EditText amountEditText;
    private Logger logger = getNewLogger(LoadMoneyActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TextView toolBar;
        ImageView menuIcn;
        LinearLayout linearLayout;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_money);

        linearLayout = (LinearLayout) findViewById(R.id.toolbar_new);
        toolBar = (TextView) linearLayout.findViewById(R.id.tvToolbar);
        toolBar.setText(R.string.load_money);
        menuIcn = (ImageView) linearLayout.findViewById(menuicon);
        menuIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        keyView = (KeyPadView) findViewById(R.id.keyPadView);
        amountEditText = (EditText) findViewById(R.id.amount_field);
        keyView.refreshAmount();

        findViewById(R.id.proceedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIValidation.isValidAmount(keyView.getInputText())) {
                    try {
                        amount = keyView.getInputText();
                        Intent intent = new Intent(LoadMoneyActivity.this, LoadMoneyPhoneNoActivity.class);
                        intent.putExtra("amount", amount);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        logger.severe("Error in KeyPad Input" + e);
                    }
                } else {
                    amountFieldListener();
                }
            }
        });
        settingMenu(LoadMoneyActivity.this);
    }

    public void amountFieldListener()
    {
        amountEditText.setError(getResources().getString(R.string.enter_amount));
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not Performing any operation On Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                amountEditText.setError(null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.clearTop(this);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }
}