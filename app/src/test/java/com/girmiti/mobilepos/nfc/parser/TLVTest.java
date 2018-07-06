package com.girmiti.mobilepos.nfc.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 13-Oct-2017 11:00:00 am
 * @version 1.0
 */
public class TLVTest {

    @InjectMocks
    TLV tlv;

    @Mock
    TLVException tlvException;

    byte[] mValue;

    @Before
    public void setUp() throws Exception {
        mValue = new byte[]{0x03, 0x05, 0x07};
        tlvException = new TLVException();
    }

    @Test
    public void testTLV() throws TLVException {
        tlv = new TLV(mValue);
        Assert.assertNotNull(tlv);
        Assert.assertNotNull(tlv.getTag());
        Assert.assertNotNull(tlv.getValue());
        Assert.assertNotNull(tlv.getChildren());
        Assert.assertTrue(tlv.isConstructed());
    }
}