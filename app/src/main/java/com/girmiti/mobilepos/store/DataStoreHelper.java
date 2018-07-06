package com.girmiti.mobilepos.store;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.girmiti.mobilepos.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girmiti on 24/10/16.
 */
public class DataStoreHelper {

    private Context context;
    private SQLiteDatabase database;
    private DataStore dbHelper;

    public DataStoreHelper(Context c) {
        this.context = c;

        dbHelper = new DataStore(context);
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
        if (database.isOpen())
            database.close();
    }

    public List<Transaction> retrieveTransactions() {
        open();

        ArrayList<Transaction> transactionList;

        Cursor c = database.rawQuery("select *"
                + " from " + DataStore.TABLE_TRANSACTION + " order by " + DataStore.COLUMN_TIMESTAMP + " desc"
                + ";", null);


        transactionList = new ArrayList<>();

        while (c.moveToNext()) {
            Transaction transaction = new Transaction();
            transaction.setEntrymode(c.getString(1));
            transaction.setInvoiceno(c.getString(Constants.TWO));
            transaction.setTransactionamount(c.getString(Constants.THREE));
            transaction.setTransactionfee(c.getString(Constants.FOUR));
            transaction.setTotalamount(c.getString(Constants.FIVE));
            transaction.setCgRefNumber(c.getString(Constants.SIX));
            transaction.setTxnDateTime(c.getString(Constants.SEVEN));
            transaction.setTxnRefNumber(c.getString(Constants.EIGHT));
            transaction.setAuthId(c.getString(Constants.NINE));
            transaction.setStatus(c.getString(Constants.TEN));
            transaction.setMerchantCode(c.getString(Constants.ELEVEN));
            transaction.setCardHolderName(c.getString(Constants.TWELVE));
            transaction.setExpDate(c.getString(Constants.THIRTEEN));
            transaction.setMaskedCardNumber(c.getString(Constants.FOURTEEN));
            transaction.setTipAmount(c.getString(Constants.FIFTEEN));
            transaction.setTransactionType(c.getString(Constants.SIXTEEN));
            transaction.setMerchantName(c.getString(Constants.SEVENTEEN));
            transaction.setSignature(c.getBlob(Constants.EIGHTEEN));

            transactionList.add(transaction);
        }

        c.close();
        close();
        return transactionList;
    }

    public void addTransaction(Transaction trans){
        open();
        SQLiteStatement sqlStmnt = database.compileStatement("insert into " + DataStore.TABLE_TRANSACTION
                + "(" + DataStore.COLUMN_ENTRY_MODE + " , "
                + DataStore.COLUMN_INVOICE_NUMBER + " , "
                + DataStore.COLUMN_TRANSACTION_AMOUNT + " , "
                + DataStore.COLUMN_TRANSACTION_FEE + " , "
                + DataStore.COLUMN_TOTAL_AMOUNT + " , "
                + DataStore.COLUMN_CG_REF_NUMBER + " , "
                + DataStore.COLUMN_TIMESTAMP + " , "
                + DataStore.COLUMN_TRANSACTION_REF_NUMBER + " , "
                + DataStore.COLUMN_AUTH_ID + " , "
                + DataStore.COLUMN_STATUS + " ,"
                + DataStore.COLUMN_MERCHANT_ID + " ,"
                + DataStore.COLUMN_CARD_HOLDER_NAME + " ,"
                + DataStore.COLUMN_CARD_EXPIRY_DATE + " ,"
                + DataStore.COLUMN_MASKED_CARD_NUMBER + " ,"
                + DataStore.COLUMN_TIP_AMOUNT + " , "
                + DataStore.COLUMN_TRANSACTION_TYPE + " , "
                + DataStore.COLUMN_MERCHANT_NAME + " , "
                + DataStore.COLUMN_SIGNATURE_DATA + ") "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        sqlStmnt.bindString(1, checkNull(trans.getEntrymode()));
        sqlStmnt.bindString(Constants.TWO, checkNull(trans.getInvoiceno()));
        sqlStmnt.bindString(Constants.THREE, checkNull(trans.getTransactionamount()));
        sqlStmnt.bindString(Constants.FOUR, checkNull(trans.getTransactionfee()));
        sqlStmnt.bindString(Constants.FIVE, checkNull(trans.getTotalamount()));
        sqlStmnt.bindString(Constants.SIX, checkNull(trans.getCgRefNumber()));
        sqlStmnt.bindString(Constants.SEVEN, checkNull(trans.getTxnDateTime()));
        sqlStmnt.bindString(Constants.EIGHT, checkNull(trans.getTxnRefNumber()));
        sqlStmnt.bindString(Constants.NINE, checkNull(trans.getAuthId()));
        sqlStmnt.bindString(Constants.TEN, checkNull(trans.getStatus()));
        sqlStmnt.bindString(Constants.ELEVEN, checkNull(trans.getMerchantCode()));
        sqlStmnt.bindString(Constants.TWELVE, checkNull(trans.getCardHolderName()));
        sqlStmnt.bindString(Constants.THIRTEEN, checkNull(trans.getExpDate()));
        sqlStmnt.bindString(Constants.FOURTEEN, checkNull(trans.getMaskedCardNumber()));
        sqlStmnt.bindString(Constants.FIFTEEN, checkNull(trans.getTipAmount()));
        sqlStmnt.bindString(Constants.SIXTEEN, checkNull(trans.getTransactionType()));
        sqlStmnt.bindString(Constants.SEVENTEEN, checkNull(trans.getMerchantName()));
        sqlStmnt.bindBlob(Constants.EIGHTEEN, trans.getSignature() == null? new byte[1] : trans.getSignature());

        sqlStmnt.executeInsert();

        sqlStmnt.close();

        close();
    }

    public void deleteAllTransaction(){
        open();
        SQLiteStatement sqlStmnt = database.compileStatement("DELETE FROM " + DataStore.TABLE_TRANSACTION);
        sqlStmnt.executeUpdateDelete();
        sqlStmnt.close();
        close();
    }

    private String checkNull(String data) {
        if (data == null) {
            return "";
        }

        return data;
    }

    public void updateTransaction(Transaction transaction) {

        open();
        SQLiteStatement sqlStmnt = database.compileStatement("update " + DataStore.TABLE_TRANSACTION
                + " SET " + DataStore.COLUMN_STATUS + "='" + transaction.getStatus()
                + "' WHERE " + DataStore.COLUMN_TRANSACTION_REF_NUMBER+ "=" + transaction.getTxnRefNumber() + ";");

        sqlStmnt.executeUpdateDelete();
        sqlStmnt.close();
        close();
    }
}
