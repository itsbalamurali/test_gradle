package com.girmiti.mobilepos.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.zxing.ResultPoint;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 3:40:00 pm
 * @version 1.0
 */
public class ViewfinderViewTest {

    @InjectMocks
    ViewfinderView viewfinderView;

    @Mock
    Context context;

    @Mock
    AttributeSet attrs;

    @Mock
    Canvas canvas;

    @Mock
    Bitmap barcode;

    @Mock
    ResultPoint point;

    @Test(expected = RuntimeException.class)
    public void testViewfinderView() throws Exception {
        viewfinderView = new ViewfinderView(context,attrs);
        viewfinderView.drawViewfinder();
        viewfinderView.drawResultBitmap(barcode);
        viewfinderView.addPossibleResultPoint(point);
        Assert.assertNotNull(viewfinderView);
    }

}