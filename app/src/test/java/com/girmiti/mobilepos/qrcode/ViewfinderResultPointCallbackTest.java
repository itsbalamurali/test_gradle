package com.girmiti.mobilepos.qrcode;

import com.google.zxing.ResultPoint;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 5:00:00 pm
 * @version 1.0
 */
public class ViewfinderResultPointCallbackTest {

    @InjectMocks
    ViewfinderResultPointCallback viewfinderResultPointCallback;

    @Mock
    ViewfinderView viewfinderView;

    @Mock
    ResultPoint point;

    @Test(expected = NullPointerException.class)
    public void testViewfinderResultPointCallback() {
        viewfinderResultPointCallback = new ViewfinderResultPointCallback(viewfinderView);
        viewfinderResultPointCallback.foundPossibleResultPoint(point);
        Assert.assertNotNull(viewfinderResultPointCallback);
    }
}