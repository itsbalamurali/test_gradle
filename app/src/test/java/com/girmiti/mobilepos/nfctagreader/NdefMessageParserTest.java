package com.girmiti.mobilepos.nfctagreader;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-17 12:00:00 pm
 * @version 1.0
 */
public class NdefMessageParserTest {

    @InjectMocks
    NdefMessageParser ndefMessageParser;

    @Mock
    NdefMessage ndefMessage;

    @Mock
    NdefRecord[] recordsArray;

    @Test(expected = NullPointerException.class)
    public void testParse() {
        String parseString = ndefMessageParser.parse(ndefMessage);
        Assert.assertNotNull(parseString);
    }

    @Test(expected = NullPointerException.class)
    public void testIsText() {
        String records = ndefMessageParser.getRecords(recordsArray);
        Assert.assertNotNull(records);
    }
}
