package com.girmiti.mobilepos.net;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 27-Jul-2017 10:00:00 am
 * @version 1.0
 */
public class NetworkExceptionTest extends Exception {
    private static final long serialVersionUID = 1L;
    Throwable throwable;


    @Test
    public void TestNetworkException() throws Exception {
        NetworkException networkException = new NetworkException();
        assertNotNull(networkException);
    }

    @Test
    public void TestNetworkExeParameater() throws Exception {
        NetworkException networkException = new NetworkException("error details", throwable);
        assertNotNull(networkException);
    }

    @Test
    public void TestNetworkExeThrowable() throws Exception {
        NetworkException networkException = new NetworkException(throwable);
        assertNotNull(networkException);
    }

    @Test
    public void TestNetworkExeString() throws Exception {
        NetworkException networkException = new NetworkException("error details");
        assertNotNull(networkException);
    }
}