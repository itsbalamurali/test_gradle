package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.BalanceInquiryResponse;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;
import com.imagpay.mpos.MposHandler;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;
import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 18/10/17.
 */
public class BalanceInquiryReceiptActivity extends BaseActivity {

    private TextView txtTranNo;
    private TextView txtTerminalId;
    private TextView txtMerchantId;
    private TextView txtMerchantName;
    private TextView txtTotalAmount;
    private TextView txtDateTime;
    private TextView txtStatus;
    private TextView txtType;
    private TextView txtTranxnAmount;
    private TextView txtMaskedCardNumber;
    private TextView txtCardHolderName;
    private ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;
    private BalanceInquiryResponse response;
    private SharedPreferences prefs;
    private TextView txtAddress;
    private TextView txtCity;
    private TextView txtState;
    private TextView txtBalance;
    private TextView txtRefNo;
    private Handler handler = new Handler();
    private String str = "en";
    private final Logger logger = getNewLogger( BalanceInquiryActivity.class.getName() );
    MposHandler mposHandler;
    com.imagpay.Settings settings;
    private String requestType;
    private String date;
    private Button home;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_balance_receipt );
        ImageView leftArrow;
        TextView tvToolbar;
        tvToolbar = (TextView) findViewById( R.id.tvToolbar_left );
        tvToolbar.setText( R.string.balance_inquiry );

        leftArrow = (ImageView) findViewById( img_left_toolbar_arrow );
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
        txtRefNo = (TextView) findViewById( R.id.txtRefNo );
        txtTranNo = (TextView) findViewById( R.id.txtTranNo );
        txtTerminalId = (TextView) findViewById( R.id.txtTerminalId );
        txtMerchantId = (TextView) findViewById( R.id.txtMerchantId );
        txtTotalAmount = (TextView) findViewById( R.id.txtTotalAmount );
        txtDateTime = (TextView) findViewById( R.id.txtDateTime );
        txtStatus = (TextView) findViewById( R.id.txtStatus );
        txtType = (TextView) findViewById( R.id.txtType );
        txtMaskedCardNumber = (TextView) findViewById( R.id.txtmasked_card_number );
        txtCardHolderName = (TextView) findViewById( R.id.card_holder_name );
        txtBalance = (TextView) findViewById( R.id.txtAvailableBalance );
        txtTranxnAmount = (TextView) findViewById( R.id.txtTranAmount );
        txtAddress = (TextView) findViewById( R.id.txtAddress );
        txtCity = (TextView) findViewById( R.id.txtCity );
        txtState = (TextView) findViewById( R.id.txtState );
        txtMerchantName = (TextView) findViewById( R.id.txtMerchantName );
        home=(Button)findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages( null );
                Intent intent = new Intent( getApplicationContext(), SlidingMenuActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            confirmTransactionDetailsDTO = (ConfirmTransactionDetailsDTO) b.getSerializable( "transaction" );
            response = (BalanceInquiryResponse) b.getSerializable( "response" );
            requestType = (String) b.getString( "requestType" );
            logger.severe( "requestType" + requestType );
            populateData( requestType );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Utils.clearTop( BalanceInquiryReceiptActivity.this );
                Intent intent = new Intent( BalanceInquiryReceiptActivity.this, SlidingMenuActivity.class );
                startActivity( intent );
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void populateData(String requestType) {

        txtTranNo.setText( response.getTxnId() );
        txtRefNo.setText( response.getProcTxnId() );
        setStatusMessage();
        if (requestType != null) {
            setRequestType( requestType );
        } else {
            txtType.setText( getString( R.string.sale ) );
        }
        txtMaskedCardNumber.setText( Utils.getMaskedCardNumberLastFourDigit( confirmTransactionDetailsDTO.getCardNumber() ) );
        txtCardHolderName.setText( confirmTransactionDetailsDTO.getCardholderName() );
        txtTranxnAmount.setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( response.getCustomerBalance() ) ) );
        txtDateTime.setText(response.getDeviceLocalTxnTime());
        if (requestType!=null && requestType.equalsIgnoreCase( getResources().getString( R.string.balance_inquiry ) )) {
            txtBalance.setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( response.getCustomerBalance() ) ) );
        }
        txtTotalAmount.setText( CurrencyFormat.getFormattedCurrency( Utils.getFormattedAmount( response.getCustomerBalance() ) ) );
        saveData();
        terminateReceipt();
    }

    private void terminateReceipt() {

        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( BalanceInquiryReceiptActivity.this.getApplicationContext(), SlidingMenuActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                BalanceInquiryReceiptActivity.this.startActivity( intent );
            }
        }, Constants.DELAY );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.clearTop( BalanceInquiryReceiptActivity.this );
        Intent intent = new Intent( getApplicationContext(), SlidingMenuActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
    }

    private void setStatusMessage() {
        if (str.equals( "es" )) {
            String statusMessage = response.getErrorMessage();
            if (statusMessage.equals( getResources().getString( R.string.status ) )) {
                txtStatus.setText( R.string.aprobado );
            }
        } else {
            txtStatus.setText( response.getErrorMessage() );
        }
    }

    private void setRequestType(String requestType) {
        if (str.equalsIgnoreCase( "es" )) {
            if (requestType.contains( getString( R.string.sale ) )) {
                txtType.setText( getString( R.string.venta ) );
            }
            if (requestType.equalsIgnoreCase( getString( R.string.refund ) )) {
                txtType.setText( getString( R.string.reembolo ) );
            }
        } else {
            if (requestType.equalsIgnoreCase( getString( R.string.balance_inquiry ) )) {
                txtType.setText( getString( R.string.balance_inquiry ) );
            }
        }
    }

    private void saveData() {
        try {
            String merchant = prefs.getString( getResources().getString( R.string.key_merchant ), null );
            String terminal = prefs.getString( getResources().getString( R.string.key_terminal ), null );
            String address = prefs.getString( getResources().getString( R.string.key_address ), null );
            String city = prefs.getString( getResources().getString( R.string.key_city ), null );
            String state = prefs.getString( getResources().getString( R.string.key_state ), null );
            String name = prefs.getString( getResources().getString( R.string.key_name ), null );

            if (name != null && !name.equals( "" )) {
                txtMerchantName.setText( name );
            } else {
                txtMerchantName.setText( getResources().getString( R.string.merchant_text ) );
            }
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
            if (address != null && !address.equals( "" )) {
                txtAddress.setText( address );
            } else {
                txtAddress.setText( getResources().getString( R.string.merchant_address ) );
            }
            if (city != null && !city.equals( "" )) {
                txtCity.setText( city );
            } else {
                txtCity.setText( getResources().getString( R.string.merchant_city ) );
            }

            if (state != null && !state.equals( "" )) {
                txtState.setText( state );
            } else {
                txtState.setText( getResources().getString( R.string.merchant_city ) );
            }

        } catch (Exception e) {
            logger.severe( "Exception inside BalanceInquiryRecipt" + e );
        }
    }
}
