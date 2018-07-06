package com.girmiti.mobilepos.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.TagData;
import com.girmiti.mobilepos.nfctagreader.NdefMessageParser;
import com.girmiti.mobilepos.util.Constants;
import com.girmiti.mobilepos.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class BaseActivity extends AppCompatActivity {

    private Handler customHandler = new Handler();
    private Logger logger = getNewLogger( BaseActivity.class.getName() );
    protected com.jeremyfeinstein.slidingmenu.lib.SlidingMenu menu;
    private LayoutInflater inflater;
    private List<Tag> tags = new ArrayList<>();
    private android.app.AlertDialog mDialog;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        menu = new com.jeremyfeinstein.slidingmenu.lib.SlidingMenu( this );
        inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        prefs = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
        mDialog = new android.app.AlertDialog.Builder( this ).setNeutralButton( "Ok", null ).create();
        if (Utils.hasNFCHardware( BaseActivity.this )) {
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter( this );
            nfcAdapter.setNdefPushMessage( null, this, this );
        }
    }

    public void displayErrorDialogInHandler(final String message, final Intent intent) {
        customHandler.post( new Runnable() {
            @Override
            public void run() {
                BaseActivity.this.displayRibbonMessage( message, intent, inflater.inflate( R.layout.error_ribbon, null ) );
            }
        } );
    }

    public void displaySuccesDialogInHandler(final String message, final Intent intent) {
        customHandler.post( new Runnable() {
            @Override
            public void run() {
                BaseActivity.this.displayRibbonMessage( message, intent, inflater.inflate( R.layout.succes_ribbon, null ) );
            }
        } );
    }

    public void displayRibbonMessage(String message, Intent intent, View layout) {

        PopupWindow popup;
        try {
            popup = new PopupWindow( layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT );
            TextView messageView = (TextView) layout.findViewById( R.id.message );
            final SpannableString s = new SpannableString( message );
            Linkify.addLinks( s, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS );
            messageView.setText( s );
            messageView.setMovementMethod( LinkMovementMethod.getInstance() );
            int y = 0;
            View titleView = findViewById( R.id.toolbar );
            if (titleView != null) {
                y = getTop( titleView ) + titleView.getHeight();
                layout.findViewById( R.id.toolbar ).setVisibility( View.GONE );
                popup.setAnimationStyle( R.style.AnimationErrorPopup );
            }
            popup.showAtLocation( getWindow().getDecorView().getRootView(), Gravity.TOP, 0, y );
            customHandler.postDelayed( new CloseDialog( popup, intent ), Constants.FIVETHOUSAND );
        } catch (Exception e) {
            logger.info( "Error in displaying Success displayRibbonMessage" + e );
        }
    }

    private int getTop(View markedView) {

        int top = markedView.getTop();
        ViewParent parent = markedView.getParent();
        while (parent != null && parent instanceof ViewGroup) {
            top += ((ViewGroup) parent).getTop();
            parent = parent.getParent();
        }
        return top;
    }

    public class CloseDialog implements Runnable {

        private PopupWindow dialog;
        private Intent intent;

        public CloseDialog(PopupWindow dialog, Intent intent) {
            this.dialog = dialog;
            this.intent = intent;
        }

        public void run() {
            try {
                dialog.dismiss();
                dialog = null;

                if (intent != null)
                    startActivity( intent );
            } catch (Exception e) {
                logger.info( "Error in closing Dialog" + e );
            }
        }
    }

    protected void settingMenu(final Activity sourceActivity) {

        menu.setMode( com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.LEFT );
        menu.setTouchModeAbove( com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.LEFT );
        menu.setShadowWidthRes( R.dimen.shadow_width );
        menu.setShadowDrawable( R.drawable.shadow );
        menu.setBehindOffsetRes( R.dimen.slidingmenu_offset );
        menu.setFadeDegree( Constants.FADE_DEGREE );
        menu.attachToActivity( this, com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.SLIDING_CONTENT );

        if (prefs != null && prefs.getBoolean( getResources().getString( R.string.key_balence_enquiry ), true )) {

            menu.setMenu( R.layout.slidingmenu_balance_inquiry );
            LinearLayout balanceInquiry = (LinearLayout) findViewById( R.id.balence_enquiry );
            balanceInquiry.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent( BaseActivity.this, BalanceInquiryActivity.class );
                    startActivity( intent );
                    finish();
                    overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
                }
            } );
        } else {
            menu.setMenu( R.layout.slidermenu );
        }
        LinearLayout sale = (LinearLayout) findViewById( R.id.sale );

        sale.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof SlidingMenuActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, SlidingMenuActivity.class );
            }
        } );


        LinearLayout loadmoney = (LinearLayout) findViewById( R.id.loadmoney );

        loadmoney.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof LoadMoneyActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, LoadMoneyActivity.class );
            }
        } );

        LinearLayout logout = (LinearLayout) findViewById( R.id.logout );

        logout.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmationDialog();

            }
        } );
        LinearLayout history = (LinearLayout) findViewById( R.id.history );

        history.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof TransactionHistoryActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, TransactionHistoryActivity.class );
            }
        } );

        LinearLayout settings = (LinearLayout) findViewById( R.id.settings );

        settings.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof SettingsActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, SettingsListActivity.class );
            }
        } );

        LinearLayout help = (LinearLayout) findViewById( R.id.help );

        help.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof HelpActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, HelpActivity.class );
            }
        } );

        LinearLayout about = (LinearLayout) findViewById( R.id.about );

        about.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sourceActivity instanceof AboutActivity) {
                    menu.toggle();
                    return;
                }
                moveScreen( sourceActivity, AboutActivity.class );
            }
        } );
    }

    protected void confirmationDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( BaseActivity.this, R.style.AlertDialogTheme );
        alertDialogBuilder.setTitle( getResources().getString( R.string.app_name ) );
        alertDialogBuilder.setMessage( getResources().getString( R.string.confirm_msg ) )
                .setCancelable( false )
                .setPositiveButton( getResources().getString( R.string.dialog_logout ), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent( BaseActivity.this, LoginActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                        finish();
                    }
                } )
                .setNegativeButton( getResources().getString( R.string.dialog_cancel ), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                } );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public TagData resolveIntent(Intent intent) {

        String encryptedData = null;
        TagData tagData = new TagData();

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals( action )
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals( action )
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals( action )) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra( NfcAdapter.EXTRA_NDEF_MESSAGES );
            NdefMessage[] msgs;
            String cardId = com.girmiti.mobilepos.nfc.utils.Utils.byteArrayToHexString( intent.getByteArrayExtra( NfcAdapter.EXTRA_ID ) );

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra( NfcAdapter.EXTRA_ID );
                Tag tag = (Tag) intent.getParcelableExtra( NfcAdapter.EXTRA_TAG );
                byte[] payload = dumpTagData( tag ).getBytes();
                NdefRecord record = new NdefRecord( NdefRecord.TNF_UNKNOWN, empty, id, payload );
                NdefMessage msg = new NdefMessage( new NdefRecord[]{record} );
                msgs = new NdefMessage[]{msg};
                tags.add( tag );
            }
            // Setup the views
            encryptedData = buildTagViews( msgs );
            tagData.setCardUid( cardId );
            tagData.setNfcData( encryptedData );
            if (encryptedData == null) {
                showMessage( R.string.error, R.string.no_card_data );
            }
        }
        return tagData;
    }

    public void showMessage(int title, int message) {
        mDialog.setTitle( title );
        mDialog.setMessage( getText( message ) );
        mDialog.show();
    }

    private String buildTagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0) {
            return null;
        }
        String records = NdefMessageParser.parse( msgs[0] );
        return records;
    }

    private String dumpTagData(Tag tag) {

        StringBuilder sb = new StringBuilder();
        String prefix = "android.nfc.tech.";
        sb.append( "Technologies: " );
        for (String tech : tag.getTechList()) {
            sb.append( tech.substring( prefix.length() ) );
            sb.append( ", " );
        }
        sb.delete( sb.length() - Constants.TWO, sb.length() );
        for (String tech : tag.getTechList()) {
            if (tech.equals( MifareClassic.class.getName() )) {
                sb.append( '\n' );
                String type = "Unknown";
                try {
                    MifareClassic mifareTag = null;
                    try {
                        mifareTag = MifareClassic.get( tag );
                    } catch (Exception e) {
                        logger.severe( "Error occured in mifareTag" + e );
                        mifareTag = MifareClassic.get( tag );
                    } finally {
                        mifareTag.close();
                    }

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                        default:
                            break;
                    }
                    sb.append( "Mifare Classic type: " );
                    sb.append( type );
                    sb.append( '\n' );

                    sb.append( "Mifare size: " );
                    sb.append( mifareTag.getSize() + " bytes" );
                    sb.append( '\n' );

                    sb.append( "Mifare sectors: " );
                    sb.append( mifareTag.getSectorCount() );
                    sb.append( '\n' );

                    sb.append( "Mifare blocks: " );
                    sb.append( mifareTag.getBlockCount() );
                } catch (Exception e) {
                    logger.severe( "Error occured in mifareTag" + e );
                    sb.append( "Mifare classic error: " + e.getMessage() );
                }
            }

            if (tech.equals( MifareUltralight.class.getName() )) {
                sb.append( '\n' );
                MifareUltralight mifareUlTag = MifareUltralight.get( tag );
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                    default:
                        break;
                }

                try {
                    mifareUlTag.close();
                } catch (IOException e) {
                    logger.severe("IOException"+e );
                }
                sb.append( "Mifare Ultralight type: " );
                sb.append( type );
            }
        }
        return sb.toString();
    }

    private void moveScreen(Activity source, Class<? extends Activity> destination) {
        Intent intent = new Intent( source, destination );
        startActivity( intent );
        overridePendingTransition( R.anim.anim_slide_in_left, R.anim.anim_slide_out_left );
    }
}
