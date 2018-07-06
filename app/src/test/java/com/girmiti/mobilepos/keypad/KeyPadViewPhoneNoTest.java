package com.girmiti.mobilepos.keypad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.girmiti.mobilepos.logger.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 12:00:00 am
 * @version 1.0
 */
public class KeyPadViewPhoneNoTest {

    @InjectMocks
    KeyPadViewPhoneNo keyPadViewPhoneNo;

    @Mock
    Context context;

    @Mock
    AttributeSet attrs;

    @Mock
    int defStyleAttr;

    @Mock
    View view;

    @Mock
    private Logger logger;

    @Test(expected = RuntimeException.class)
    public void testKeyPadViewPhoneNo() throws Exception {
        logger = getNewLogger(KeyPadViewPhoneNo.class.getName());
        keyPadViewPhoneNo = new KeyPadViewPhoneNo(context);
        keyPadViewPhoneNo = new KeyPadViewPhoneNo(context, attrs);
        keyPadViewPhoneNo = new KeyPadViewPhoneNo(context, attrs, defStyleAttr);
        keyPadViewPhoneNo.onClick(view);
        keyPadViewPhoneNo.getInputText();
        keyPadViewPhoneNo.refreshAmount();
        Assert.assertNotNull(keyPadViewPhoneNo);
    }
}