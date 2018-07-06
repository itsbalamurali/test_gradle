package com.girmiti.mobilepos.qrcode.camera;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.girmiti.mobilepos.qrcode.config.ZXingLibConfig;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-17 10:15:00 am
 * @version 1.0
 */
public class CameraManagerTest {

    @InjectMocks
    CameraManager cameraManager;

    @Mock
    Context context;

    @Mock
    ZXingLibConfig zXingLibConfig;

    @Mock
    SurfaceHolder holder;

    @Mock
    Handler handler;

    @Mock
    Rect rect;

    @Mock
    PlanarYUVLuminanceSource planarYUVLuminanceSource;

    @Mock
    byte[] data;

    @Test(expected = NumberFormatException.class)
    public void testInit() {
        cameraManager.init(context, zXingLibConfig);
    }

    @Test(expected = NullPointerException.class)
    public void testOpenDriver() throws IOException {
        cameraManager.openDriver(holder);
    }

    @Test(expected = NullPointerException.class)
    public void testCloseDriver() throws IOException {
        cameraManager.closeDriver();
    }

    @Test(expected = NullPointerException.class)
    public void testStartPreview() throws IOException {
        cameraManager.startPreview();
    }

    @Test(expected = NullPointerException.class)
    public void testRequestPreviewFrame() throws IOException {
        cameraManager.requestPreviewFrame(handler, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testRequestAutoFocus() throws IOException {
        cameraManager.requestAutoFocus(handler, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFramingRect() throws IOException {
        rect = cameraManager.getFramingRect();
        Assert.assertNotNull(rect);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFramingRectInPreview() throws IOException {
        rect = cameraManager.getFramingRectInPreview();
        Assert.assertNotNull(rect);
    }

    @Test(expected = NullPointerException.class)
    public void testSetManualFramingRect() throws IOException {
        cameraManager.setManualFramingRect(1, 2);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildLuminanceSource() throws IOException {
        planarYUVLuminanceSource = cameraManager.buildLuminanceSource(data, 1, 1);
    }
}