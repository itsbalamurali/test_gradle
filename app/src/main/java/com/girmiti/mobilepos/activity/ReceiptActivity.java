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
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;
import com.imagpay.Settings;
import com.imagpay.mpos.MposHandler;
import com.imagpay.qpboc.ApplePayHandler;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by Girish on 22-10-2016.
 */
public class ReceiptActivity extends BaseActivity {

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
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private Response response;
    private SharedPreferences prefs;
    private TextView txtAddress;
    private TextView txtCity;
    private TextView txtState;
    private final Logger logger = getNewLogger( ReceiptActivity.class.getName() );
    MposHandler handler;
    com.imagpay.Settings settings;
    private String date;
    private String requestType;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_receipt );

        TextView tvToolbar = (TextView) findViewById( R.id.tvToolbar_left );
        tvToolbar.setText( R.string.send_recipt );

        ImageView leftArrow = (ImageView) findViewById( img_left_toolbar_arrow );
        leftArrow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getApplicationContext(), SlidingMenuActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
                overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right );
            }
        } );

        prefs = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        txtTranNo = (TextView) findViewById( R.id.txtTranNo );
        txtRefNo = (TextView) findViewById( R.id.txtRefNo );
        txtTerminalId = (TextView) findViewById( R.id.txtTerminalId );
        txtMerchantId = (TextView) findViewById( R.id.txtMerchantId );
        txtAuthCode = (TextView) findViewById( R.id.txtAuthCode );
        txtMerchantName = (TextView) findViewById( R.id.txtMerchantName );
        txtTotalAmount = (TextView) findViewById( R.id.txtTotalAmount );
        txtDateTime = (TextView) findViewById( R.id.txtDateTime );
        txtType = (TextView) findViewById( R.id.txtType );
        txtTip = (TextView) findViewById( R.id.txtTip );
        txtStatus = (TextView) findViewById( R.id.txtStatus );
        txtTranxnAmount = (TextView) findViewById( R.id.txtTranAmount );
        txtMaskedCardNumber = (TextView) findViewById( R.id.txtmasked_card_number );
        txtCardHolderName = (TextView) findViewById( R.id.card_holder_name );
        txtAddress = (TextView) findViewById( R.id.txtAddress );
        txtCity = (TextView) findViewById( R.id.txtCity );
        txtState = (TextView) findViewById( R.id.txtState );

        Bundle b = getIntent().getExtras();
        if (b != null) {
            confirmTransactionDetailsDTO = (ConfirmTransactionDetailsDTO) b.getSerializable( "transaction" );
            response = (Response) b.getSerializable( "response" );
            requestType = b.getString( "requestType" );
            populateData( requestType );
        }

        findViewById( R.id.sendBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ReceiptActivity.this, SignatureCapture.class );
                intent.putExtra( "txn_amount", confirmTransactionDetailsDTO.getAmount() );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity( intent );
            }
        } );

        findViewById( R.id.skipBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), SlidingMenuActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
            }
        } );

    }

    public synchronized void test(View v) {

        if ((v.getId() == R.id.print) && prefs.getString( getResources().getString( R.string.key_device_list ), "" ).equals( getString( R.string.z90_device ) )) {
                handler = new MposHandler( getApplicationContext() );
                handler.setParameters( "/dev/ttyS2", Constants.Z90 );
                settings = new com.imagpay.Settings( handler );
                getResources();
                ApplePayHandler.init( settings, handler );
                settings.mPosPowerOn();
                isNfcConneteced();
            }
        }


    private void isNfcConneteced() {

        if (!handler.isConnected()) {
            handler.connect();
            printtest();

        } else {
            handler.close();
            handler.connect();
        }
    }

    private void populateData(String requestType) {

        txtTranNo.setText( response.getTxnRefNumber() );
        txtRefNo.setText( response.getCgRefNumber() );
        txtAuthCode.setText( response.getAuthId() );
        txtMerchantName.setText( response.getMerchantName() );

        String str = getResources().getConfiguration().locale.getLanguage();
        status();
        if (str.equals( "es" )) {
            if (requestType.contains( "SALE" )) {
                txtType.setText( "VENTA" );
            }
            if (requestType.equalsIgnoreCase( "REFUND" )) {
                txtType.setText( "REEMBOLSO" );
            }
            if (requestType.equalsIgnoreCase( "QR-SALE" )) {
                txtType.setText( "QR-VENTA" );
            }
            if (requestType.equalsIgnoreCase( "NFC-SALE" )) {
                txtType.setText( "NFC-VENTA" );
            }

        } else {
            txtType.setText( requestType );
        }
        txtTip.setText( CurrencyFormat.getCurrencyCodeAlplha() + " " + Utils.getFormattedAmount( confirmTransactionDetailsDTO.getTipAmount() ) );
        txtMaskedCardNumber.setText( Utils.getMaskedCardNumberLastFourDigit( confirmTransactionDetailsDTO.getCardNumber() ) );
        txtTranxnAmount.setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( confirmTransactionDetailsDTO.getTransactionAmount() ) ) );
        txtCardHolderName.setText( confirmTransactionDetailsDTO.getCardholderName() );
        txtDateTime.setText(response.getDeviceLocalTxnTime());
        txtTotalAmount.setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( confirmTransactionDetailsDTO.getAmount() ) ) );
        savedata();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Utils.clearTop( ReceiptActivity.this );
        Intent intent = new Intent( getApplicationContext(), SlidingMenuActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
    }

    public void status() {

        String str = getResources().getConfiguration().locale.getLanguage();
        if (str.equals( "es" )) {
            String statusMessage = response.getErrorMessage();
            if (statusMessage.equals( "Approved" )) {
                txtStatus.setText( "aprobado" );
            }
        } else {
            txtStatus.setText( response.getErrorMessage() );
        }
    }

    public void savedata() {
        try {
            String merchant = prefs.getString( getResources().getString( R.string.key_merchant ), null );
            String terminal = prefs.getString( getResources().getString( R.string.key_terminal ), null );
            String address = prefs.getString( getResources().getString( R.string.key_address ), null );
            String city = prefs.getString( getResources().getString( R.string.key_city ), null );
            String state = prefs.getString( getResources().getString( R.string.key_state ), null );

            if (merchant != null && !merchant.equals( "" )) {
                txtMerchantId.setText( merchant );
            } else {
                txtMerchantId.setText( getResources().getString( R.string.merchant_id ) );
            }
            if (terminal != null && !terminal.equals( "" )) {
                txtTerminalId.setText( terminal );
            } else {
                txtTerminalId.setText( getResources().getString( R.string.terminal_id ) );
            }
            if (address != null && !address.equals( "" ) && city != null && !city.equals( "" ) && state != null && !state.equals( "" )) {
                txtAddress.setText( address );
                txtCity.setText( city );
                txtState.setText( state );
            } else {
                txtAddress.setText( getResources().getString( R.string.merchant_address ) );
                txtCity.setText( getResources().getString( R.string.merchant_city ) );
                txtState.setText( getResources().getString( R.string.merchant_city ) );
            }
        } catch (Exception e) {
            logger.severe( "Error Displaying Merchant Details in Receipt" + e );
        }
    }

    private void printtest() {

        new Thread( new Runnable() {
            public void run() {
                try {
                    settings.mPosEnterPrint();
                    settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
                    settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_SIZE );
                    settings.mPosPrnStr( response.getMerchantName() );
                    settings.mPosPrnStr( prefs.getString( getResources().getString( R.string.key_address ), null ) );
                    settings.mPosPrnStr( prefs.getString( getResources().getString( R.string.key_city ), null ) );
                    settings.mPosPrnStr( prefs.getString( getResources().getString( R.string.key_state ), null ) );
                    settings.mPosPrintLn();

                    initilizePrinterReceipt();
                } catch (Exception e) {
                    logger.severe( "Exception " + e );
                }
            }
        } ).start();
    }

    private void initilizePrinterReceipt() {

        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_LEFT );
        String[] parts = date.split( " " );
        settings.mPosPrnStr( "Date: " + parts[0] + "            " + "Time: " + parts[1] + " " + parts[Constants.TWO] );
        settings.mPosPrnStr( "MID: " + prefs.getString( getResources().getString( R.string.key_merchant ), null ) + "        " + "TID: " + prefs.getString( getResources().getString( R.string.key_terminal ), null ) );

        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_SIZE );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
        settings.mPosPrnStr( requestType );
        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_LEFT );
        settings.mPosPrnStr( "Card Num: " + Utils.getMaskedCardNumber( confirmTransactionDetailsDTO.getCardNumber() ) );
        settings.mPosPrnStr( "Card Name: " + confirmTransactionDetailsDTO.getCardholderName() );
        settings.mPosPrnStr( "Txn ID: " + response.getTxnRefNumber() + "           " + "Auth Code: " + response.getAuthId() );
        settings.mPosPrnStr( "Processor Txn ID: " + response.getCgRefNumber() + "   " + "Status: " + response.getErrorMessage() );

        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_SIZE );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
        settings.mPosPrnStr( " Amount: " + CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( confirmTransactionDetailsDTO.getAmount() ) ) + " " );
        settings.mPosPrintLn();
        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_DOUBLE_HEIGHT );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
        settings.mPosPrnStr( "PIN VERIFIED OK" );
        settings.mPosPrnStr( "NO SIGNATURE REQUIRED" );
        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_NORMAL );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
        settings.mPosPrnStr( "I AGREE TO PAY THE ABOVE TOTAL AMOUNT ACCORDING TO CARD ISSUER AGREEMENT" );
        settings.mPosPrintLn();
        settings.mPosPrintTextSize( Settings.MPOS_PRINT_TEXT_NORMAL );
        settings.mPosPrintAlign( Settings.MPOS_PRINT_ALIGN_CENTER );
        settings.mPosPrnStr( "****** CUSTOMER COPY ******" );
        settings.mPosPrnStr( "Version 1.0.10" );
        settings.mPosPrnStr( "Powered by Ipsidy" );
        settings.mPosPrintLn();
        settings.mPosPrintLn();
        settings.mPosPrintLn();
        settings.mPosPrintLn();
    }
}