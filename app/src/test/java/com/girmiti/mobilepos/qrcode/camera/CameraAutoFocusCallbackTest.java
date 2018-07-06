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
 * @date 16-Oct-17 01:30:00 pm
 * @version 1.0
 */
public class CameraAutoFocusCallbackTest {

    @InjectMocks
    CameraAutoFocusCallback cameraAutoFocusCallback = new CameraAutoFocusCallback();

    @Mock
    Handler autoFocusHandler;

    @Mock
    Camera camera;

    @Test
    public void testSetHandler() {
        cameraAutoFocusCallback.setHandler(autoFocusHandler, 1);
    }

    @Test(expected = RuntimeException.class)
    public void testOnAutoFocus() {
        cameraAutoFocusCallback.onAutoFocus(true, camera);
    }
}
