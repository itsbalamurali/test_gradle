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
 * @date 16-Oct-2017 3:00:00 pm
 * @version 1.0
 */
public class KeyPadViewTest {

    @InjectMocks
    KeyPadView keyPadView;

    @Mock
    Context context;

    @Mock
    AttributeSet attrs;

    @Mock
    int defStyleAttr;

    @Mock
    int defStyleRes;

    @Mock
    View view;

    @Mock
    private Logger logger;

    @Test(expected = RuntimeException.class)
    public void testKeyPadView() {
        logger = getNewLogger(KeyPadView.class.getName());
        keyPadView = new KeyPadView(context);
        keyPadView = new KeyPadView(context, attrs);
        keyPadView = new KeyPadView(context, attrs, defStyleAttr);
        keyPadView.onClick(view);
        keyPadView.getInputText();
        keyPadView.refreshAmount();
        Assert.assertNotNull(keyPadView);
    }
}