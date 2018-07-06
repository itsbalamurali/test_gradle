package com.girmiti.mobilepos.activity;

/**
 * Created by aravind on 29-04-2017.
 */

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.qrcode.Intents;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class PaymentOptionActivity extends BaseActivity {
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private String amount;
    private String tipAmount;
    private String saleAmount;
    private String cardHolder;
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private String cardNumber;
    private SharedPreferences prefs;
    public static final String TOTALAMT = "total_amount";
    public static final String TIPAMT = "tip_amount";
    public static final String SALEAMT = "sale_amount";
    private final Logger logger = getNewLogger(PaymentOptionActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tvToolbar;
        Button btnNfc;
        ImageView leftArrow;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_option);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TOTALAMT)) {
            amount = intent.getExtras().getString(TOTALAMT);
            tipAmount = intent.getStringExtra(TIPAMT);
            saleAmount = intent.getStringExtra(SALEAMT);
        }
        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();
        tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.payment_option_name);

        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        btnNfc = (Button) findViewById(R.id.btn_nfc);
        btnNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentOptionActivity.this, CardWaitActivity.class);
                intent1.putExtra(SALEAMT, String.valueOf(saleAmount));
                intent1.putExtra(TOTALAMT, String.valueOf(amount));
                intent1.putExtra(TIPAMT, String.valueOf(tipAmount));
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });


        findViewById(R.id.manualEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentOptionActivity.this, ManualEntryActivity.class);
                intent.putExtra(TOTALAMT, String.format("%.2f", Float.valueOf(amount)));
                intent.putExtra(TIPAMT, String.format("%.2f", Float.valueOf(tipAmount)));
                intent.putExtra(SALEAMT, String.format("%.2f", Float.valueOf(saleAmount)));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        findViewById(R.id.readQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //start the scanning activity from the com.google.zxing.client.android.SCAN intent
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    logger.info("inside PaymentOptionActivity :::" + anfe);
                    Toast.makeText(PaymentOptionActivity.this, getString(R.string.no_scanner), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (prefs != null && prefs.getBoolean(getResources().getString(R.string.key_tip), true)) {
            Intent i = new Intent(PaymentOptionActivity.this, TipActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        } else {
            Intent i = new Intent(PaymentOptionActivity.this, SlidingMenuActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        }
    }

    private void showConfirmDialog(String data, boolean isQrCodeTxn) {

        // Get the layout inflater
        LayoutInflater inflater = PaymentOptionActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_transaction_dialog, null);
        if (isQrCodeTxn) {
            String qrCode = data.substring(0, Constants.SIXTEEN);
            String track2 = data.substring(Constants.SIXTEEN, Constants.FOURTYSIX);
            cardHolder = CommonUtils.removeLastCharacters(data.substring(Constants.FOURTYSIX), Constants.FOUR);
            String[] cardDetails = data.split("D");
            String expDate = cardDetails[1].substring(0, Constants.FOUR);
            cardNumber = "**** **** **** " + data.substring(data.length() - Constants.FOUR);

            confirmTransactionDetailsDTO.setAmount(String.format("%.2f", Float.valueOf(amount)));
            confirmTransactionDetailsDTO.setTipAmount(String.format("%.2f", Float.valueOf(tipAmount)));
            confirmTransactionDetailsDTO.setTransactionAmount(String.format("%.2f", Float.valueOf(saleAmount)));
            confirmTransactionDetailsDTO.setCardholderName(cardHolder);
            confirmTransactionDetailsDTO.setCardNumber(cardNumber);
            confirmTransactionDetailsDTO.setExpDate(expDate);
            confirmTransactionDetailsDTO.setEmvField55("");
            confirmTransactionDetailsDTO.setTrack2(track2);
            confirmTransactionDetailsDTO.setQrCode(qrCode);
            if (cardNumber.startsWith("5"))
                confirmTransactionDetailsDTO.setCardType("MC");
            else confirmTransactionDetailsDTO.setCardType("VI");
            confirmTransactionDetailsDTO.setEntryMode(getResources().getString(R.string.qrsale));

            ((TextView) view.findViewById(R.id.card_number)).setText(cardNumber);
            ((TextView) view.findViewById(R.id.exp_date)).setText("****");
            ((TextView) view.findViewById(R.id.amount)).setText(CurrencyFormat.getFormattedCurrency(String.format("%.2f", Float.valueOf(amount))));
            ((TextView) view.findViewById(R.id.card_holder)).setText(cardHolder);

        } else {
            ((TextView) view.findViewById(R.id.card_number)).setText(com.girmiti.mobilepos.util.Utils.getMaskedCardNumber(cardNumber));
            ((TextView) view.findViewById(R.id.card_holder)).setText(cardHolder);
            ((TextView) view.findViewById(R.id.exp_date)).setText("****");
            ((TextView) view.findViewById(R.id.amount)).setText(CurrencyFormat.getFormattedCurrency(String.format("%.2f", Float.valueOf(amount))));

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentOptionActivity.this);
        builder.setView(view)

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                })

                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(PaymentOptionActivity.this, TransactionResultActivity.class);
                        intent.putExtra(getString(R.string.transaction_details), confirmTransactionDetailsDTO);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            processScanData(data);
        }
    }

    private void processScanData(Intent data)
    {
        String contents = data.getStringExtra("SCAN_RESULT");
        try {
            showConfirmDialog(contents, true);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.failed_read_qr), Toast.LENGTH_LONG).show();
            logger.severe("QR code Format Error" + e);
        }
    }
}

