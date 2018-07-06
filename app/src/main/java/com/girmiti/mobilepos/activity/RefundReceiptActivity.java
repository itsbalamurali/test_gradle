package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 21/10/17.
 */
public class RefundReceiptActivity extends BaseActivity {

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
    private TextView txtMaskedCardNumber;
    private TextView txtCardHolderName;
    private TextView txtTranxnAmount;
    private Transaction transaction;
    private Response response;
    private SharedPreferences prefs;
    private TextView txtAddress;
    private TextView txtCity;
    private TextView txtState;
    public static final String DATETIMEFORMAT = "dd/MM/yyyy   hh:mm:ss a";
    private final Logger logger =getNewLogger(ReceiptActivity.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.refund_receipt);

        TextView tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.send_recipt);

        ImageView leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SlidingMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        txtTranNo = (TextView) findViewById(R.id.txtTranNo);
        txtRefNo = (TextView) findViewById(R.id.txtRefNo);
        txtTerminalId = (TextView) findViewById(R.id.txtTerminalId);
        txtMerchantId = (TextView) findViewById(R.id.txtMerchantId);
        txtAuthCode = (TextView) findViewById(R.id.txtAuthCode);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtType = (TextView) findViewById(R.id.txtType);
        txtTip = (TextView) findViewById(R.id.txtTip);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtTranxnAmount = (TextView) findViewById(R.id.txtTranAmount);
        txtMaskedCardNumber = (TextView) findViewById(R.id.txtmasked_card_number);
        txtCardHolderName = (TextView) findViewById(R.id.card_holder_name);

        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtState = (TextView) findViewById(R.id.txtState);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            transaction = (Transaction) b.getSerializable("transaction");
            response = (Response) b.getSerializable("response");
            populateData(transaction.getTransactionType());
        }
        findViewById(R.id.skipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SlidingMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void populateData(String requestType) {
        txtTranNo.setText(response.getTxnRefNumber());
        txtRefNo.setText(response.getCgRefNumber());
        txtAuthCode.setText(response.getAuthId());
        txtMerchantName.setText(response.getMerchantName());

        String str = getResources().getConfiguration().locale.getLanguage();
        status();
        if (str .equals( "es" )) {
            if (requestType.contains("SALE")) {
                txtType.setText("VENTA");
            }
            if (requestType.equalsIgnoreCase("REFUND")) {
                txtType.setText("REEMBOLSO");
            }
        } else {
            txtType.setText(requestType);
        }
        txtTip.setText( CurrencyFormat.getCurrencyCodeAlplha() + " " + Utils.getFormattedAmount(transaction.getTipAmount()));
        txtMaskedCardNumber.setText(Utils.getMaskedCardNumberLastFourDigit(transaction.getMaskedCardNumber()));
        txtTranxnAmount.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(transaction.getTransactionamount())));
        txtCardHolderName.setText(transaction.getCardHolderName());
        txtDateTime.setText(response.getDeviceLocalTxnTime());
        txtTotalAmount.setText(CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount(transaction.getTotalamount())));
        savedata();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.clearTop(RefundReceiptActivity.this);
        Intent intent = new Intent(getApplicationContext(), SlidingMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void status() {
        String str = getResources().getConfiguration().locale.getLanguage();
        if (str.equals( "es" )) {
            String statusMessage = response.getErrorMessage();
            if (statusMessage.equals("Approved")) {
                txtStatus.setText("aprobado");
            }
        } else {
            txtStatus.setText(response.getErrorMessage());
        }
    }

    public void savedata() {
        try {
            String name = prefs.getString( getResources().getString( R.string.key_name ), null );
            String merchant = prefs.getString(getResources().getString(R.string.key_merchant), null);
            String terminal = prefs.getString(getResources().getString(R.string.key_terminal), null);
            String address = prefs.getString(getResources().getString(R.string.key_address), null);
            String city = prefs.getString(getResources().getString(R.string.key_city), null);
            String state = prefs.getString(getResources().getString(R.string.key_state), null);

            if (name != null && !name.equals( "" )) {
                txtMerchantName.setText( name );
            } else {
                txtMerchantName.setText( getResources().getString( R.string.merchant_text ) );
            }

            if (merchant != null && !merchant.equals("")) {
                txtMerchantId.setText(merchant);
            } else {
                txtMerchantId.setText(getResources().getString(R.string.merchant_id));
            }
            if (terminal != null && !terminal.equals("")) {
                txtTerminalId.setText(terminal);
            } else {
                txtTerminalId.setText(getResources().getString(R.string.terminal_id));
            }
            if (address != null && !address.equals("") && city != null && !city.equals("") && state != null && !state.equals("")) {
                txtAddress.setText(address);
                txtCity.setText(city);
                txtState.setText(state);
            } else {
                txtAddress.setText(getResources().getString(R.string.merchant_address));
                txtCity.setText(getResources().getString(R.string.merchant_city));
                txtState.setText(getResources().getString(R.string.merchant_city));
            }

        } catch (Exception e) {
            logger.severe("Error Displaying Merchant Details in Receipt" + e);
        }

    }

}
