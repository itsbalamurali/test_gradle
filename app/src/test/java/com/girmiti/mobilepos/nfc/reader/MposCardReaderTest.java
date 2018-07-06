package com.girmiti.mobilepos.nfc.reader;

import android.nfc.Tag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 13-Oct-2017 3:00:00 pm
 * @version 1.0
 */
public class MposCardReaderTest {

    @InjectMocks
    MposCardReader mposCardReader;

    @Mock
    MposCardReader.DataCallback dataCallback;

    @Mock
    Tag tag;

    @Before
    public void setUp() throws Exception{
        mposCardReader = new MposCardReader(dataCallback);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testMposCardReader()  {
        mposCardReader.buildSelectApdu("aid");
        mposCardReader.buildReadRecordApdu("data");
        mposCardReader.buildGPOForVISA(100,10,0,"unPredictNum");
        mposCardReader.BuildGPOForMC();
        mposCardReader.BuildComputeCryptoChecksumApdu("unPredictNum",10,0);
        mposCardReader.getRandomNumberInRange(0,1);
        mposCardReader.toDate("13-10-2017","pattern");
        mposCardReader.onTagDiscovered(tag);
        Assert.assertNotNull(mposCardReader);
    }

}