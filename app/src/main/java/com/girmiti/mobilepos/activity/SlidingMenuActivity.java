package com.girmiti.mobilepos.activity;

/**
 * Created by aravind on 27-04-2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.keypad.KeyPadView;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.UIValidation;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class SlidingMenuActivity extends BaseActivity {
    TextView tvToolbar;
    ImageView menuicon;

    private KeyPadView keyView;
    private SharedPreferences prefs;
    private EditText amount;
    private final Logger logger = getNewLogger(SlidingMenuActivity.class.getName());
    public static final String AMTSEPERATION = "[-+.^:,]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences.Editor editor;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_data);
        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar_new);

        tvToolbar = (TextView) toolbar.findViewById(R.id.tvToolbar);
        menuicon = (ImageView) findViewById(R.id.menuicon);
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        String merchant = prefs.getString(getResources().getString(R.string.key_merchant), "");
        String terminal = prefs.getString(getResources().getString(R.string.key_terminal), "");

        if (merchant == null || merchant.equals("")) {
            editor.putString(getResources().getString(R.string.key_merchant), getResources().getString(R.string.merchant_id));
            editor.commit();
        } else {
            editor.putString(getResources().getString(R.string.key_merchant), merchant);
            editor.commit();
        }
        if (terminal == null || terminal.equals("")) {
            editor.putString(getResources().getString(R.string.key_terminal), getResources().getString(R.string.terminal_id));
            editor.commit();
        } else {
            editor.putString(getResources().getString(R.string.key_terminal), terminal);
            editor.commit();
        }

        editor.commit();
        //Localization support
        getResources().getConfiguration().locale.getLanguage();
        keyView = (KeyPadView) findViewById(R.id.keyPadView);
        amount = (EditText) findViewById(R.id.amount_field);
        keyView.refreshAmount();
        Button proceedBtn = (Button) findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procedBtnValidation();
            }
        });

        settingMenu(SlidingMenuActivity.this);
    }

    private void procedBtnValidation() {
        if (UIValidation.isValidAmount(keyView.getInputText())) {
            tipEnabledorDisabled();
        } else {
            amount.setError(getResources().getString(R.string.enter_amount));

            amount.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Not Performing any operation On Text Changed
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Not Performing any operation Before Text Changed
                }

                @Override
                public void afterTextChanged(Editable s) {

                            amount.setError(null);
                        }
                    });
                    return;
                }
    }

    public void tipEnabledorDisabled()
    {
        try {
            if (prefs != null && prefs.getBoolean(getResources().getString(R.string.key_tip), true)) {
                Intent intent = new Intent(SlidingMenuActivity.this, TipActivity.class);
                Bundle b = new Bundle();
                b.putFloat("sale_amount", Float.valueOf(keyView.getInputText().replaceAll(AMTSEPERATION, "")) / Constants.HUNDRED);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else {
                Intent intent = new Intent(SlidingMenuActivity.this, PaymentListActivity.class);
                intent.putExtra("total_amount", String.valueOf(Float.valueOf(keyView.getInputText().replaceAll(AMTSEPERATION, "")) / Constants.HUNDRED));
                intent.putExtra("tip_amount", "0.00");
                intent.putExtra("sale_amount", String.valueOf(Float.valueOf(keyView.getInputText().replaceAll(AMTSEPERATION, "")) / Constants.HUNDRED));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        } catch (Exception e) {
            logger.severe("Error in enabling and disabling the Tip " +e);
        }
    }

    @Override
    public void onBackPressed() {
        confirmationDialog();
    }


}

