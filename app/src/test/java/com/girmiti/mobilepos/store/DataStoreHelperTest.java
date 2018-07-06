package com.girmiti.mobilepos.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 12-Oct-2017 2:0:00 pm
 * @version 1.0
 */
public class DataStoreHelperTest {

    @InjectMocks
    DataStoreHelper dataStoreHelper;

    @Mock
    Context context;

    @Mock
    SQLiteDatabase database;

    @Mock
    DataStore dbHelper;

    @Mock
    Transaction transaction;

    @Test(expected = RuntimeException.class)
    public void testDataStoreHelper() {
        dataStoreHelper = new DataStoreHelper(context);
        dataStoreHelper.retrieveTransactions();
        dataStoreHelper.addTransaction(transaction);
        dataStoreHelper.deleteAllTransaction();
        dataStoreHelper.updateTransaction(transaction);
        Assert.assertNotNull(dataStoreHelper);
    }
}