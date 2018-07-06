package com.girmiti.mobilepos.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.girmiti.mobilepos.BuildConfig;
import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.NetworkException;
import com.girmiti.mobilepos.net.ServerCommunication;
import com.girmiti.mobilepos.net.model.LoginRequest;
import com.girmiti.mobilepos.net.model.LoginResponse;
import com.girmiti.mobilepos.util.CurrencyFormat;
import com.girmiti.mobilepos.util.Utils;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * Created by nayan on 13/12/16.
 */

public class LoginActivity extends BaseActivity {

    private ProgressDialog progress;
    private EditText username;
    private EditText passwd;
    private CurrencyFormat currencyFormat;
    private SharedPreferences.Editor editor;
    private String versionName;
    private String selectedLanguage;
    private Logger logger = getNewLogger( LoadMoneyPhoneNoActivity.class.getName() );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs;
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        selectedLanguage = getResources().getConfiguration().locale.getLanguage();
        prefs = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        editor = prefs.edit();
        currencyFormat = new CurrencyFormat( this );
        username = (EditText) findViewById( R.id.username );
        passwd = (EditText) findViewById( R.id.password );
        findViewById( R.id.signin_btn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(  );
            }
        } );

        findViewById( R.id.forgot ).setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                forgotPassword();
            }
        } );
    }

    public void login( ) {
        try {
            validate();
            new LoginTask().execute();
        } catch (Exception e) {
            displayErrorDialogInHandler( e.getMessage(), null );
            logger.info( "Validation Error" + e );
        }
    }

    private void forgotPassword() {
        Intent forgotPasswordIntent = new Intent( this, ForgotPasswordActivity.class );
        startActivity( forgotPasswordIntent );
        overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
    }

    //mobile side Validation
    private void validate() throws GenericException {
        Editable editable = username.getText();
        if (editable == null || "".equals( editable.toString() ))
            throw new GenericException( getResources().getString( R.string.enter_username ) );
        editable = passwd.getText();
        if (editable == null || "".equals( editable.toString() ))
            throw new GenericException( getResources().getString( R.string.user_password ) );
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( Intent.ACTION_MAIN );
        intent.addCategory( Intent.CATEGORY_HOME );
        startActivity( intent );
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.requestFocus();
    }

    private class LoginTask extends AsyncTask<Void, Void, LoginResponse> {

        String uName = null;
        String uPasswd = null;
        String nwException = getString( R.string.error_generic );

        @Override
        protected void onPreExecute() {
            uName = username.getText().toString().trim();
            uPasswd = passwd.getText().toString().trim();
            progress = new ProgressDialog( LoginActivity.this );
            progress.setMessage( getResources().getString( R.string.processing ) );
            progress.setCancelable( false );
            progress.show();
        }

        @Override
        protected LoginResponse doInBackground(Void... params) {
            LoginResponse resp = null;
            try {
                TelephonyManager tManager = (TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE );
                LoginRequest loginReq = new LoginRequest();
                versionName = String.valueOf( BuildConfig.VERSION_NAME );
                loginReq.setUserName( uName );
                loginReq.setPassword( uPasswd );
                loginReq.setDeviceSerial( tManager.getDeviceId() );
                loginReq.setCurrentAppVersion( versionName );
                loginReq.setTimeZoneOffset(Utils.currentTimeZone());
                loginReq.setTimeZoneRegion(Utils.currentTimeZoneinGmt());
                resp = ServerCommunication.getInstance().login( loginReq, selectedLanguage );
            } catch (NetworkException ne) {
                nwException = ne.getMessage();
                logger.info( "Network Error" + ne );
            } catch (Exception e) {
                nwException = e.getMessage();
                logger.info( "Network Error" + e );
            }
            return resp;
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {

            // login with tms
            if (progress != null)
                progress.dismiss();
            progress = null;

            if (loginResponse != null) {
                if (loginResponse.getErrorCode() != null && "GEN_001".equals( loginResponse.getErrorCode() ) && (loginResponse.getTerminalData() != null) && loginResponse.getCurrencyDTO() != null) {
                    processLogin( loginResponse );
                } else if (loginResponse.getErrorCode() != null && "GEN_002".equals( loginResponse.getErrorCode() )) {
                    displayErrorDialogInHandler( loginResponse.getErrorMessage(), null );
                } else if (loginResponse.getCurrencyDTO() == null) {
                    displayErrorDialogInHandler( getString( R.string.currency_error ), null );
                } else {
                    displayErrorDialogInHandler( nwException, null );
                }
            } else {
                displayErrorDialogInHandler( nwException, null );
            }
        }

        private void showConfirmDialog(final LoginResponse loginResponse) {

            // Get the layout inflater
            LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
            View view = inflater.inflate( R.layout.update_details_dialog, null );

            final String url = loginResponse.getTerminalData().getUpdateURL();
            String versionNo = loginResponse.getTerminalData().getUpdateVersion();
            TextView textView = (TextView) view.findViewById( R.id.updateDetail );
            textView.setText( getResources().getString( R.string.new_update_available ) + versionNo + " " + getResources().getString( R.string.please_click_for_update ) );

            AlertDialog.Builder builder = new AlertDialog.Builder( LoginActivity.this );
            builder.setView( view )
                    .setPositiveButton( R.string.update, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            username.setText( "" );
                            passwd.setText( "" );
                            Intent i = new Intent( Intent.ACTION_VIEW );
                            i.setData( Uri.parse( url ) );
                            String versionNo = loginResponse.getTerminalData().getUpdateVersion();
                            editor.putString( "versionnumber", versionNo );
                            editor.commit();
                            startActivity( i );
                            finish();
                            overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );

                        }
                    } );

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside( false );
            dialog.show();
        }

        public void processLogin(LoginResponse loginResponse) {
                if ("success".equals( loginResponse.getTerminalData().getErrorMessage() ) && loginResponse.getTerminalData().getUpdateURL() != null) {
                    showConfirmDialog( loginResponse );
                } else if ("No device found".contains( loginResponse.getTerminalData().getErrorMessage() )) {

                    AlertDialog alertDialog = new AlertDialog.Builder( LoginActivity.this ).create();
                    alertDialog.setTitle( getResources().getString( R.string.alert ) );
                    alertDialog.setMessage( getResources().getString( R.string.device_not_registered ) );
                    alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, getResources().getString( R.string.ok ), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            passwd.setText( "" );
                            dialog.dismiss();
                        }
                    } );
                    alertDialog.show();
                } else if ((getResources().getString( R.string.no_terminal_id_for_merchant )).equals( loginResponse.getTerminalData().getErrorMessage() )) {

                    AlertDialog alertDialog = new AlertDialog.Builder( LoginActivity.this ).create();
                    alertDialog.setTitle( getResources().getString( R.string.alert ) );
                    alertDialog.setMessage( getResources().getString( R.string.no_terminal_id_for_merchant ) );
                    alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, getResources().getString( R.string.ok ), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            passwd.setText( "" );
                            dialog.dismiss();
                        }
                    } );
                    alertDialog.show();

                } else {
                    editor.putString( "versionnumber", versionName );
                    editor.commit();
                    Intent intent = new Intent( LoginActivity.this, SlidingMenuActivity.class );
                    startActivity( intent );
                    overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
                }
                currencyFormat.setCurrencyData( loginResponse );
                editor.putString( getResources().getString( R.string.key_merchant ), loginResponse.getMerchantCode() );
                editor.putString( getResources().getString( R.string.key_terminal ), loginResponse.getTerminalData().getTerminalId() );
                editor.putString( getResources().getString( R.string.key_name ), loginResponse.getBussinessName() );
                editor.putString( getResources().getString( R.string.key_email ), uName );
                editor.putString( getResources().getString( R.string.key_address ), loginResponse.getAddress() );
                editor.putString( getResources().getString( R.string.key_city ), loginResponse.getCity() );
                editor.putString( getResources().getString( R.string.key_state ), loginResponse.getState() );
                editor.commit();

                overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );

        }

    }
}