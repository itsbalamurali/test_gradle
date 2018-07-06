package com.girmiti.mobilepos.qrcode;

import android.os.Message;

import com.google.zxing.BarcodeFormat;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 3:30:00 pm
 * @version 1.0
 */
public class CaptureActivityHandlerTest {

    @InjectMocks
    CaptureActivityHandler captureActivityHandler;

    @Mock
    CaptureActivity activity;

    @Mock
    ArrayList<BarcodeFormat> decodeFormats;

    @Mock
    String characterSet;

    @Mock
    Message message;

    @Test(expected = RuntimeException.class)
    public void testCaptureActivityHandler() {
        captureActivityHandler = new CaptureActivityHandler(activity, decodeFormats, characterSet);
        captureActivityHandler.handleMessage(message);
        captureActivityHandler.quitSynchronously();
        Assert.assertNotNull(captureActivityHandler);
    }
}