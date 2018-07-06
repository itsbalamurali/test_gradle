package com.girmiti.mobilepos.qrcode;

import android.content.Intent;
import android.net.Uri;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 9:45:00 am
 * @version 1.0
 */
public class DecodeFormatManagerTest {

    @InjectMocks
    DecodeFormatManager decodeFormatManager;

    @Mock
    Intent intent;

    @Mock
    Uri inputUri;

    @Test(expected = NullPointerException.class)
    public void testDecodeFormatManager() {
        Assert.assertNotNull(decodeFormatManager.parseDecodeFormats(intent));
        Assert.assertNotNull(decodeFormatManager.parseDecodeFormats(inputUri));
    }
}