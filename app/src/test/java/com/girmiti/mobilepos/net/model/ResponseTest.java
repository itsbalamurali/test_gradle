package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/*
 * Created by aravind on 20-07-2017.
 */

public class ResponseTest {

    String expected;
    @InjectMocks

    Response response;

    @Before
    public void BeforeTestMethod() throws Exception {

        expected = "{\n" +
                "\t\"errorCode\": \"404\",\n" +
                "\t\"errorMessage\": \"Approved\",\n" +
                "\t\"deviceLocalTxnTime\": \"1500544445870\",\n" +
                "\t\"txnRefNumber\": \"681417424841\",\n" +
                "\t\"authId\": \"122471\",\n" +
                "\t\"cgRefNumber\": \"2000002130\",\n" +
                "\t\"merchantCode\": \"156165263616431\",\n" +
                "\t\"merchantName\": \"Ipsidy\"\n" +
                "}";
        response = new Response(expected.toString());
        Assert.assertNotNull(response);
    }

    @Test
    public void testResponse() throws Exception {

        Assert.assertEquals("404", response.getErrorCode());
        Assert.assertEquals("Approved", response.getErrorMessage());
        Assert.assertEquals("1500544445870", response.getDeviceLocalTxnTime());
        Assert.assertEquals("681417424841", response.getTxnRefNumber());
        Assert.assertEquals("122471", response.getAuthId());
        Assert.assertEquals("2000002130", response.getCgRefNumber());
        Assert.assertEquals("156165263616431", response.getMerchantCode());
        Assert.assertEquals("Ipsidy", response.getMerchantName());
    }
}
