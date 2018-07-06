package com.girmiti.mobilepos.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPointCallback;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 11:10:00 pm
 * @version 1.0
 */
public class DecodeThreadTest {

    @InjectMocks
    DecodeThread decodeThread;

    @Mock
    CaptureActivity activity;

    @Mock
    ArrayList<BarcodeFormat> decodeFormats;

    @Mock
    String characterSet;

    @Mock
    ResultPointCallback resultPointCallback;

    @Test(expected = RuntimeException.class)
    public void testDecodeThread() {
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet, resultPointCallback);
        decodeThread.run();
        decodeThread.getHandler();
        Assert.assertNotNull(decodeThread);
    }
}