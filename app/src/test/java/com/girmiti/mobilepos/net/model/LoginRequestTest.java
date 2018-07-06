package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class LoginRequestTest {

    @InjectMocks
    LoginRequest loginRequest;


    @Before
    public void setLoginRequest() throws Exception {

        loginRequest = new LoginRequest();

        loginRequest.setPassword("password");
        loginRequest.setTerminalId("001");
        loginRequest.setUserName("aravind");
        loginRequest.setDeviceSerial("111");
        loginRequest.setMerchantId("111");
        loginRequest.setCurrentAppVersion("1");
        loginRequest.setTxnType("Manual");
        loginRequest.createRequest();
    }

    @Test
    public void testLoginRequest() throws Exception {
        Assert.assertEquals("password", loginRequest.getPassword());
        Assert.assertEquals("001", loginRequest.getTerminalId());
        Assert.assertEquals("aravind", loginRequest.getUserName());
        Assert.assertEquals("111", loginRequest.getDeviceSerial());
        Assert.assertEquals("111", loginRequest.getMerchantId());
        Assert.assertEquals("1", loginRequest.getCurrentAppVersion());
        Assert.assertEquals("Manual", loginRequest.getTxnType());
    }
}