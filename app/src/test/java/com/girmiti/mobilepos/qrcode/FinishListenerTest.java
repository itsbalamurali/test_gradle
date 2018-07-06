package com.girmiti.mobilepos.qrcode;

import android.app.Activity;
import android.content.DialogInterface;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 17-Oct-2017 11:40:00 am
 * @version 1.0
 */
public class FinishListenerTest {

    @InjectMocks
    FinishListener finishListener;

    @Mock
    Activity activityToFinish;

    @Mock
    DialogInterface dialogInterface;

    @Test(expected = NullPointerException.class)
    public void testFinishListener() {
        finishListener = new FinishListener(activityToFinish);
        finishListener.onClick(dialogInterface, 1);
        finishListener.onCancel(dialogInterface);
        Assert.assertNotNull(finishListener);
    }
}