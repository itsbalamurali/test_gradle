package com.girmiti.mobilepos.qrcode.config;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-17 04:45:00 pm
 * @version 1.0
 */

public class ZXingLibConfigTest {

    @InjectMocks
    ZXingLibConfig zXingLibConfig = new ZXingLibConfig();

    @Test
    public void test() {
        Assert.assertFalse(zXingLibConfig.copyToClipboard);
        Assert.assertFalse(zXingLibConfig.vibrateOnDecoded);
        Assert.assertTrue(zXingLibConfig.playBeepOnDecoded);
        Assert.assertFalse(zXingLibConfig.useFrontLight);
        Assert.assertFalse(zXingLibConfig.reverseImage);
    }
}