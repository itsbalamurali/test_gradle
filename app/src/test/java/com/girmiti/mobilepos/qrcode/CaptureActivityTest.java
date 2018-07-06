package com.girmiti.mobilepos.qrcode;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.Window;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 2:30:00 pm
 * @version 1.0
 */
public class CaptureActivityTest {

    @InjectMocks
    CaptureActivity captureActivity;

    @Mock
    Bundle bundle;

    @Mock
    Window window;

    @Mock
    KeyEvent event;

    @Mock
    SurfaceHolder holder;

    @Test(expected = RuntimeException.class)
    public void testCaptureActivity() {
        captureActivity = new CaptureActivity();
        captureActivity.surfaceCreated(holder);
        captureActivity.surfaceDestroyed(holder);
        captureActivity.surfaceChanged(holder, 1, 1, 1);
        captureActivity.getViewfinderView();
        captureActivity.getHandler();
        captureActivity.onCreate(bundle);
        captureActivity.onResume();
        captureActivity.onPause();
        captureActivity.onDestroy();
        captureActivity.onKeyDown(1, event);
        Assert.assertNotNull(captureActivity);
    }
}