package com.girmiti.mobilepos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.nfc.reader.MposCardReader;
import com.girmiti.mobilepos.nfc.utils.Utils;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class CardWaitActivity extends BaseActivity implements MposCardReader.DataCallback {

    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    private static int readerflags = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private final Logger logger = getNewLogger(CardWaitActivity.class.getName());
    MposCardReader mposCardReader;
    private Map<String, String> mCardReaderData;
    private String amount;
    private String tipAmount;
    private String cardNumber;
    private String emv;
    private String track2data;
    private String saleAmount;
    private String cardHolder;
    private String cardType;
    private String entryMode;
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    public static final String EXPIRY = "expiry";
    public static final String CRYPTOGRAM = "cryptogram";
    public static final String CRYPTOGRAMINFO = "cryptogramInfo";
    public static final String TRACK2INFO = "track2";
    public static final String PANSEQNUM = "panSeqNum";
    public static final String UNPREDICTNUM = "unPredictNum";
    public static final String VISACARD = "VI";
    public static final String MASTERCARD = "MC";
    public static final String IPSIDYCARD = "IP";
    public static final String BLANKCARD = "BLANK";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TextView tvToolbar;
        ImageView leftArrow;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardwait);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("total_amount")) {
            amount = intent.getExtras().getString("total_amount");
            tipAmount = intent.getStringExtra("tip_amount");
            saleAmount = intent.getStringExtra("sale_amount");
        }
        mposCardReader = new MposCardReader(this);
        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();


        tvToolbar = (TextView) findViewById(R.id.tvToolbar_left);
        tvToolbar.setText(R.string.present_card);
        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });
    }

    @Override
    public void onDataReceived(final Map<String, String> responseData) {

        logger.info("Card Holder Name : " + (responseData.get("name") != null ? responseData.get("name") : ""));
        logger.info("PAN : " + (responseData.get("pan") != null ? responseData.get("pan").substring(responseData.get("pan").length() - Constants.FOUR) : ""));
        logger.info("ATC: " + (responseData.get("atc") != null ? responseData.get("atc") : ""));
        logger.info("AIP: " + (responseData.get("aip") != null ? responseData.get("aip") : ""));
        logger.info("Cryptogram: " + (responseData.get(CRYPTOGRAM) != null ? responseData.get(CRYPTOGRAM) : ""));
        logger.info("PAN Seq Num: " + (responseData.get(PANSEQNUM) != null ? responseData.get(PANSEQNUM) : ""));
        mCardReaderData = responseData;

        if (responseData != null) {
            String expiryDate;
            cardNumber = responseData.get("pan") != null ? responseData.get("pan") : "";
            cardType = CommonUtils.getCCType(cardNumber);
            expiryDate = responseData.get(EXPIRY) != null ? responseData.get(EXPIRY) : "";
            cardHolder = responseData.get("name") != null ? responseData.get("name") : "";

            typeofCard();
            swipeorContactless();
            confirmTransactionDetailsDTO.setAmount(String.format("%.2f", Float.valueOf(amount)));
            confirmTransactionDetailsDTO.setTipAmount(String.format("%.2f", Float.valueOf(tipAmount)));
            confirmTransactionDetailsDTO.setTransactionAmount(String.format("%.2f", Float.valueOf(saleAmount)));
            confirmTransactionDetailsDTO.setCardholderName(cardHolder);
            confirmTransactionDetailsDTO.setCardNumber(cardNumber);
            confirmTransactionDetailsDTO.setExpDate(expiryDate.substring(0, Constants.FOUR));
            confirmTransactionDetailsDTO.setEmvField55(emv);
            confirmTransactionDetailsDTO.setTrack2(track2data);
            confirmTransactionDetailsDTO.setCardType(cardType);
            confirmTransactionDetailsDTO.setEntryMode(entryMode);
            CardWaitActivity.this.runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    CardWaitActivity.this.track2null();
                }
            } );
        } else {
            showDialog(getResources().getString(R.string.nfc_tap_error));
        }
    }

    private void track2null() {
        if (track2data == null || "".equals(track2data)) {
            showDialog(getResources().getString(R.string.nfc_tap_error));
        } else {
            showConfirmDialog(null, false);
        }
    }

    private void typeofCard() {
        if (cardType.equalsIgnoreCase(VISACARD)) {
            emv = buildEmv55();
            track2data = buildTrack2();
            logger.info("EMV 55 " + buildEmv55());
        } else if (cardType.equalsIgnoreCase(MASTERCARD) || cardType.equalsIgnoreCase(IPSIDYCARD)) {
            emv = "";
            track2data = buildTrack2ForMC();
        } else if (cardType.equalsIgnoreCase(BLANKCARD)) {
            track2data = "";
        }
    }

    private void swipeorContactless() {
        if (cardType.equalsIgnoreCase(VISACARD) && !emv.isEmpty()) {
            entryMode = "PAN_AUTO_ENTRY_CONTACTLESS_M_CHIP";
        } else if (cardType.equalsIgnoreCase(MASTERCARD) && !track2data.isEmpty()) {
            entryMode = "PAN_SWIPE_CONTACTLESS";
        } else if (cardType.equalsIgnoreCase(IPSIDYCARD) && !track2data.isEmpty()) {
            entryMode = "PAN_SWIPE_CONTACTLESS";
        } else {
            entryMode = "MANUAL";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    private void enableReaderMode() {
        logger.info("MainActivity.enableReaderMode()");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, mposCardReader, readerflags, null);
        }
    }

    private void disableReaderMode() {
        logger.info("MainActivity.disableReaderMode()");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }

    private String buildEmv55() {

        String emv55 = "";
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();

        try {
            emv55 = "82" + "02" + mCardReaderData.get("aip")
                    + "95" + "05" + Utils.byteArrayToHexString(MposCardReader.TVR)
                    + "9A" + "03" + dateFormat.format(date)
                    + "9C" + "01" + "00"
                    + "5F2A" + "02" + MposCardReader.CURRENCY_CODE
                    + "5F34" + "01" + mCardReaderData.get(PANSEQNUM)
                    + "9F02" + "06" + String.format("%012d", Integer.parseInt(amount.replace(".", "")))
                    + "9F03" + "06" + "000000000000"
                    + "9F10" + String.format("%01X", mCardReaderData.get("issuerAppData").length() / Constants.TWO) + mCardReaderData.get("issuerAppData")
                    + "9F1A" + "02" + MposCardReader.COUNTRY_CODE
                    + "9F1E" + "08" + "0000000000000000"
                    + "9F26" + "08" + mCardReaderData.get(CRYPTOGRAM)
                    + "9F27" + "01" + mCardReaderData.get(CRYPTOGRAMINFO)
                    + "9F36" + "02" + mCardReaderData.get("atc")
                    + "9F37" + String.format("%02X", mCardReaderData.get(UNPREDICTNUM).length() / Constants.TWO) + mCardReaderData.get(UNPREDICTNUM)
                    + "9F66" + "04" + Utils.byteArrayToHexString(MposCardReader.TTQ);
        } catch (Exception e) {
            logger.severe("Exception while getting emv55 data " + e.toString() + e);
        }

        return emv55;
    }

    private String buildTrack2() {
        String track2 = "";
        try {
            track2 = mCardReaderData.get(TRACK2INFO);
            logger.info("Track2 Data ::::" + mCardReaderData.get(TRACK2INFO));
        } catch (Exception e) {
            logger.severe("Exception in getting track2 data  " + e.toString() + e);
        }

        return track2;
    }

    // Changes for pin validation
    private String buildTrack2ForMC() {
        String track2 = "";
        try {
            track2 = mCardReaderData.get("_9f6bValue").substring(0, Constants.TWENTYFIVE)
                    + Utils.paddingWithZeros(String.valueOf(Utils.hexToDec(mCardReaderData.get("cryptoATC"))), Constants.FIVE).substring(1)
                    + Utils.paddingWithZeros(String.valueOf(Utils.hexToDec(mCardReaderData.get("cvc3"))), Constants.FIVE).substring(1)
                    + mCardReaderData.get(UNPREDICTNUM).substring(Constants.FIVE)
                    + "3";
            logger.info("Track2 Data For MC ::::" + track2);
            logger.info("SendcryptoATC" + (mCardReaderData.get("cryptoATC")));
            logger.info("CVC3" + (mCardReaderData.get("cvc3")));

        } catch (Exception e) {
            logger.severe("Exception in getting track2 for MC" + e.toString() + e);
        }
        return track2;
    }

    private void showConfirmDialog(String data, boolean isQrCodeTxn) {

        // Get the layout inflater
        disableReaderMode();
        LayoutInflater inflater = CardWaitActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_transaction_dialog, null);
        if (isQrCodeTxn) {
            String qrCode = data.substring(0, Constants.SIXTEEN);
            String track2 = data.substring(Constants.SIXTEEN, Constants.FOURTYSIX);
            cardHolder = CommonUtils.removeLastCharacters(data.substring(Constants.FOURTYSIX), Constants.FOUR);
            String[] cardDetails = data.split("D");
            String expDate = cardDetails[1].substring(0, Constants.FOUR);
            String cardnum = "**** **** **** " + data.substring(data.length() - Constants.FOUR);
            confirmTransactionDetailsDTO.setAmount(String.format("%.2f", Float.valueOf(amount)));
            confirmTransactionDetailsDTO.setTipAmount(String.format("%.2f", Float.valueOf(tipAmount)));
            confirmTransactionDetailsDTO.setTransactionAmount(String.format("%.2f", Float.valueOf(saleAmount)));
            confirmTransactionDetailsDTO.setCardholderName(cardHolder);
            confirmTransactionDetailsDTO.setCardNumber(cardnum);
            confirmTransactionDetailsDTO.setExpDate(expDate);
            confirmTransactionDetailsDTO.setEmvField55("");
            confirmTransactionDetailsDTO.setTrack2(track2);
            confirmTransactionDetailsDTO.setQrCode(qrCode);
            if (cardnum.startsWith("5"))
                confirmTransactionDetailsDTO.setCardType(MASTERCARD);
            else confirmTransactionDetailsDTO.setCardType(VISACARD);
            confirmTransactionDetailsDTO.setEntryMode(getResources().getString(R.string.qrsale));

            ((TextView) view.findViewById(R.id.card_number)).setText(cardnum);
            ((TextView) view.findViewById(R.id.exp_date)).setText(expDate);
            ((TextView) view.findViewById(R.id.amount)).setText(CurrencyFormat.getFormattedCurrency(String.format("%.2f", Float.valueOf(amount))));
            ((TextView) view.findViewById(R.id.card_holder)).setText(cardHolder);

        } else {
            ((TextView) view.findViewById(R.id.card_number)).setText(com.girmiti.mobilepos.util.Utils.getMaskedCardNumber(cardNumber));
            ((TextView) view.findViewById(R.id.card_holder)).setText(cardHolder);
            ((TextView) view.findViewById(R.id.exp_date)).setText(confirmTransactionDetailsDTO.getExpDate());
            ((TextView) view.findViewById(R.id.amount)).setText(CurrencyFormat.getFormattedCurrency(String.format("%.2f", Float.valueOf(amount))));

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(CardWaitActivity.this);
        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CardWaitActivity.this, TransactionResultActivity.class);
                        intent.putExtra(getString(R.string.transaction_details), confirmTransactionDetailsDTO);
                        startActivity(intent);
                        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            //get the extras that are returned from the intent
            String contents = data.getStringExtra("SCAN_RESULT");

            try {
                showConfirmDialog(contents, true);
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.failed_read_qr), Toast.LENGTH_LONG).show();
                logger.severe("QR code format error" + e);
            }
        }
    }

    private void showDialog(String errorMessage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}