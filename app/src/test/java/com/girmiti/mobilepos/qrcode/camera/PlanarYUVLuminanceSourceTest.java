package com.girmiti.mobilepos.qrcode.camera;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-17 01:00:00 pm
 * @version 1.0
 */
public class PlanarYUVLuminanceSourceTest {

    byte[] data;

    @InjectMocks
    PlanarYUVLuminanceSource planarYUVLuminanceSource = new PlanarYUVLuminanceSource(data, 5, 6, 1, 1, 1, 1, true);

    @Test(expected = IllegalArgumentException.class)
    public void testGetRow() throws Exception {
        planarYUVLuminanceSource.getRow(1, data);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMatrix() throws Exception {
        data = planarYUVLuminanceSource.getMatrix();
        Assert.assertNotNull(data);
    }

    @Test
    public void testIsCropSupported() throws Exception {
        Boolean crop = planarYUVLuminanceSource.isCropSupported();
        Assert.assertTrue(crop);
    }

    @Test(expected = NullPointerException.class)
    public void testRenderCroppedGreyscaleBitmap() throws Exception {
        Bitmap bitmap = planarYUVLuminanceSource.renderCroppedGreyscaleBitmap();
        Assert.assertNotNull(bitmap);
    }
}