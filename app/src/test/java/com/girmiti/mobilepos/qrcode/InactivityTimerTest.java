package com.girmiti.mobilepos.qrcode;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 02:20:00 pm
 * @version 1.0
 */
public class InactivityTimerTest {

    @InjectMocks
    InactivityTimer inactivityTimer;

    @Mock
    Activity activity;

    @Test(expected = RuntimeException.class)
    public void testInactivityTimer() {
        inactivityTimer = new InactivityTimer(activity);
        inactivityTimer.onResume();
        inactivityTimer.onPause();
        inactivityTimer.shutdown();
        Assert.assertNotNull(inactivityTimer);
    }

}