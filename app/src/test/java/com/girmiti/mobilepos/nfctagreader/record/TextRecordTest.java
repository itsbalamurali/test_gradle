package com.girmiti.mobilepos.nfctagreader.record;

import android.nfc.NdefRecord;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-17 10:00:00 am
 * @version 1.0
 */
public class TextRecordTest {

    @InjectMocks
    TextRecord textRecord;

    @Mock
    NdefRecord ndefRecord;

    @Test(expected = NullPointerException.class)
    public void testParse() {
        String parseString = textRecord.parse(ndefRecord);
        Assert.assertNotNull(parseString);
    }

    @Test(expected = NullPointerException.class)
    public void testIsText() {
        Boolean isText = textRecord.isText(ndefRecord);
        Assert.assertTrue(isText);
    }
}