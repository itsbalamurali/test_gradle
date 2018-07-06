package com.girmiti.mobilepos.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.RefundRequest;
import com.girmiti.mobilepos.net.model.Request;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.net.model.VoidRequest;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;

import java.util.Calendar;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by shrikant on 5/11/16.
 */

public class TransactionHistoryDetails extends BaseActivity {

    private Logger logger = getNewLogger(TransactionHistoryDetails.class.getName());
    private SharedPreferences prefs;
    private String terminal;
    private String merchant;
    private String actualTransactionReference;
    private Transaction transaction = null;
    private ProgressDialog progressDialog;
    private static final String REFUNDAMT = "REFUND";
    public static final String DATETIMEFORMAT = "dd/MM/yyyy   hh:mm:ss a";
    private TextView txtAddress;
    private TextView txtCity;
    private TextView txtState;
    private TextView txtTranNo;
    private TextView txtRefNo;
    private TextView txtTerminalId;
    private TextView txtMerchantId;
    private TextView txtMerchantName;
    private TextView txtAuthCode;
    private TextView txtTotalAmount;
    private TextView txtDateTime;
    private TextView txtStatus;
    private TextView txtType;
    private TextView txtTip;
    private TextView txtTranxnAmount;
    private String city;
    private String state;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.transaction_details_name);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        merchant = prefs.getString(getResources().getString(R.string.key_merchant), "");
        terminal = prefs.getString(getResources().getString(R.string.key_terminal), "");
        txtTranxnAmount = (TextView) findViewById(R.id.txtTranAmount);
        txtTranNo = (TextView) findViewById(R.id.txtTranNo);
        txtRefNo = (TextView) findViewById(R.id.txtRefNo);
        txtTip = (TextView) findViewById(R.id.txtTip);
        txtTerminalId = (TextView) findViewById(R.id.txtTerminalId);
        txtMerchantId = (TextView) findViewById(R.id.txtMerchantId);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        txtAuthCode = (TextView) findViewById(R.id.txtAuthCode);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtType = (TextView) findViewById(R.id.txtType);
        TextView txtAvalBalance = (TextView) findViewById(R.id.aval_balance);
        TextView accountNumber = (TextView) findViewById(R.id.Account_Number);
        TextView  txtMaskedCardNumber = (TextView) findViewById(R.id.txtmasked_card_number);
        TextView  txtCardHolderName = (TextView) findViewById(R.id.card_holder_name);
        RelativeLayout cgRefNo = (RelativeLayout) findViewById(R.id.cgRefNo);
        RelativeLayout authCode = (RelativeLayout) findViewById(R.id.authCode);
        RelativeLayout tip = (RelativeLayout) findViewById(R.id.tip);
        RelativeLayout tranAmount=(RelativeLayout) findViewById( R.id.tranaAmount );
        RelativeLayout rlLoadMoney = (RelativeLayout) findViewById(R.id.rlLoadMoney);
        RelativeLayout availBalance=(RelativeLayout) findViewById( R.id.availBalance );
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtState = (TextView) findViewById(R.id.txtState);

        ImageView leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistoryDetails.this, TransactionHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        Button voidBtn = (Button) findViewById(R.id.voidBtn);
        Button refundBtn = (Button) findViewById(R.id.refundBtn);
        Button backBtn = (Button)findViewById( R.id.backBtn );
        LinearLayout llVoidRefund = (LinearLayout) findViewById(R.id.llVoidRefund);
        LinearLayout llOkSkip = (LinearLayout) findViewById(R.id.llOkSkip);
        llOkSkip.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        address = prefs.getString(getResources().getString(R.string.key_address), "");
        city = prefs.getString(getResources().getString(R.string.key_city), "");
        state = prefs.getString(getResources().getString(R.string.key_state), "");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            transaction = (Transaction) b.getSerializable("transaction");
        }

        if (transaction == null) {
            Toast.makeText(this, getString(R.string.no_transactions), Toast.LENGTH_SHORT).show();
            return;
        }

        if ((transaction.getTransactionType().contains(getResources().getString(R.string.sale)) || transaction.getTransactionType().contains("VENTA")) && !(transaction.getStatus().equalsIgnoreCase(getResources().getString(R.string.voided))
                || transaction.getStatus().equalsIgnoreCase(getResources().getString(R.string.refunded))) && transaction.getStatus().equalsIgnoreCase( "approved" )) {
            llVoidRefund.setVisibility(View.VISIBLE);
        } else if (transaction.getTransactionType().equalsIgnoreCase("VOID")) {
            llVoidRefund.setVisibility(View.GONE);
        } else {
            llVoidRefund.setVisibility(View.GONE);
        }

        if (transaction.getTransactionType().equalsIgnoreCase(getResources().getString(R.string.load_fund))) {
            cgRefNo.setVisibility(View.GONE);
            authCode.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            accountNumber.setText(transaction.getCardHolderName());
            rlLoadMoney.setVisibility(View.VISIBLE);
        } else if(transaction.getTransactionType().equalsIgnoreCase(getResources().getString(R.string.balance_inquiry))){
            cgRefNo.setVisibility(View.GONE);
            authCode.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            tranAmount.setVisibility( View.GONE );
            availBalance.setVisibility( View.VISIBLE );
            txtAvalBalance.setText( CurrencyFormat.getFormattedCurrency(transaction.getTransactionamount()) );
            txtCardHolderName.setText(transaction.getCardHolderName());
            txtMaskedCardNumber.setText(transaction.getMaskedCardNumber());

        } else {
            cgRefNo.setVisibility(View.VISIBLE);
            authCode.setVisibility(View.VISIBLE);
            tip.setVisibility(View.VISIBLE);
            txtCardHolderName.setText(transaction.getCardHolderName());
            txtMaskedCardNumber.setText(transaction.getMaskedCardNumber());
        }

        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        txnHistroyInitilization(transaction);
        voidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VoidTransaction().execute();
            }
        });
        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog( transaction );
            }
        });

    }

    private void txnHistroyInitilization(Transaction transaction) {

        actualTransactionReference = transaction.getTxnRefNumber();
        txtAddress.setText(address);
        txtCity.setText(city);
        txtState.setText(state);
        txtTranNo.setText(transaction.getTxnRefNumber());
        txtRefNo.setText(transaction.getCgRefNumber());
        txtAuthCode.setText(transaction.getAuthId());
        txtStatus.setText(transaction.getStatus());
        txtType.setText(transaction.getTransactionType());
        if (transaction.getTransactionType() .equals( "" ))
            txtTranxnAmount.setText(transaction.getTransactionamount());

        if (!Utils.isEmptyString(transaction.getTipAmount())) {
            txtTip.setText(CurrencyFormat.getFormattedCurrency(Utils.getFormattedAmount(transaction.getTipAmount())));
        } else {
            txtTip.setText(CurrencyFormat.getFormattedCurrency(getString( R.string.default_amount )));
        }

        if (!Utils.isEmptyString(transaction.getMerchantName())) {
            txtMerchantName.setText(transaction.getMerchantName());
        } else {
            findViewById(R.id.txtMerchantName).setVisibility(View.GONE);
        }
        txtDateTime.setText(transaction.getTxnDateTime());

        txtTotalAmount.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(Double.valueOf(Utils.formatAmount(Utils.getFormattedAmount(transaction.getTotalamount()))) / Constants.HUNDRED)));
        txtTranxnAmount.setText(CurrencyFormat.getFormattedCurrency(String.valueOf(Double.valueOf(Utils.formatAmount(Utils.getFormattedAmount(transaction.getTransactionamount()))) / Constants.HUNDRED)));

        try {
            txtMerchantId.setText(merchant);
            txtTerminalId.setText(terminal);
        } catch (Exception e) {
            logger.severe("Failed in setting MerchantId and TerminalID" + e);
        }
    }

    private class VoidTransaction extends AsyncTask<String, String, Response> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Response doInBackground(String... params) {

            VoidRequest request = new VoidRequest();
            transactionData(request);
            request.setTxnRefNumber(transaction.getTxnRefNumber());
            request.setCgRefNumber(transaction.getCgRefNumber());
            request.setTimeZoneOffset(Utils.currentTimeZone());
            request.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
            try {
                return ServerCommunication.getInstance().processVoid(request);
            } catch (Exception e) {
                logger.severe("Error in void" + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            if (progressDialog != null)
                progressDialog.dismiss();
            progressDialog = null;

            if (response != null && response.getErrorMessage() != null && response.getErrorCode().equals("00")) {

                try {
                    saveData(response, "VOID");
                    updateData(response, "VOID");
                } catch (Exception e) {
                    logger.severe("Failure while saving or Updating data" + e);
                    Toast.makeText(TransactionHistoryDetails.this, getResources().getString(R.string.error_transaction), Toast.LENGTH_SHORT).show();
                }
                showConfirmDialog(response);

            } else if (response != null && response.getErrorMessage() != null) {
                showConfirmDialog(response);
            } else {
                showDialog(getResources().getString(R.string.something_wrong));
            }
        }
    }

    private void showConfirmDialog(Response responseObj) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.transaction_result_dialog, null);
        ((TextView) view.findViewById(R.id.errorCode)).setText(responseObj.getErrorCode());
        ((TextView) view.findViewById(R.id.status)).setText(responseObj.getErrorMessage());

        Calendar cal = Calendar.getInstance();

        if (responseObj.getDeviceLocalTxnTime() != null)
            cal.setTimeInMillis(Long.valueOf(responseObj.getDeviceLocalTxnTime()));
        String date = (DateFormat.format(DATETIMEFORMAT, cal.getTime()).toString());

        ((TextView) view.findViewById(R.id.txnDateTime)).setText(date);
        ((TextView) view.findViewById(R.id.txnRefNumber)).setText(responseObj.getTxnRefNumber());
        ((TextView) view.findViewById(R.id.authId)).setText(responseObj.getAuthId());
        ((TextView) view.findViewById(R.id.cgRefNumber)).setText(responseObj.getCgRefNumber());
        ((TextView) view.findViewById(R.id.merchantCode)).setText(transaction.getMerchantCode());
        ((TextView) view.findViewById(R.id.merchantName)).setText(transaction.getMerchantName());

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showConfirmDialog(Transaction transaction) {

        // Get the layout inflater
        LayoutInflater inflater = TransactionHistoryDetails.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_transaction_dialog, null);
           ((TextView) view.findViewById(R.id.title)).setText(getString( R.string.refund_detail ));
            ((TextView) view.findViewById(R.id.card_number)).setText(transaction.getMaskedCardNumber());
            ((TextView) view.findViewById(R.id.exp_date)).setText(transaction.getExpDate());
            ((TextView) view.findViewById(R.id.amount)).setText(CurrencyFormat.getFormattedCurrency(Utils.getFormattedAmount(transaction.getTransactionamount())));
            ((TextView) view.findViewById(R.id.card_holder)).setText(transaction.getCardHolderName());


        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionHistoryDetails.this);
        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new RefundTransaction().execute();
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private class RefundTransaction extends AsyncTask<String, String, Response> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Response doInBackground(String... params) {

            RefundRequest request = new RefundRequest();

            transactionData(request);
            request.setTxnRefNumber(transaction.getTxnRefNumber());
            request.setCgRefNumber(transaction.getCgRefNumber());
            request.setTimeZoneOffset(Utils.currentTimeZone());
            request.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
            try {
                return ServerCommunication.getInstance().processRefund(request);
            } catch (Exception e) {
                logger.severe("Error in Progressing Refund" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (progressDialog != null)
                progressDialog.dismiss();
            progressDialog = null;

            if (response != null && response.getErrorMessage() != null && response.getErrorCode().equals("00")) {

                try {
                    saveData(response, REFUNDAMT);
                    updateData(response, REFUNDAMT);
                    Intent intent = new Intent(TransactionHistoryDetails.this, RefundReceiptActivity.class);
                    Bundle b = new Bundle();
                    transaction.setTransactionType( getString( R.string.refund_name ) );
                    b.putSerializable("transaction", transaction);
                    b.putSerializable("response", response);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    logger.severe("Error in save and update refund data" + e);
                    Toast.makeText(TransactionHistoryDetails.this, getResources().getString(R.string.error_transaction), Toast.LENGTH_SHORT).show();
                }

            } else if (response != null && response.getErrorMessage() != null && response.getErrorCode().equals("TXN_0113")) {
                showDialog(getResources().getString(R.string.transaction_processing_state));
            } else if (response != null && response.getErrorMessage() != null && response.getErrorMessage().equals("Invalid Transaction Id or transaction already voided ") && response.getErrorCode().equals("TXN_0103")) {
                showDialog(getResources().getString(R.string.transaction_refund_state));
            } else {
                showDialog(getResources().getString(R.string.something_wrong));
            }
        }
    }

    private void transactionData(Request request) {
        if (merchant == null || merchant.equals("")) {
            request.setMerchantId(getResources().getString(R.string.merchant_id));
        } else {
            request.setMerchantId(prefs.getString(getResources().getString(R.string.key_merchant), ""));
        }

        if (terminal == null || terminal.equals("")) {
            request.setTerminalId(getResources().getString(R.string.terminal_id));
        } else {
            request.setTerminalId(prefs.getString(getResources().getString(R.string.key_terminal), ""));
        }
    }

    private void showDialog(String errorMessage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        if (errorMessage.equalsIgnoreCase(getResources().getString(R.string.transaction_processing_state))) {
            builder.setTitle(getResources().getString(R.string.warning));
        } else {
            builder.setTitle(getResources().getString(R.string.error));
        }
        builder.setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

        android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void saveData(Response response, String type) throws GenericException {
        DataStoreHelper helper = new DataStoreHelper(this);

        Transaction trans = new Transaction();

        trans.setTransactionfee("0");
        trans.setCgRefNumber(response.getCgRefNumber());
        trans.setTxnDateTime(response.getDeviceLocalTxnTime());
        trans.setTxnRefNumber(response.getTxnRefNumber());
        trans.setAuthId(response.getAuthId());
        trans.setStatus(response.getErrorMessage());
        trans.setMerchantCode(transaction.getMerchantCode());
        trans.setTransactionType(type);
        trans.setTotalamount(transaction.getTotalamount());
        trans.setCardHolderName(transaction.getCardHolderName());
        trans.setMaskedCardNumber(transaction.getMaskedCardNumber());
        trans.setMerchantName(transaction.getMerchantName());
        trans.setTipAmount(transaction.getTipAmount());
        trans.setTransactionamount(transaction.getTransactionamount());
        helper.addTransaction(trans);
    }

    private void updateData(Response response, String type) throws GenericException {
        DataStoreHelper helper = new DataStoreHelper(this);
        Transaction trans = new Transaction();
        if (response.getErrorMessage().equalsIgnoreCase("status")) {
            trans.setTxnRefNumber(actualTransactionReference);
            if (type.equalsIgnoreCase("VOID")) {
                trans.setStatus(getResources().getString(R.string.voided));
            } else if (type.equalsIgnoreCase(REFUNDAMT)) {
                trans.setStatus(getResources().getString(R.string.refunded));
            }
            helper.updateTransaction(trans);
        }
    }
}
