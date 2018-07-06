package com.girmiti.mobilepos.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.BalanceInquiryRequest;
import com.girmiti.mobilepos.net.model.BalanceInquiryResponse;
import com.girmiti.mobilepos.net.model.CardData;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.net.model.TagData;
import com.girmiti.mobilepos.store.DataStoreHelper;
import com.girmiti.mobilepos.store.Transaction;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.UIValidation;
import com.girmiti.mobilepos.util.Utils;
import com.imagpay.mpos.MposHandler;
import com.imagpay.qpboc.ApplePayHandler;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 11/10/17.
 */
public class BalanceInquiryActivity extends BaseActivity {

    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private String cardNumber;
    private String cardType;
    private String entryMode;
    private ProgressDialog progressDialog;
    private int invoiceNumber;
    private EditText panEditText1;
    private EditText panEditText2;
    private EditText panEditText3;
    private EditText panEditText4;
    private EditText expDateEditText;
    private EditText cardHolderName;
    private EditText cvv;
    private NfcAdapter mAdapter;
    private String encrypted = null;
    private String terminal;
    private String merchant;
    private String merchantName;
    private PendingIntent mPendingIntent = null;
    private NdefMessage mNdefPushMessage = null;
    private static final String BALANCE = "BALANCE";
    private static final String BALANCE_INQUIRY = "CARD_TAP";
    private Logger logger = getNewLogger( BaseActivity.class.getName() );
    private Button readBtn;
    private SharedPreferences prefs;
    MposHandler handler;
    com.imagpay.Settings settings;
    boolean flag = false;
    boolean magFlag = false;
    boolean nfcFlag = false;
    private String desfireCardUid;
    private TagData tagData = null;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_balance_inquiry );

        ImageView leftArrow;
        TextView tvToolbar;
        prefs = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );

        merchant = prefs.getString( getResources().getString( R.string.key_merchant ), "" );
        terminal = prefs.getString( getResources().getString( R.string.key_terminal ), "" );
        merchantName = prefs.getString( getResources().getString( R.string.key_name ), "" );

        readBtn = (Button) findViewById( R.id.resetBtn );
        if (prefs.getString( getResources().getString( R.string.key_device_list ), "" ).equals( getString( R.string.z90_device ) )) {
            readBtn.setText( getString( R.string.read_btn ) );
        } else {
            mAdapter = NfcAdapter.getDefaultAdapter( this );
            if (mAdapter == null) {
                showMessage( R.string.warning, R.string.no_nfc );
            }
        }

        tagData = new TagData();
        mPendingIntent = PendingIntent.getActivity( this, 0,
                new Intent( this, getClass() ).addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP ), 0 );
        mNdefPushMessage = new NdefMessage( new NdefRecord[]{newTextRecord(
                "Message from NFC Reader ", Locale.ENGLISH, true )} );

        tvToolbar = (TextView) findViewById( R.id.tvToolbar_left );
        tvToolbar.setText( R.string.balance_inquiry );

        leftArrow = (ImageView) findViewById( img_left_toolbar_arrow );
        leftArrow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right );
            }
        } );

        progressDialog = new ProgressDialog( this );
        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();

        panEditText1 = (EditText) findViewById( R.id.panEditText1 );
        panEditText2 = (EditText) findViewById( R.id.panEditText2 );
        panEditText3 = (EditText) findViewById( R.id.panEditText3 );
        panEditText4 = (EditText) findViewById( R.id.panEditText4 );
        expDateEditText = (EditText) findViewById( R.id.expDateEditText );
        cardHolderName = (EditText) findViewById( R.id.cardHolderName );
        cvv = (EditText) findViewById( R.id.cvv );

        findViewById( R.id.proceedBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    confirmTransactionDetailsDTO.setCardholderName( cardHolderName.getText().toString().trim() );
                    confirmTransactionDetailsDTO.setCardNumber( cardNumber );
                    confirmTransactionDetailsDTO.setCvv( cvv.getText().toString().trim() );
                    confirmTransactionDetailsDTO.setExpDate( expDateEditText.getText().toString().trim() );
                    confirmTransactionDetailsDTO.setEmvField55( "" );
                    confirmTransactionDetailsDTO.setTrack2( "" );

                    cardType = CommonUtils.getCCType( cardNumber );
                    confirmTransactionDetailsDTO.setCardType( cardType );
                    entryMode = BALANCE_INQUIRY;
                    confirmTransactionDetailsDTO.setEntryMode( entryMode );
                    confirmTransactionDetailsDTO.setTxnType( getString( R.string.balance_inquiry ) );
                    showConfirmDialog( confirmTransactionDetailsDTO );
                }
            }
        } );

        addTextListeners();
    }

    public synchronized void test(View v) {
        if (v.getId() == R.id.resetBtn) {
            if (prefs.getString( getResources().getString( R.string.key_device_list ), "" ).equals( getString( R.string.z90_device ) )) {
                handler = new MposHandler( getApplicationContext() );
                handler.setParameters( "/dev/ttyS2", Constants.Z90 );
                settings = new com.imagpay.Settings( handler );
                ApplePayHandler.init( settings, handler );
                settings.mPosPowerOn();
                isNfcConneteced();
                cardInput();
            } else {
                clearEditTexts();
            }
        }
    }

    private void isNfcConneteced() {
        if (!handler.isConnected()) {
            handler.connect();
            logger.severe( "card connected" );
            ntagest();
        } else {
            handler.close();
            nfcFlag = false;
            handler.connect();
        }
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes( Charset.forName( "US-ASCII" ) );

        Charset utfEncoding = encodeInUtf8 ? Charset.forName( "UTF-8" ) : Charset.forName( "UTF-16" );
        byte[] textBytes = text.getBytes( utfEncoding );

        int utfBit = encodeInUtf8 ? 0 : (1 << Constants.SEVEN);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy( langBytes, 0, data, 1, langBytes.length );
        System.arraycopy( textBytes, 0, data, 1 + langBytes.length, textBytes.length );

        return new NdefRecord( NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data );
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearEditTexts();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch( this, mPendingIntent, null, null );
            mAdapter.enableForegroundNdefPush( this, mNdefPushMessage );
            cardInput();
        }
    }

    private void cardInput() {

        String desfireCardNumber = null;
        String desfireExpDate = null;
        String desfireCardHolderName = null;
        if (tagData != null) {
            desfireCardUid = tagData.getCardUid();
            encrypted = tagData.getNfcData();

            if (encrypted != null) {
                desfireCardNumber = encrypted.substring( 0, Constants.SIXTEEN ).toString();
                desfireExpDate = encrypted.substring( Constants.SIXTEEN, Constants.TWENTY ).toString();
                desfireCardHolderName = encrypted.substring( Constants.TWENTY ).toString();

                if (desfireCardNumber.length() != 0) {
                    panEditText1.setText( desfireCardNumber.substring( 0, Constants.FOUR ) );
                    panEditText1.setError( null );
                    panEditText2.setText( desfireCardNumber.substring( Constants.FOUR, Constants.EIGHT ) );
                    panEditText2.setError( null );
                    panEditText3.setText( desfireCardNumber.substring( Constants.EIGHT, Constants.TWELVE ) );
                    panEditText3.setError( null );
                    panEditText4.setText( desfireCardNumber.substring( Constants.TWELVE ) );
                    panEditText4.setError( null );
                }
                if (desfireExpDate.length() != 0 && desfireExpDate.length() == Constants.FOUR) {
                    expDateEditText.setText( desfireExpDate );
                    expDateEditText.setError( null );
                } else if (desfireExpDate.length() != 0) {
                    expDateEditText.setText( 0 + desfireExpDate );
                    expDateEditText.setError( null );
                }

                if (desfireCardHolderName.length() != 0) {
                    cardHolderName.setText( desfireCardHolderName );
                    cardHolderName.setError( null );
                }
            }
        }
    }

    private void showConfirmDialog(final ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO) {

        // Get the layout inflater
        LayoutInflater inflater = BalanceInquiryActivity.this.getLayoutInflater();
        View view = inflater.inflate( R.layout.balance_inquiry_confirm_dialog, null );

        ((TextView) view.findViewById( R.id.card_number )).setText( com.girmiti.mobilepos.util.Utils.getMaskedCardNumber( confirmTransactionDetailsDTO.getCardNumber() ) );
        ((TextView) view.findViewById( R.id.exp_date )).setText( confirmTransactionDetailsDTO.getExpDate() );
        ((TextView) view.findViewById( R.id.card_holder )).setText( confirmTransactionDetailsDTO.getCardholderName() );
        ((TextView) view.findViewById( R.id.txn_type )).setText( getResources().getString( R.string.balance_inquiry ) );

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder( BalanceInquiryActivity.this );
        builder.setView( view )
                // Add action buttons
                .setPositiveButton( R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new PerformBalanceInquiry().execute();

                    }
                } )
                .setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        clearEditTexts();
                    }
                } );
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside( false );
        dialog.show();
    }

    private void clearEditTexts() {
        panEditText1.setText( "" );
        panEditText2.setText( "" );
        panEditText3.setText( "" );
        panEditText4.setText( "" );
        expDateEditText.setText( "" );
        cardHolderName.setText( "" );
        cvv.setText( "" );
        cvv.requestFocus();
    }

    private class PerformBalanceInquiry extends AsyncTask<BalanceInquiryResponse, Void, BalanceInquiryResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage( getResources().getString( R.string.please_wait ) );
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside( false );
        }

        @Override
        protected BalanceInquiryResponse doInBackground(BalanceInquiryResponse... params) {

            BalanceInquiryResponse balanceInquiryResponse = null;
            SharedPreferences pref = getApplicationContext().getSharedPreferences( "MyPref", MODE_PRIVATE );
            SharedPreferences.Editor editor = pref.edit();
            invoiceNumber = pref.getInt( "InvoiceNumber", 0 );
            editor.putInt( "InvoiceNumber", ++invoiceNumber );
            editor.commit();

            CardData cardData = new CardData();
            cardData.cardHolderName = confirmTransactionDetailsDTO.getCardholderName();
            cardData.cardNumber = confirmTransactionDetailsDTO.getCardNumber();
            cardData.cvv = confirmTransactionDetailsDTO.getCvv();
            cardData.uid = desfireCardUid;
            cardData.expDate = confirmTransactionDetailsDTO.getExpDate();
            BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
            balanceInquiryRequest.setMerchantId( merchant );
            balanceInquiryRequest.setTerminalId( terminal );
            balanceInquiryRequest.setCardData( cardData );
            balanceInquiryRequest.setTxnType( BALANCE );
            balanceInquiryRequest.setEntryMode( entryMode );
            balanceInquiryRequest.setTimeZoneOffset(Utils.currentTimeZone());
            balanceInquiryRequest.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
            String username = prefs.getString( getResources().getString( R.string.key_email ), "" );
            balanceInquiryRequest.setUserName( username );
            try {
                balanceInquiryResponse = ServerCommunication.getInstance().processBalanceInquiry( balanceInquiryRequest );
            } catch (Exception e) {
                logger.severe( "Exception occured while performing balance inquiry" + e );
            }
            return balanceInquiryResponse;
        }

        @Override
        protected void onPostExecute(BalanceInquiryResponse balanceInquiryResponse) {
            super.onPostExecute( balanceInquiryResponse );

            if (progressDialog != null)
                progressDialog.dismiss();

            if (balanceInquiryResponse != null && balanceInquiryResponse.getErrorMessage() != null && balanceInquiryResponse.getErrorCode().equals( "00" )) {

                saveBalanceResponse( balanceInquiryResponse );
            } else if (balanceInquiryResponse != null && balanceInquiryResponse.getErrorMessage() != null) {
                try {
                    balanceInquiryResponse.setCustomerBalance( "0.00" );
                    saveData( balanceInquiryResponse );
                    showDialog( balanceInquiryResponse.getErrorMessage() );
                } catch (Exception e) {
                    logger.severe( "Exception occured while displaying balance inquiry details" + e );
                }
            } else {
                if (!isFinishing())
                    showDialog( getResources().getString( R.string.something_wrong ) );
            }
        }

        private void saveBalanceResponse(BalanceInquiryResponse balanceInquiryResponse) {
            Intent intent = new Intent( BalanceInquiryActivity.this, BalanceInquiryReceiptActivity.class );
            Bundle b = new Bundle();
            b.putSerializable( "transaction", confirmTransactionDetailsDTO );
            b.putSerializable( "response", balanceInquiryResponse );
            if (confirmTransactionDetailsDTO.getEntryMode().equals( getResources().getString( R.string.qrsale ) )) {
                b.putString( "requestType", getResources().getString( R.string.qr_sale ) );
            } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase( getString( R.string.manual_type ) )) {
                b.putString( "requestType", getResources().getString( R.string.manual_sale ) );
            } else if (confirmTransactionDetailsDTO.getEntryMode().equals( getString( R.string.contactless ) ) || confirmTransactionDetailsDTO.getEntryMode().equals( "PAN_SWIPE_CONTACTLESS" )) {
                b.putString( "requestType", getResources().getString( R.string.nfc_sale ) );
            } else if (confirmTransactionDetailsDTO.getTxnType().equals( getResources().getString( R.string.balance_inquiry ) )) {
                b.putString( "requestType", getResources().getString( R.string.balance_inquiry ) );
            }
            intent.putExtras( b );
            try {
                saveData( balanceInquiryResponse );
            } catch (Exception e) {
                logger.severe( "Exception" + e );
                Toast.makeText( BalanceInquiryActivity.this, getResources().getString( R.string.error_transaction ), Toast.LENGTH_SHORT ).show();
            }
            startActivity( intent );
            finish();
        }
    }

    private void showDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( getResources().getString( R.string.error ) )
                .setMessage( errorMessage )
                .setCancelable( false )
                .setPositiveButton( getResources().getString( R.string.ok ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent( BalanceInquiryActivity.this, SlidingMenuActivity.class );
                        startActivity( intent );
                        finish();
                    }
                } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveData(BalanceInquiryResponse response) throws GenericException {

        DataStoreHelper helper = new DataStoreHelper( this );
        Transaction trans = new Transaction();
        if (response.getCustomerBalance() != null || !response.getCustomerBalance().equals( "" )) {
            trans.setTransactionamount( response.getCustomerBalance() );
        } else {
            trans.setTransactionamount( Constants.AMOUNT);
        }
        trans.setTxnRefNumber( response.getTxnId() );
        trans.setTotalamount( response.getCustomerBalance() );
        trans.setEntrymode( confirmTransactionDetailsDTO.getEntryMode() );
        trans.setInvoiceno( invoiceNumber + "" );
        trans.setTxnDateTime( response.getDeviceLocalTxnTime() );
        trans.setStatus( response.getErrorMessage() );
        trans.setMerchantCode( response.getMerchantId() );
        trans.setCardHolderName( confirmTransactionDetailsDTO.getCardholderName() );
        trans.setExpDate( confirmTransactionDetailsDTO.getExpDate() );
        trans.setMaskedCardNumber( Utils.getMaskedCardNumberLastFourDigit( confirmTransactionDetailsDTO.getCardNumber() ) );

        if (confirmTransactionDetailsDTO.getEntryMode().equals( getResources().getString( R.string.qrsale ) )) {
            trans.setTransactionType( getResources().getString( R.string.qr_sale ) );
        } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase( getString( R.string.manual_type ) )) {
            trans.setTransactionType( getResources().getString( R.string.manual_sale ) );
        } else if (confirmTransactionDetailsDTO.getEntryMode().equalsIgnoreCase( getString( R.string.contactless ) ) || confirmTransactionDetailsDTO.getEntryMode().equals( "PAN_SWIPE_CONTACTLESS" )) {
            trans.setTransactionType( getResources().getString( R.string.nfc_sale ) );
        } else if (confirmTransactionDetailsDTO.getTxnType().equals( getString( R.string.balance_inquiry ) )) {
            trans.setTransactionType( getString( R.string.balance_inquiry ) );
        }
        trans.setMerchantName( merchantName );
        helper.addTransaction( trans );
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( R.string.nfc_disabled );
        builder.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent( Settings.ACTION_WIRELESS_SETTINGS );
                startActivity( intent );
            }
        } );
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        } );
        builder.create().show();
        return;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch( this );
            mAdapter.disableForegroundNdefPush( this );
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent( intent );
        tagData = resolveIntent( intent );
    }

    private boolean validate() {

        cardNumber = panEditText1.getText().toString().trim() + panEditText2.getText().toString().trim()
                + panEditText3.getText().toString().trim() + panEditText4.getText().toString().trim();

        if (!UIValidation.isPanNotEmpty( panEditText1.getText().toString() )) {
            panEditText1.setError( getResources().getString( R.string.pan_validation ) );
            return false;
        }
        if (!UIValidation.isPanNotEmpty( panEditText2.getText().toString() )) {
            panEditText2.setError( getResources().getString( R.string.pan_validation ) );
            return false;
        }
        if (!UIValidation.isPanNotEmpty( panEditText3.getText().toString() )) {
            panEditText3.setError( getResources().getString( R.string.pan_validation ) );
            return false;
        }
        if (!UIValidation.isPanNotEmpty( panEditText4.getText().toString() )) {
            panEditText4.setError( getResources().getString( R.string.pan_validation ) );
            return false;
        }
        if (!UIValidation.isPanValid( cardNumber )) {
            Toast.makeText( this, getString( R.string.error_cc ), Toast.LENGTH_SHORT ).show();
            return false;
        }

        String expDate = expDateEditText.getText().toString();
        if (!UIValidation.isExpDateNotEmpty( expDate )) {

            expDateEditText.setError( getResources().getString( R.string.exp_validation ) );
            return false;
        }

        String cvvValue = cvv.getText().toString();
        if (!UIValidation.isCVVEmpty( cvvValue )) {
            cvv.setError( getResources().getString( R.string.cvv_validation ) );
            return false;
        }

        int calYear = Calendar.getInstance().get( Calendar.YEAR );
        // Extract month and year
        String exp = expDate.trim();
        String month = exp.substring( Constants.TWO );
        String year = exp.substring( 0, Constants.TWO );
        int m = 0, y = 0;
        try {
            m = Integer.valueOf( month );
            y = Integer.valueOf( year );
        } catch (Exception exception) {
            Toast.makeText( this, getString( R.string.error_exp ), Toast.LENGTH_SHORT ).show();
            logger.severe( "Expiry date is invalid" + exception );
            return false;
        }

        calYear = Integer.valueOf( (calYear + "").substring( Constants.TWO ) );
        if (m > Constants.TWELVE || y < calYear) {
            Toast.makeText( this, getString( R.string.error_exp ), Toast.LENGTH_SHORT ).show();
            return false;
        }

        if (UIValidation.isCardHolderNameNotEmpty( cardHolderName.getText().toString() )) {
            return true;
        } else {
            cardHolderName.setError( getResources().getString( R.string.name_validation ) );
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( BalanceInquiryActivity.this, SlidingMenuActivity.class );
        startActivity( intent );
        overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void addTextListeners() {

        panEditText1.addTextChangedListener( new GenericTextWatcher( panEditText1 ) );
        panEditText2.addTextChangedListener( new GenericTextWatcher( panEditText2 ) );
        panEditText3.addTextChangedListener( new GenericTextWatcher( panEditText3 ) );
        panEditText4.addTextChangedListener( new GenericTextWatcher( panEditText4 ) );
        expDateEditText.addTextChangedListener( new GenericTextWatcher( expDateEditText ) );
        cardHolderName.addTextChangedListener( new GenericTextWatcher( cardHolderName ) );
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
                case R.id.panEditText1:
                    if (s != null && s.length() == Constants.FOUR) {
                        panEditText2.requestFocus();
                    }
                    break;
                case R.id.panEditText2:
                    initilizePanEditText2( s );
                    break;
                case R.id.panEditText3:
                    initilizePanEditText3( s );
                    break;
                case R.id.panEditText4:
                    initilizePanEditText4( s );
                    break;
                case R.id.expDateEditText:
                    requestFocus( s );
                    break;
                case R.id.cardHolderName:
                    requestFocus( s );
                    break;
                default:
                    break;
            }
        }

        private void initilizePanEditText2(Editable s) {

            if (s != null && s.length() == Constants.FOUR) {
                panEditText3.requestFocus();
            } else if (s == null || s.length() == 0 || s.toString().equals( "" )) {
                panEditText1.requestFocus( View.FOCUS_LEFT );
            }
        }

        private void initilizePanEditText3(Editable s) {
            if (s != null && s.length() == Constants.FOUR) {
                panEditText4.requestFocus();
            } else if (s == null || s.length() == 0 || s.toString().equals( "" )) {
                if (panEditText2.getText().toString() == null ||
                        panEditText2.getText().toString().equals( "" ) ||
                        panEditText2.getText().toString().length() == 0) {
                    panEditText1.requestFocus( View.FOCUS_LEFT );
                } else {
                    panEditText2.requestFocus( View.FOCUS_LEFT );
                }
            }
        }

        private void initilizePanEditText4(Editable s) {

            if (s != null && s.length() == Constants.FOUR) {
                expDateEditText.requestFocus();
            } else if (s == null || s.length() == 0 || s.toString().equals( "" )) {
                if (panEditText3.getText().toString().equals( "" )
                        || panEditText3.getText().toString() == null ||
                        panEditText3.length() == 0) {
                    panEditText2.requestFocus( View.FOCUS_LEFT );
                } else if ((panEditText3.getText().toString().equals( "" )
                        || panEditText3.getText().toString() == null ||
                        panEditText3.length() == 0)
                        && (panEditText2.getText().toString() == null ||
                        panEditText2.getText().toString().equals( "" ) ||
                        panEditText2.getText().toString().length() == 0)) {
                    panEditText1.requestFocus( View.FOCUS_LEFT );
                } else {
                    panEditText3.requestFocus( View.FOCUS_LEFT );
                }
            }
        }

        private void requestFocus(Editable s) {
            if (s != null && s.length() == Constants.FOUR) {
                cardHolderName.requestFocus();
            }
        }
    }

    //Read  card data in z-90 device
    private void ntagest() {

        handler.setShowLog( true );
        settings.m1Request();
        String resp06 = settings.m1ReadBlock( Constants.BLOCK06 );
        if (resp06 != null && resp06.length() > Constants.EIGHT) {
            resp06 = resp06.substring( Constants.TWO, Constants.EIGHT );
            resp06 = resp06.replaceAll( "fe", "" );
        }
        String resp07 = settings.m1ReadBlock( Constants.BLOCK07 );
        if (resp07 != null && resp07.length() > Constants.EIGHT) {
            resp07 = resp07.substring( 0, Constants.EIGHT );
            resp07 = resp07.replaceAll( "fe", "" );
        }
        String resp08 = settings.m1ReadBlock( Constants.BLOCK08 );
        if (resp08 != null && resp08.length() > Constants.EIGHT) {
            resp08 = resp08.substring( 0, Constants.EIGHT );
            resp08 = resp08.replaceAll( "fe", "" );
        }
        String resp09 = settings.m1ReadBlock( Constants.BLOCK09 );
        if (resp09 != null && resp09.length() > Constants.EIGHT) {
            resp09 = resp09.substring( 0, Constants.EIGHT );
            resp09 = resp09.replaceAll( "fe", "" );
        }
        String resp0a = settings.m1ReadBlock( Constants.BLOCK0a );
        if (resp0a != null && resp0a.length() > Constants.EIGHT) {
            resp0a = resp0a.substring( 0, Constants.EIGHT );
            resp0a = resp0a.replaceAll( "fe", "" );
        }
        String resp0b = settings.m1ReadBlock( Constants.BLOCK0b );
        if (resp0b != null && resp0b.length() > Constants.EIGHT) {
            resp0b = resp0b.substring( 0, Constants.EIGHT );
            resp0b = resp0b.replaceAll( "fe", "" );
        }
        String resp0c = settings.m1ReadBlock( Constants.BLOCK0c );
        if (resp0c != null && resp0c.length() > Constants.EIGHT) {
            resp0c = resp0c.substring( 0, Constants.EIGHT );
            resp0c = resp0c.replaceAll( "fe", "" );
        }
        String resp0d = settings.m1ReadBlock( Constants.BLOCK0d );
        if (resp0d != null && resp0d.length() > Constants.EIGHT) {
            resp0d = resp0d.substring( 0, Constants.EIGHT );
            resp0d = resp0d.replaceAll( "fe", "" );
        }
        String resp0e = settings.m1ReadBlock( Constants.BLOCK0e );
        if (resp0e != null && resp0e.length() > Constants.EIGHT) {
            resp0e = resp0e.substring( 0, Constants.EIGHT );
            resp0e = resp0e.replaceAll( "fe", "" );
        }

        if (resp06 != null && resp07 != null && resp08 != null && resp09 != null && resp0a != null && resp0b != null && resp0c != null && resp0d != null && resp0e != null) {
            String cardData = resp06 + resp07 + resp08 + resp09 + resp0a + resp0b + resp0c + resp0d + resp0e;
            encrypted = CommonUtils.hexToString( cardData );
            desfireCardUid = settings.m1Request();
            tagData.setNfcData( encrypted );
            tagData.setCardUid( desfireCardUid );
            logger.severe( "card information from z90 is " + encrypted );

        } else {
            showMessage( R.string.error, R.string.no_card_data );
        }
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.close();
            flag = false;
            nfcFlag = false;
        }
        super.onDestroy();
    }

}

