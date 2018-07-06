package com.girmiti.mobilepos.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.keypad.KeyPadViewPhoneNo;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.LoadFundRequest;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.UIValidation;
import com.girmiti.mobilepos.util.Utils;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by shrikant on 11/11/16.
 */

public class LoadMoneyPhoneNoActivity extends BaseActivity {

    private String accountNo;
    private String amount;
    private ProgressDialog progressDialog;
    private KeyPadViewPhoneNo keyView;
    private SharedPreferences prefs;
    private EditText accountNumberEditText;
    private Logger logger = getNewLogger(LoadMoneyPhoneNoActivity.class.getName());
    public static final String DATETIMEFORMAT = "dd/MM/yyyy   hh:mm:ss a";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ImageView leftArrow;
        TextView tvToolbar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_money_phoneno);
        tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.load_money);
        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        Button proceedBtn = (Button) findViewById(R.id.proceedBtn);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        keyView = (KeyPadViewPhoneNo) findViewById(R.id.keyPadView);
        accountNumberEditText = (EditText) findViewById(R.id.accountNo);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIValidation.isValidAccountNumber(keyView.getInputText())) {
                    try {
                        accountNo = keyView.getInputText();

                        new LoadMoneyRequest().execute();
                    } catch (Exception e) {
                        logger.severe("Error in AccountNumber Input" + e);
                    }
                } else {
                    initilizeAmmoutField();
                }
            }
        });
    }

   private void  initilizeAmmoutField() {

       accountNumberEditText.setError(getResources().getString(R.string.enter_account_no));
       accountNumberEditText.addTextChangedListener(new TextWatcher() {
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
               accountNumberEditText.setError(null);
           }
       });
   }


    public class LoadMoneyRequest extends AsyncTask<String, String, Response> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                LoadFundRequest loadFundRequest = new LoadFundRequest();
                loadFundRequest.setEntryMode("MANUAL");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String merchant = sharedPreferences.getString("merchantId", "");
                if (merchant == null) {
                    loadFundRequest.setMerchantId(getResources().getString(R.string.merchant_id));
                } else {
                    loadFundRequest.setMerchantId(sharedPreferences.getString("merchantId", ""));
                }
                String terminal = sharedPreferences.getString("terminalId", "");
                if (terminal == null) {
                    loadFundRequest.setTerminalId(getResources().getString(R.string.terminal_id));
                } else {
                    loadFundRequest.setTerminalId(sharedPreferences.getString("terminalId", ""));
                }

                loadFundRequest.setAccountNumber(accountNo);
                loadFundRequest.setTotalTxnAmount((long) Float.parseFloat(Utils.formatAmount(amount)));
                loadFundRequest.setTxnType("LOAD_FUND");
                loadFundRequest.setTimeZoneOffset(Utils.currentTimeZone());
                loadFundRequest.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
                return ServerCommunication.getInstance().processLoadMoney(loadFundRequest);
            } catch (Exception e) {
                logger.severe("Error in Loading Funds" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            progressDialog.dismiss();

            if (response != null && response.getErrorCode() != null && response.getErrorCode().equals("0")) {
                saveData(response);
                showConfirmDialog(response);
            } else if (response != null && response.getErrorMessage() != null) {
                Toast.makeText(getApplicationContext(), response.getErrorMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        }

        private void saveData(Response response) {

            String txnRefNumber = response.getTxnRefNumber();
            Transaction tran = new Transaction();
            tran.setTransactionamount(amount);
            tran.setTransactionType(getResources().getString(R.string.load_fund));
            tran.setCardHolderName(accountNo);
            tran.setStatus(getResources().getString(R.string.approved));
            tran.setTxnRefNumber(txnRefNumber);
            tran.setTotalamount(amount);

            tran.setTxnDateTime(String.valueOf(System.currentTimeMillis()));

            DataStoreHelper helper = new DataStoreHelper(LoadMoneyPhoneNoActivity.this);
            try {
                helper.addTransaction(tran);
            } catch (Exception e) {
                logger.severe("Error in saving Transaction to History" + e);
            }
        }

        private void showConfirmDialog(Response responseObj) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoadMoneyPhoneNoActivity.this);

            View view = getLayoutInflater().inflate(R.layout.load_fund_result_dialog, null);
            ((TextView) view.findViewById(R.id.errorCode)).setText(responseObj.getErrorCode());
            ((TextView) view.findViewById(R.id.status)).setText(responseObj.getErrorMessage());
            ((TextView) view.findViewById(R.id.txnDateTime)).setText(responseObj.getDeviceLocalTxnTime());
            ((TextView) view.findViewById(R.id.txnRefNumber)).setText(responseObj.getTxnRefNumber());
            ((TextView) view.findViewById(R.id.merchantCode)).setText(prefs.getString(getResources().getString(R.string.key_merchant), ""));
            ((TextView) view.findViewById(R.id.customerNo)).setText(accountNo);
            ((TextView) view.findViewById(R.id.totAmount)).setText(CurrencyFormat.getCurrencyCodeAlplha() + " " + amount);

            builder.setCancelable(false);
            builder.setView(view)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            startActivity(new Intent(LoadMoneyPhoneNoActivity.this, SlidingMenuActivity.class));
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }


    }
}