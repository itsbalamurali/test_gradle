package com.girmiti.mobilepos.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.net.model.TagData;
import com.girmiti.mobilepos.util.CommonUtils;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
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
 * Created by Aravind on 21-10-2017.
 */
public class CardTapActivity extends BaseActivity {

    private final Logger logger = getNewLogger( CardTapActivity.class.getName() );
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
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private String encrypted = null;
    private static final String TOTALAMT = "total_amount";
    private static final String TIPAMT = "tip_amount";
    private static final String SALEAMT = "sale_amount";
    private String desfireCardUid;
    private TagData tagData = null;
    MposHandler handler;
    com.imagpay.Settings settings;
    boolean flag = false;
    boolean magFlag = false;
    boolean nfcFlag = false;
    private Button readBtn;
    private SharedPreferences prefs;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        ImageView leftArrow;
        TextView tvToolbar;
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_balance_inquiry );
        readBtn = (Button) findViewById( R.id.resetBtn );

        prefs = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );

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
        tvToolbar.setText( R.string.cardtap );

        leftArrow = (ImageView) findViewById( img_left_toolbar_arrow );
        leftArrow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right );
            }
        } );

        Intent intent = getIntent();
        if (intent != null) {
            txnAmount = intent.getStringExtra( TOTALAMT );
            tipAmount = intent.getStringExtra( TIPAMT );
            saleAmount = intent.getStringExtra( SALEAMT );
        }
        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();

        panEditText1 = (EditText) findViewById( R.id.panEditText1 );
        panEditText2 = (EditText) findViewById( R.id.panEditText2 );
        panEditText3 = (EditText) findViewById( R.id.panEditText3 );
        panEditText4 = (EditText) findViewById( R.id.panEditText4 );

        expDateEditText = (EditText) findViewById( R.id.expDateEditText );
        cardHolderName = (EditText) findViewById( R.id.cardHolderName );
        cvvEditText = (EditText) findViewById( R.id.cvv );
        findViewById( R.id.proceedBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
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
                getResources();
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
            ntagest();
        } else {
            handler.close();
            nfcFlag = false;
            handler.connect();
        }
    }

    private void initialize() {
        String entryMode;

        if (validate()) {
            confirmTransactionDetailsDTO.setTransactionAmount( Utils.getFormattedAmount( saleAmount ) );
            confirmTransactionDetailsDTO.setAmount( Utils.getFormattedAmount( txnAmount ) );
            confirmTransactionDetailsDTO.setTipAmount( Utils.getFormattedAmount( tipAmount ) );
            confirmTransactionDetailsDTO.setCardholderName( cardHolderName.getText().toString().trim() );
            confirmTransactionDetailsDTO.setCardNumber( cardNumber );
            confirmTransactionDetailsDTO.setExpDate( expDateEditText.getText().toString().trim() );
            confirmTransactionDetailsDTO.setEmvField55( "" );
            confirmTransactionDetailsDTO.setTrack2( "" );
            confirmTransactionDetailsDTO.setCvv( cvvEditText.getText().toString().trim() );
            confirmTransactionDetailsDTO.setCardType( CommonUtils.getCCType( cardNumber ) );
            confirmTransactionDetailsDTO.setUid( desfireCardUid );

            entryMode = getString( R.string.card_tap );
            confirmTransactionDetailsDTO.setEntryMode( entryMode );
            showConfirmDialog( confirmTransactionDetailsDTO );
        }
    }

    private void showConfirmDialog(final ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO) {

        LayoutInflater inflater = CardTapActivity.this.getLayoutInflater();
        View view = inflater.inflate( R.layout.manual_entry_confirm_dialog, null );
        ((TextView) view.findViewById( R.id.card_number )).setText( com.girmiti.mobilepos.util.Utils.getMaskedCardNumber( confirmTransactionDetailsDTO.getCardNumber() ) );
        ((TextView) view.findViewById( R.id.exp_date )).setText( expDateEditText.getText().toString().trim() );
        ((TextView) view.findViewById( R.id.card_holder )).setText( confirmTransactionDetailsDTO.getCardholderName() );
        ((TextView) view.findViewById( R.id.amount )).setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( confirmTransactionDetailsDTO.getAmount() ) ) );

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder( CardTapActivity.this );
        builder.setView( view )
                .setPositiveButton( R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent( CardTapActivity.this, TransactionResultActivity.class );
                        intent.putExtra( getString( R.string.transaction_details ), confirmTransactionDetailsDTO );
                        startActivity( intent );
                        overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
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
        }
        cardInput();
    }

    private void cardInput() {

        String desfireCardNumber = null;
        String desfireExpDate = null;
        String desfireCardHolderName = null;
        if (tagData != null) {
            desfireCardUid = tagData.getCardUid();
            encrypted = tagData.getNfcData();
            if (encrypted != null) {
                desfireCardNumber = encrypted.substring( 0, Constants.SIXTEEN );
                desfireExpDate = encrypted.substring( Constants.SIXTEEN, Constants.TWENTY );
                desfireCardHolderName = encrypted.substring( Constants.TWENTY );

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
                    String exp = 0 + desfireExpDate;
                    expDateEditText.setText( exp );
                    expDateEditText.setError( null );
                }

                if (desfireCardHolderName.length() != 0) {
                    cardHolderName.setText( desfireCardHolderName );
                    cardHolderName.setError( null );
                }
            }
        }
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

        String cvv = cvvEditText.getText().toString();
        if (!UIValidation.isCVVEmpty( cvv )) {
            cvvEditText.setError( getResources().getString( R.string.cvv_validation ) );
            return false;
        }

        int calYear = Calendar.getInstance().get( Calendar.YEAR );
        // Extract month and year
        String exp = expDate.trim();
        String month = exp.substring( Constants.TWO );
        String year = exp.substring( 0, Constants.TWO );
        int m = 0;
        int y = 0;
        try {
            m = Integer.valueOf( month );
            y = Integer.valueOf( year );
        } catch (Exception exception) {
            logger.info( "Exception :::" + exception );
            Toast.makeText( this, getString( R.string.error_exp ), Toast.LENGTH_SHORT ).show();
            return false;
        }

        calYear = Integer.valueOf( (Integer.toString( calYear )).substring( Constants.TWO ) );
        if (m > Constants.TOTAL_MONTH || y < calYear) {
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
        overridePendingTransition( R.anim.anim_slide_in_right, R.anim.anim_slide_out_right );
    }

    private void addTextListeners() {

        panEditText1.addTextChangedListener( new TextWatcher() {

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

        } );

        panEditText2.addTextChangedListener( new TextWatcher() {

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
                    panEditText1.requestFocus( View.FOCUS_LEFT );
                }
            }
        } );

        panEditText3.addTextChangedListener( new TextWatcher() {

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
                initilizePanEditText3( s );
            }
        } );

        panEditText4.addTextChangedListener( new TextWatcher() {

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
                initilizePanEditText4( s );
            }

        } );

        expDateEditText.addTextChangedListener( new GenericTextWatcher( expDateEditText ) );
        cardHolderName.addTextChangedListener( new GenericTextWatcher( cardHolderName ) );
    }

    private void initilizePanEditText3(Editable s) {
        if (s != null && s.length() == Constants.FOUR) {
            panEditText4.requestFocus();
        } else if (s == null || s.length() == 0) {
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
        } else if (s == null || s.length() == 0) {
            if (panEditText3.getText().toString().equals( "" ) || panEditText3.getText().toString() == null || panEditText3.length() == 0) {
                panEditText2.requestFocus( View.FOCUS_LEFT );
            } else if ((panEditText3.getText().toString().equals( "" ) || panEditText3.getText().toString() == null || panEditText3.length() == 0) && (panEditText2.getText().toString() == null || panEditText2.getText().toString().equals( "" ) || panEditText2.getText().toString().length() == 0)) {
                panEditText1.requestFocus( View.FOCUS_LEFT );
            } else {
                panEditText3.requestFocus( View.FOCUS_LEFT );
            }
        }
    }

    private void clearEditTexts() {

        panEditText1.setText( "" );
        panEditText2.setText( "" );
        panEditText3.setText( "" );
        panEditText4.setText( "" );
        expDateEditText.setText( "" );
        cardHolderName.setText( "" );
        cvvEditText.setText( "" );
        cvvEditText.requestFocus();
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
                    requestFocus( s );
                    break;
                case R.id.cardHolderName:
                    requestFocus( s );
                    break;
                default:
                    break;
            }
        }

        private void requestFocus(Editable s) {
            if (s != null && s.length() == Constants.FOUR) {
                cardHolderName.requestFocus();
            }
        }
    }

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