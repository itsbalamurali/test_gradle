package com.girmiti.mobilepos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.UIValidation;
import com.girmiti.mobilepos.util.Utils;

import java.util.Calendar;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class ManualEntryActivity extends BaseActivity {

    private final Logger logger = getNewLogger(ManualEntryActivity.class.getName());
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private String txnAmount;
    private String tipAmount;
    private String cardNumber;
    private String saleAmount;
    private EditText panEditText1;
    private EditText panEditText2;
    private EditText panEditText3;
    private EditText panEditText4;
    private EditText expDateEditText;
    private EditText cardHolderName;
    private EditText cvvEditText;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        ImageView leftArrow;
        TextView tvToolbar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.manual_entry);

        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            txnAmount = intent.getStringExtra("total_amount");
            tipAmount = intent.getStringExtra("tip_amount");
            saleAmount = intent.getStringExtra("sale_amount");
        }

        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();

        panEditText1 = (EditText) findViewById(R.id.panEditText1);
        panEditText2 = (EditText) findViewById(R.id.panEditText2);
        panEditText3 = (EditText) findViewById(R.id.panEditText3);
        panEditText4 = (EditText) findViewById(R.id.panEditText4);

        expDateEditText = (EditText) findViewById(R.id.expDateEditText);
        cardHolderName = (EditText) findViewById(R.id.cardHolderName);
        cvvEditText = (EditText) findViewById(R.id.cvvEditText);
        findViewById(R.id.proceedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initialize();
            }
        });

        addTextListeners();
    }

    private void initialize() {
        String entryMode;
        if (validate()) {
            confirmTransactionDetailsDTO.setTransactionAmount(Utils.getFormattedAmount(saleAmount));
            confirmTransactionDetailsDTO.setAmount(Utils.getFormattedAmount(txnAmount));
            confirmTransactionDetailsDTO.setTipAmount(Utils.getFormattedAmount(tipAmount));
            confirmTransactionDetailsDTO.setCardholderName(cardHolderName.getText().toString().trim());
            confirmTransactionDetailsDTO.setCardNumber(cardNumber);
            confirmTransactionDetailsDTO.setExpDate(expDateEditText.getText().toString().trim());
            confirmTransactionDetailsDTO.setEmvField55("");
            confirmTransactionDetailsDTO.setTrack2("");
            confirmTransactionDetailsDTO.setCvv(cvvEditText.getText().toString().trim());
            confirmTransactionDetailsDTO.setCardType(CommonUtils.getCCType(cardNumber));
            entryMode = getString( R.string.manual_type );
            confirmTransactionDetailsDTO.setEntryMode(entryMode);
            showConfirmDialog( confirmTransactionDetailsDTO);
        }
    }

    private void showConfirmDialog(final ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO) {

        LayoutInflater inflater = ManualEntryActivity.this.getLayoutInflater();
        View view = inflater.inflate( R.layout.manual_entry_confirm_dialog, null);
        ((TextView) view.findViewById( R.id.card_number)).setText(com.girmiti.mobilepos.util.Utils.getMaskedCardNumber(confirmTransactionDetailsDTO.getCardNumber()));
        ((TextView) view.findViewById( R.id.exp_date)).setText(expDateEditText.getText().toString().trim());
        ((TextView) view.findViewById( R.id.card_holder)).setText(confirmTransactionDetailsDTO.getCardholderName());
        ((TextView) view.findViewById( R.id.amount)).setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(confirmTransactionDetailsDTO.getAmount())));

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ManualEntryActivity.this);
        builder.setView(view)
                .setPositiveButton( R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ManualEntryActivity.this, TransactionResultActivity.class);
                        intent.putExtra(getString(R.string.transaction_details), confirmTransactionDetailsDTO);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    }
                })
                .setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        clearEditTexts();
                    }
                });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearEditTexts();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private boolean validate() {

        cardNumber = panEditText1.getText().toString().trim() + panEditText2.getText().toString().trim()
                + panEditText3.getText().toString().trim() + panEditText4.getText().toString().trim();

        if (!UIValidation.isPanNotEmpty(panEditText1.getText().toString())) {
            panEditText1.setError(getResources().getString(R.string.pan_validation));
            return false;
        }
        if (!UIValidation.isPanNotEmpty(panEditText2.getText().toString())) {
            panEditText2.setError(getResources().getString(R.string.pan_validation));
            return false;
        }
        if (!UIValidation.isPanNotEmpty(panEditText3.getText().toString())) {
            panEditText3.setError(getResources().getString(R.string.pan_validation));
            return false;
        }
        if (!UIValidation.isPanNotEmpty(panEditText4.getText().toString())) {
            panEditText4.setError(getResources().getString(R.string.pan_validation));
            return false;
        }
        if (!UIValidation.isPanValid(cardNumber)) {
            Toast.makeText(this, getString(R.string.error_cc), Toast.LENGTH_SHORT).show();
            return false;
        }

        String expDate = expDateEditText.getText().toString();
        if (!UIValidation.isExpDateNotEmpty(expDate)) {

            expDateEditText.setError(getResources().getString(R.string.exp_validation));
            return false;
        }

        String cvv = cvvEditText.getText().toString();
        if (!UIValidation.isCVVEmpty(cvv)) {
            cvvEditText.setError(getResources().getString(R.string.cvv_validation));
            return false;
        }

        int calYear = Calendar.getInstance().get(Calendar.YEAR);
        // Extract month and year
        String exp = expDate.trim();
        String month = exp.substring(Constants.TWO);
        String year = exp.substring(0, Constants.TWO);
        int m = 0;
        int y = 0;
        try {
            m = Integer.valueOf(month);
            y = Integer.valueOf(year);
        } catch (Exception exception) {
            logger.info("Exception :::" + exception);
            Toast.makeText(this, getString(R.string.error_exp), Toast.LENGTH_SHORT).show();
            return false;
        }

        calYear = Integer.valueOf((Integer.toString(calYear)).substring(Constants.TWO));
        if (m > Constants.TOTAL_MONTH || y < calYear) {
            Toast.makeText(this, getString(R.string.error_exp), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (UIValidation.isCardHolderNameNotEmpty(cardHolderName.getText().toString())) {
            return true;
        } else {
            cardHolderName.setError(getResources().getString(R.string.name_validation));
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    private void addTextListeners() {

        panEditText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == Constants.FOUR) {
                    panEditText2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not Performing any operation On Text Changed
            }
        });

        panEditText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not Performing On operation Before Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == Constants.FOUR) {
                    panEditText3.requestFocus();
                } else if (s == null || s.length() == 0) {
                    panEditText1.requestFocus(View.FOCUS_LEFT);
                }
            }
        });

        panEditText3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not Performing On operation Before Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                initilizePanEditText3(s);
            }
        });

        panEditText4.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Performing any operation Before Text Changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not Performing any operation on Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                initilizePanEditText4(s);
            }
        });

        expDateEditText.addTextChangedListener(new GenericTextWatcher(expDateEditText));
        cardHolderName.addTextChangedListener(new GenericTextWatcher(cardHolderName));
    }

    private void initilizePanEditText3(Editable s) {
        if (s != null && s.length() == Constants.FOUR) {
            panEditText4.requestFocus();
        } else if (s == null || s.length() == 0) {
            if (panEditText2.getText().toString() == null ||
                    panEditText2.getText().toString().equals("") ||
                    panEditText2.getText().toString().length() == 0) {
                panEditText1.requestFocus(View.FOCUS_LEFT);
            } else {
                panEditText2.requestFocus(View.FOCUS_LEFT);
            }
        }
    }

    private void initilizePanEditText4(Editable s) {
        if (s != null && s.length() == Constants.FOUR) {
            expDateEditText.requestFocus();
        } else if (s == null || s.length() == 0) {
            if (panEditText3.getText().toString().equals("") || panEditText3.getText().toString() == null || panEditText3.length() == 0) {
                panEditText2.requestFocus(View.FOCUS_LEFT);
            } else if ((panEditText3.getText().toString().equals("") || panEditText3.getText().toString() == null || panEditText3.length() == 0) && (panEditText2.getText().toString() == null || panEditText2.getText().toString().equals("") || panEditText2.getText().toString().length() == 0)) {
                panEditText1.requestFocus(View.FOCUS_LEFT);
            } else {
                panEditText3.requestFocus(View.FOCUS_LEFT);
            }
        }
    }

    private void clearEditTexts() {

        panEditText1.setText("");
        panEditText2.setText("");
        panEditText3.setText("");
        panEditText4.setText("");
        expDateEditText.setText("");
        cardHolderName.setText("");
        panEditText1.requestFocus();
        cvvEditText.setText("");
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Not Performing any operation Before Text Changed
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Not Performing any operation On Text Changed
        }

        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.expDateEditText:
                    requestFocus(s);
                    break;
                case R.id.cardHolderName:
                    break;
                default:
                    break;
            }
        }
        private void requestFocus(Editable s) {
            if (s != null && s.length() == Constants.FOUR) {
                cvvEditText.requestFocus();
            }
        }
    }
}