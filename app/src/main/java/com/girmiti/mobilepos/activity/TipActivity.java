package com.girmiti.mobilepos.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.UIValidation;
import com.girmiti.mobilepos.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class TipActivity extends BaseActivity implements View.OnClickListener {

    private TextView tipAmountText;
    private TextView totalAmountText;
    private float saleAmount;
    private float tipAmount = 0;
    private float totalAmount;
    public static final String SALE = "sale_amount";
    public static final String TIP = "tip_amount";
    public static final String TOTAL = "total_amount";
    private final Logger logger = getNewLogger(TipActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TextView saleAmountText;
        Button buttonFirst;
        Button buttonSecond;
        Button buttonThird;
        Button buttonManual;
        Button proceedBtn;
        TextView toolBar;
        ImageView leftArrow;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        toolBar = (TextView) findViewById(R.id.tvToolbar_left);
        toolBar.setText(R.string.tip_name);

        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SALE))
            saleAmount = intent.getExtras().getFloat(SALE);
        logger.info("saleAmount: " + saleAmount);
        saleAmountText = (TextView) findViewById(R.id.saleAmountText);
        saleAmountText.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(saleAmount)));
        tipAmountText = (TextView) findViewById(R.id.tipAmountText);
        totalAmountText = (TextView) findViewById(R.id.totalAmountText);
        totalAmountText.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(saleAmount)));
        tipAmountText.setText(CurrencyFormat.getFormattedCurrency(String.valueOf("0.00")));

        buttonFirst = (Button) findViewById(R.id.buttonFirst);
        buttonFirst.setOnClickListener(this);
        buttonSecond = (Button) findViewById(R.id.buttonSecond);
        buttonSecond.setOnClickListener(this);
        buttonThird = (Button) findViewById(R.id.buttonThird);
        buttonThird.setOnClickListener(this);
        buttonManual = (Button) findViewById(R.id.buttonManual);
        buttonManual.setOnClickListener(this);
        proceedBtn = (Button) findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener(this);
        totalAmount = saleAmount;
        findViewById(R.id.skipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipAmountText.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount("0.00")));
                totalAmountText.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(saleAmount)));
                Intent intent = new Intent(TipActivity.this, PaymentListActivity.class);
                intent.putExtra(SALE, String.valueOf(saleAmount));
                intent.putExtra(TIP, "0.00");
                intent.putExtra(TOTAL, String.valueOf(saleAmount));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.proceedBtn:
                proceedwithTip();
                break;

            case R.id.buttonFirst:
                calculateTip(Constants.FIVE);
                break;

            case R.id.buttonSecond:
                calculateTip(Constants.TEN);
                break;

            case R.id.buttonThird:
                calculateTip(Constants.FIFTEEN);
                break;

            case R.id.buttonManual:
                showManualDialog();
                break;

            default:
                break;
        }
    }

    private void proceedwithTip() {
        Intent intent = new Intent(TipActivity.this, PaymentListActivity.class);
        intent.putExtra(SALE, String.valueOf(saleAmount));
        intent.putExtra(TIP, String.valueOf(tipAmount));
        intent.putExtra(TOTAL, String.valueOf(totalAmount));
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    private void calculateTip(int percent) {

        tipAmount = (saleAmount * percent) / Constants.HUNDRED;
        tipAmountText.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(tipAmount)));
        totalAmount = (saleAmount + tipAmount);
        totalAmountText.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(totalAmount)));
    }

    private void showManualDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.manual_dialog_layout, null);

        builder.setView(layout);

        final EditText manualEditText = (EditText) layout.findViewById(R.id.manualEditText);
        manualEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(Constants.TWO, Constants.TWO)});

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (UIValidation.isValidManualTipAmount(manualEditText.getText().toString()))
                    tipAmount = (saleAmount * Float.parseFloat(manualEditText.getText().toString())) / Constants.HUNDRED;
                tipAmountText.setText(CurrencyFormat.getFormattedCurrency(Utils.getFormattedAmount(tipAmount)));
                totalAmount = (saleAmount + tipAmount);
                totalAmountText.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(totalAmount)));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;
        int digitsBeforeZero;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            this.digitsBeforeZero = digitsBeforeZero;
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches()) {
                if (dest.toString().contains(".")) {
                    if (dest.toString().substring(dest.toString().indexOf('.')).length() > Constants.TWO) {
                        return "";
                    }
                    return null;
                } else if (!Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}").matcher(dest).matches()) {
                    if (!dest.toString().contains(".") && (source.toString().equalsIgnoreCase("."))) {
                        return null;
                    }
                    return "";
                } else {
                    return null;
                }
            }
            return null;
        }
    }
}
