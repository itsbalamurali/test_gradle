package com.girmiti.mobilepos.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.BillingData;
import com.girmiti.mobilepos.net.model.CardData;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.net.model.SaleRequest;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.Utils;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class TransactionResultActivity extends BaseActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private int invoiceNumber;
    private SharedPreferences prefs;
    private String terminal;
    private String merchant;
    private static final String REQTYPE = "requestType";
    private Logger logger = getNewLogger(TransactionResultActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {

        TextView textPanNumber;
        TextView cardHolderName;
        ImageView cardLogo;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txnresult);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        merchant = prefs.getString(getResources().getString(R.string.key_merchant), "");
        terminal = prefs.getString(getResources().getString(R.string.key_terminal), "");
        textPanNumber = (TextView) findViewById(R.id.text_pan_number);
        cardHolderName = (TextView) findViewById(R.id.cardHolderName);
        cardLogo = (ImageView) findViewById(R.id.card_logo);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.transaction_details))) {
            confirmTransactionDetailsDTO = (ConfirmTransactionDetailsDTO) intent.getSerializableExtra(getString(R.string.transaction_details));
            textPanNumber.setText(Utils.getMaskedCardNumber(confirmTransactionDetailsDTO.getCardNumber()));
            cardHolderName.setText(confirmTransactionDetailsDTO.getCardholderName());

            if (confirmTransactionDetailsDTO.getCardNumber() != null) {
                cardLogo.setImageDrawable(Utils.visaOrMasterCardImage(this, confirmTransactionDetailsDTO.getCardNumber()));
            }
        }
        progressDialog = new ProgressDialog(this);
        findViewById(R.id.signatureBtn).setOnClickListener(this);
        findViewById(R.id.skipBtn).setOnClickListener(this);

        new PerformSaleTask().execute();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.signatureBtn) {
            Intent intent = new Intent(TransactionResultActivity.this, SignatureCapture.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
    }

    private class PerformSaleTask extends AsyncTask<Response, Void, Response> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Response doInBackground(Response... responseData) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            invoiceNumber = pref.getInt("InvoiceNumber", 0);
            editor.putInt("InvoiceNumber", ++invoiceNumber);
            editor.commit();

            SaleRequest request = new SaleRequest();

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

            request.setInvoiceNumber(Long.toString(System.currentTimeMillis()));
            request.setMerchatntName(getResources().getString(R.string.chatak_merchant_name));
            request.setMerchantAmount(Utils.formatAmount(confirmTransactionDetailsDTO.getAmount()));
            request.setFeeAmount("0");
            request.setTotalTxnAmount(Utils.formatAmount(confirmTransactionDetailsDTO.getAmount()));
            request.setRegisterNumber("11225");
            request.setTxnType(getResources().getString(R.string.sale));
            request.setOrderId(Integer.toString(invoiceNumber));
            request.setQrCode(confirmTransactionDetailsDTO.getQrCode());

            CardData cardData = new CardData();
            if (!Utils.isEmptyString(confirmTransactionDetailsDTO.getCardNumber()) && !confirmTransactionDetailsDTO.getEntryMode().equals(getResources().getString(R.string.qrsale))) {
                cardData.cardNumber = confirmTransactionDetailsDTO.getCardNumber();
            } else {
                cardData.cardNumber = "";
            }
            if (!Utils.isEmptyString(confirmTransactionDetailsDTO.getExpDate()) && !confirmTransactionDetailsDTO.getEntryMode().equals(getResources().getString(R.string.qrsale))) {
                cardData.expDate = confirmTransactionDetailsDTO.getExpDate().substring(0, Constants.FOUR);
            } else {
                cardData.expDate = "";
            }
            cardData.cardType = confirmTransactionDetailsDTO.getCardType();
            cardData.cardHolderName = confirmTransactionDetailsDTO.getCardholderName();
            cardData.emv = confirmTransactionDetailsDTO.getEmvField55();
            cardData.track2 = confirmTransactionDetailsDTO.getTrack2();
            cardData.cvv = confirmTransactionDetailsDTO.getCvv();
            cardData.uid = confirmTransactionDetailsDTO.getUid();

            request.setCardData(cardData);
            request.setEntryMode(confirmTransactionDetailsDTO.getEntryMode());
            request.setUserName( prefs.getString( getResources().getString(R.string.key_email),null ) );
            BillingData billingData = new BillingData();
            billingData.address1 = "Kundanahalli Gate";
            billingData.address2 = "MarathHalli";
            billingData.city = "Bangalore";
            billingData.state = "Karnataka";
            billingData.country = "IN";
            billingData.email = "test@girmiti.com";
            billingData.zipCode = "560019";

            request.setBillingData(billingData);

            request.setTimeZoneOffset(Utils.currentTimeZone());
            request.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
            Response response = null;
            try {
                response = ServerCommunication.getInstance().processSale(request);
            } catch (Exception e) {
                logger.severe("Error in process Sale" + e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

             closeDialog();
            if (response != null && response.getErrorMessage() != null && response.getErrorCode().equals("00")) {

              initilizeTxnRceipt(  response);
            } else if (response != null && response.getErrorMessage() != null) {
                try {
                    saveData(response);
                } catch (GenericException e) {
                    logger.severe("Error in saving data " +e);
                }
                showDialog(response.getErrorMessage());
            } else {
                if (!isFinishing())
                    showDialog(getResources().getString(R.string.something_wrong));
            }
        }

        private void initilizeTxnRceipt(Response response) {

            Intent intent = new Intent(TransactionResultActivity.this, ReceiptActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("transaction", confirmTransactionDetailsDTO);
            b.putSerializable("response", response);
            if (confirmTransactionDetailsDTO.getEntryMode().equals(getResources().getString(R.string.qrsale))) {
                b.putString(REQTYPE, getResources().getString(R.string.qr_sale));
            } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString( R.string.manual_type )) || confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString(R.string.card_tap))) {
                b.putString(REQTYPE, getResources().getString(R.string.manual_sale));
            } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString( R.string.contactless )) || confirmTransactionDetailsDTO.getEntryMode().equals("PAN_SWIPE_CONTACTLESS")) {
                b.putString(REQTYPE, getResources().getString(R.string.nfc_sale));
            }
            intent.putExtras(b);
            try {
                saveData(response);
            } catch (Exception e) {
                logger.severe("Error in save data" + e);
            }
            startActivity(intent);
            finish();
        }

        private void closeDialog() {
            if (progressDialog != null)
                progressDialog.dismiss();
        }
        private void saveData(Response response) throws GenericException {
            DataStoreHelper helper = new DataStoreHelper(TransactionResultActivity.this);
            Transaction trans = new Transaction();
            trans.setTransactionamount(confirmTransactionDetailsDTO.getTransactionAmount());
            trans.setEntrymode(confirmTransactionDetailsDTO.getEntryMode());
            trans.setInvoiceno(Integer.toString(invoiceNumber));
            trans.setTotalamount(Utils.getFormattedAmount(confirmTransactionDetailsDTO.getAmount()));
            trans.setTransactionfee("0");
            trans.setCgRefNumber(response.getCgRefNumber());
            trans.setTxnDateTime(response.getDeviceLocalTxnTime());
            trans.setTxnRefNumber(response.getTxnRefNumber());
            trans.setAuthId(response.getAuthId());
            if(response.getErrorCode().equals("00")) {
                trans.setStatus(response.getErrorMessage());
            } else {
                trans.setStatus(getString( R.string.declined_msg ));
            }
            trans.setMerchantCode(response.getMerchantCode());
            trans.setCardHolderName(confirmTransactionDetailsDTO.getCardholderName());
            trans.setExpDate(confirmTransactionDetailsDTO.getExpDate());
            trans.setMaskedCardNumber(Utils.getMaskedCardNumberLastFourDigit(confirmTransactionDetailsDTO.getCardNumber()));
            trans.setTipAmount(Utils.getFormattedAmount(confirmTransactionDetailsDTO.getTipAmount()));

            if (confirmTransactionDetailsDTO.getEntryMode().equals(getResources().getString(R.string.qrsale))) {
                trans.setTransactionType(getResources().getString(R.string.qr_sale));
            } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString( R.string.manual_type ))||(confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString(R.string.card_tap)))) {
                trans.setTransactionType(getResources().getString(R.string.manual_sale));
            } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase(getString( R.string.contactless )) || confirmTransactionDetailsDTO.getEntryMode().equals("PAN_SWIPE_CONTACTLESS")) {
                trans.setTransactionType(getResources().getString(R.string.nfc_sale));
            }
            trans.setMerchantName(response.getMerchantName());
            helper.addTransaction(trans);
        }

        private void showDialog(String errorMessage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TransactionResultActivity.this);
            builder.setTitle(getResources().getString(R.string.error))
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (progressDialog != null)
            progressDialog.dismiss();
    }

}