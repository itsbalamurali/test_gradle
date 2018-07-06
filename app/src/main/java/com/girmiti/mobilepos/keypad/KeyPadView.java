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
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import static com.girmiti.mobilepos.R.id.t9_key_backspace;
import static com.girmiti.mobilepos.R.id.t9_key_clear;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class KeyPadView extends KeyPadLayout implements View.OnClickListener {

    private EditText amount;
    private Logger logger = getNewLogger(KeyPadView.class.getName());
    private String formatted;
    private String cleanString;
    int charCount;
    Context context;

    public KeyPadView(Context context) {
        super(context);
        init();
        this.context = context;
    }

    public KeyPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keypad, this);
        initViews();
    }

    private void initViews() {
        TextView currencySymbol;
        amount = btnpress(R.id.amount_field);
        currencySymbol = btnpress(R.id.tvCurrency);
        currencySymbol.setText(CurrencyFormat.getCurrencyCodeAlplha());
        initializeKeyViews();
        btnpress(t9_key_clear).setOnClickListener(this);
        btnpress(R.id.t9_key_backspace).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            Editable editable = amount.getText();
            charCount = editable.length();
            numberKeysListener(v);
        }

        if (v.getId() == t9_key_clear) {
            keyClear();
        } else if (v.getId() == t9_key_backspace) {
            keyBackspace();
        }
    }

    private void numberKeysListener(View v) {
        try {
            if (charCount > 0) {
                amountTextListners();
                amount.append(((TextView) v).getText());

            }
        } catch (Exception e) {
            logger.severe("Error occured while pressing Button" + e);
        }
    }

    private void amountTextListners() {
        amount.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    amount.removeTextChangedListener(this);
                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    cleanString = s.toString().replaceAll(replaceable, "");
                    formatAmount();
                    current = formatted;
                    try {
                        amount.setText(formatted);
                        amount.setSelection(formatted.length());
                        amount.addTextChangedListener(this);
                    } catch (StackOverflowError e) {
                        logger.severe("Stack overflow Error" + e);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not Performing any operation after Text |Changed
            }
        });

    }

    private void keyClear() {
        try {
            amount.setText("0.00");
        } catch (Exception e) {
            logger.severe("Error while clearing text" + e);
        }
    }

    private void keyBackspace() {
        try {
            Editable editable = amount.getText();
            charCount = editable.length();
            if (!amount.getText().toString().equals("0.00") && charCount > 0) {
                editable.delete(charCount - 1, charCount);
            } else {
                amount.setText("0.00");
            }
        } catch (Exception e) {
            logger.severe("Error while pressing Backspace" + e);
        }

    }

    private void formatAmount() {
        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }
        double da = parsed / Constants.HUNDRED;
        formatted = Utils.getFormattedAmount((float) da);
        if (formatted.length() > Constants.FOUR) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(CurrencyFormat.getCurrencyCodeMinorUnit().charAt(0));
            symbols.setGroupingSeparator(CurrencyFormat.getCurrencyCodeThousandUnit().charAt(0));
            DecimalFormat formatter = new DecimalFormat("#.00", symbols);
            formatter.setGroupingUsed(true);
            formatter.setGroupingSize(Integer.parseInt(CurrencyFormat.getCurrencySeparater()));
            formatted = formatter.format(da);

        }
    }

    public String getInputText() {
        return amount.getText().toString();
    }

    public void refreshAmount() {
        amount.setText("0.00");
    }

    protected <T extends View> T btnpress(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}

