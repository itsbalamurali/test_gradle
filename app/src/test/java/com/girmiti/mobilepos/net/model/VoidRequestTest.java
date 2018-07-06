package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class VoidRequestTest {

    @InjectMocks
    VoidRequest voidRequest;

    @Before
    public void setVoidRequest() throws Exception {

        voidRequest = new VoidRequest();

        voidRequest.setTxnRefNumber("100");
        voidRequest.setTerminalId("001");
        voidRequest.setTxnType("NFC");
        voidRequest.setCgRefNumber("001");
        voidRequest.setMerchantId("001");
        voidRequest.createRequest();
    }

    @Test
    public void testVoidRequest() throws Exception {
        Assert.assertEquals("100", voidRequest.getTxnRefNumber());
        Assert.assertEquals("001", voidRequest.getTerminalId());
        Assert.assertEquals("NFC", voidRequest.getTxnType());
        Assert.assertEquals("001", voidRequest.getCgRefNumber());
        Assert.assertEquals("001", voidRequest.getMerchantId());
    }
}