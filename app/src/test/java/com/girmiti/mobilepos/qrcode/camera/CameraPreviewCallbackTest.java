package com.girmiti.mobilepos.qrcode.camera;

import android.hardware.Camera;
import android.os.Handler;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-17 11:00:00 am
 * @version 1.0
 */
public class CameraPreviewCallbackTest {

    @Mock
    CameraConfigurationManager configManager;

    @InjectMocks
    CameraPreviewCallback cameraPreviewCallback = new CameraPreviewCallback(configManager, true);

    @Mock
    Handler handler;

    @Mock
    byte[] data;

    @Mock
    Camera camera;

    @Test
    public void testSetHandler() throws Exception {
        cameraPreviewCallback.setHandler(handler, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testOnPreviewFrame() throws Exception {
        cameraPreviewCallback.onPreviewFrame(data, camera);
    }
}