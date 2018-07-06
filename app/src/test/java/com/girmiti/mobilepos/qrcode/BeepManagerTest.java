package com.girmiti.mobilepos.qrcode;

import android.app.Activity;

import com.girmiti.mobilepos.qrcode.config.ZXingLibConfig;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 12:30:00 am
 * @version 1.0
 */
public class BeepManagerTest {

    @InjectMocks
    BeepManager beepManager;

    @Mock
    Activity activity;

    @Mock
    ZXingLibConfig config;

    @Test(expected = NullPointerException.class)
    public void testBeepManager() {
        beepManager = new BeepManager(activity, config);
        beepManager.playBeepSoundAndVibrate();
        Assert.assertNotNull(beepManager);
    }
}