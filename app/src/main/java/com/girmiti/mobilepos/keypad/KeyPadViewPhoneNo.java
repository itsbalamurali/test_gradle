package com.girmiti.mobilepos.keypad;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;

import static com.girmiti.mobilepos.R.id.t9_key_backspace;
import static com.girmiti.mobilepos.R.id.t9_key_clear;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by shrikant on 11/11/16.
 */

public class KeyPadViewPhoneNo extends KeyPadLayout implements View.OnClickListener {

    private EditText txtAccno;
    private Logger logger = getNewLogger(KeyPadViewPhoneNo.class.getName());

    public KeyPadViewPhoneNo(Context context) {
        super(context);
        init();
    }

    public KeyPadViewPhoneNo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyPadViewPhoneNo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keypad_phoneno, this);
        initViews();
    }

    private void initViews() {
        txtAccno = btnpress(R.id.accountNo);
        initializeKeyViews();
        btnpress(t9_key_clear).setOnClickListener(this);
        btnpress(t9_key_backspace).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            try {
                txtAccno.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Not Performing any operation on Text Changed
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Not Performing any operation Before Text Changed
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //Not Performing any operation after Text Changed
                    }
                });
                txtAccno.append(((TextView) v).getText());
            } catch (Exception e) {
                logger.severe("Error while pressing Backspace" +e);
            }
        }
        if (v.getId() == t9_key_clear) {
            try {
                txtAccno.setText("");
            } catch (Exception ex) {
                logger.severe("Error while clearing text" +ex);
            }
        } else if (v.getId() == t9_key_backspace) {
            try {

                Editable editable = txtAccno.getText();
                int charCount = editable.length();
                if (!txtAccno.getText().toString().equals("") && charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                } else {
                    txtAccno.setText("");
                }
            } catch (Exception ex) {
                logger.severe("Error while pressing Backspace" + ex);
            }
        }
    }

    public String getInputText() {
        return txtAccno.getText().toString();
    }

    public void refreshAmount() {
        txtAccno.setText("");
    }

    @Override
    protected <T extends View> T btnpress(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}