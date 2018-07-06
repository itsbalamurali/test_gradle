package com.girmiti.mobilepos.qrcode.camera;

import android.content.Context;
import android.hardware.Camera;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-17 03:15:00 pm
 * @version 1.0
 */
public class CameraConfigurationManagerTest {

    Context context;

    @InjectMocks
    CameraConfigurationManager cameraConfigurationManager = new CameraConfigurationManager(context);

    @Mock
    Camera camera;

    @Mock
    Camera.Parameters parameters;

    @Test(expected = NullPointerException.class)
    public void testInitFromCameraParameters() {
        cameraConfigurationManager.initFromCameraParameters(camera);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDesiredCameraParameters() {
        cameraConfigurationManager.setDesiredCameraParameters(camera);
//        Mockito.when(camera.getParameters()).thenReturn(parameters);
    }
}
