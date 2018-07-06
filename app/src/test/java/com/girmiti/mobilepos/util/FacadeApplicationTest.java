package com.girmiti.mobilepos.util;

import android.content.Context;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 06-Oct-2017 04:30:00 pm
 * @version 1.0
 */
public class FacadeApplicationTest {

    @Mock
    Context context;


    @InjectMocks
    FacadeApplication facadeApplication;


    @Test
    public void getAppContext() throws Exception {
        facadeApplication.getAppContext();
    }

    @Test
    public   void oncreate() throws Exception {
        //facadeApplication.onCreate();
    }
}
