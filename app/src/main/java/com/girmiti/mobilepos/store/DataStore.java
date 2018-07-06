package com.girmiti.mobilepos.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Girmiti on 24/10/16.
 */
public class DataStore extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID 			= "_id";
    public static final String COLUMN_ENTRY_MODE    = "entry_mode";
    public static final String COLUMN_INVOICE_NUMBER = "invoice_no";
    public static final String COLUMN_TRANSACTION_AMOUNT    = "trans_amount";
    public static final String COLUMN_TRANSACTION_FEE       = "trans_fee";
    public static final String COLUMN_TOTAL_AMOUNT          = "total_amount";
    public static final String COLUMN_CG_REF_NUMBER       = "cg_ref_no";
    public static final String COLUMN_ORDER_ID              = "order_id";
    public static final String COLUMN_STATUS                = "status";
    public static final String COLUMN_SIGNATURE_DATA        = "signature";
    public static final String COLUMN_MERCHANT_NAME         = "merchant_name";
    public static final String COLUMN_MERCHANT_ID           = "merchant_id";
    public static final String COLUMN_MERCHANT_ADDRESS      = "merchant_address";
    public static final String COLUMN_MERCHANT_EMAIL        = "merchant_email";
    public static final String COLUMN_MERCHANT_MOBILE       = "merchanr_mobile";
    public static final String COLUMN_TIP_ENABLE            = "tip_enable";
    public static final String COLUMN_TAX_PERCENTAGE        = "tax_percentage";
    public static final String COLUMN_IP                    = "ip_address";
    public static final String COLUMN_PORT                  = "port_number";
    public static final String COLUMN_PROTOCOL              = "protocol";
    public static final String COLUMN_TERMINAL_ID           = "terminal_id";
    public static final String COLUMN_TIMESTAMP             = "timestamp";
    public static final String COLUMN_TRANSACTION_REF_NUMBER = "txn_ref_no";
    public static final String COLUMN_AUTH_ID               = "auth_id";
    public static final String COLUMN_CARD_HOLDER_NAME      = "cardholder_name";
    public static final String COLUMN_CARD_EXPIRY_DATE      = "exp_date";
    public static final String COLUMN_MASKED_CARD_NUMBER    = "card_number";
    public static final String COLUMN_TIP_AMOUNT            = "tip_amount";
    public static final String COLUMN_TRANSACTION_TYPE      = "transaction_type";

    public static final String TABLE_TRANSACTION        = "trans_data";
    public static final String TABLE_MERCHANT_SETTINGS  = "merchant_settings";
    public static final String TABLE_GENERAL_SETTINGS   = "gen_settings";
    public static final String TABLE_TRANSACTION_SETTINGS = "trans_settings";
    public static final String VARCHAR = " VARCHAR, ";

    private static final String TABLE_TRANSACTION_CREATE = "create table "
            + TABLE_TRANSACTION
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ENTRY_MODE + VARCHAR
            + COLUMN_INVOICE_NUMBER + VARCHAR
            + COLUMN_TRANSACTION_AMOUNT + VARCHAR
            + COLUMN_TRANSACTION_FEE + VARCHAR
            + COLUMN_TOTAL_AMOUNT + VARCHAR
            + COLUMN_CG_REF_NUMBER + VARCHAR
            + COLUMN_TIMESTAMP + VARCHAR
            + COLUMN_TRANSACTION_REF_NUMBER + VARCHAR
            + COLUMN_AUTH_ID + VARCHAR
            + COLUMN_STATUS + VARCHAR
            + COLUMN_MERCHANT_ID + VARCHAR
            + COLUMN_CARD_HOLDER_NAME + VARCHAR
            + COLUMN_CARD_EXPIRY_DATE + VARCHAR
            + COLUMN_MASKED_CARD_NUMBER + VARCHAR
            + COLUMN_TIP_AMOUNT + VARCHAR
            + COLUMN_TRANSACTION_TYPE + VARCHAR
            + COLUMN_MERCHANT_NAME + VARCHAR
            + COLUMN_SIGNATURE_DATA + " BLOB);";

    public DataStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_TRANSACTION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This method to Upgrade the SQLite db
    }
}
