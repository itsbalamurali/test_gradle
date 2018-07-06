package com.girmiti.mobilepos.qrcode;

import android.os.Message;

import com.google.zxing.DecodeHintType;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 10:30:00 am
 * @version 1.0
 */
public class DecodeHandlerTest {

    @InjectMocks
    DecodeHandler decodeHandler;

    @Mock
    CaptureActivity activity;

    @Mock
    HashMap<DecodeHintType, Object> hints;

    @Mock
    Message message;

    @Test(expected = RuntimeException.class)
    public void testDecodeHandler() {
        decodeHandler = new DecodeHandler(activity, hints);
        decodeHandler.handleMessage(message);
        Assert.assertNotNull(decodeHandler);
    }
}