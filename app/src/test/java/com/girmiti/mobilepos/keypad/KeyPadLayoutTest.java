package com.girmiti.mobilepos.keypad;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 10:00:00 am
 * @version 1.0
 */
public class KeyPadLayoutTest {

    @InjectMocks
    KeyPadLayout keyPadLayout;

    @Mock
    Context context;

    @Mock
    AttributeSet attrs;

    @Mock
    int defStyleAttr;

    @Mock
    int defStyleRes;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Test(expected = RuntimeException.class)
    public void testKeyPadLayout() throws Exception {
        keyPadLayout = new KeyPadLayout(context);
        keyPadLayout = new KeyPadLayout(context, attrs);
        keyPadLayout = new KeyPadLayout(context, attrs, defStyleAttr);
        keyPadLayout = new KeyPadLayout(context, attrs, defStyleAttr, defStyleRes);
        keyPadLayout.initializeKeyViews();
        Assert.assertNotNull(keyPadLayout);
    }
}