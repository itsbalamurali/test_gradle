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
 * @date 12-Oct-2017 11:30:00 am
 * @version 1.0
 */
public class DataStoreTest {

    @InjectMocks
    DataStore dataStore;

    @Mock
    Context context;

    @Mock
    SQLiteDatabase database;

    @Test(expected = RuntimeException.class)
    public void testDataStore() {
        dataStore = new DataStore(context);
        dataStore.onCreate(database);
        dataStore.onUpgrade(database, 1, 2);
        Assert.assertNotNull(dataStore);
    }
}
