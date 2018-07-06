package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class OAuthTokenTest {

    @InjectMocks
    OAuthToken oAuthToken;

    @Before
    public void setoAuthToken() throws Exception {

        oAuthToken = new OAuthToken();

        oAuthToken.setAccess_token("Token");
        oAuthToken.setExpires_in(10);
        oAuthToken.setRefresh_token("100");
        oAuthToken.setToken_type("New");
    }

    @Test
    public void testoAuthToken() throws Exception {
        Assert.assertEquals("Token", oAuthToken.getAccess_token());
        Assert.assertEquals((Integer) 10, oAuthToken.getExpires_in());
        Assert.assertEquals("100", oAuthToken.getRefresh_token());
        Assert.assertEquals("New", oAuthToken.getToken_type());
    }
}