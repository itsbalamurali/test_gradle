package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class LoadFundRequestTest {

    @InjectMocks
    LoadFundRequest loadFundRequest;

    @Before
    public void setLoadFundRequest() throws Exception {

        loadFundRequest = new LoadFundRequest();

        loadFundRequest.setTxnType("NFC");
        loadFundRequest.setTerminalId("001");
        loadFundRequest.setMerchantId("0101");
        loadFundRequest.setAccountNumber("123");
        loadFundRequest.setEntryMode("Type");
        loadFundRequest.setMobileNumber("8888899999");
        loadFundRequest.setTotalTxnAmount(100L);

        loadFundRequest.createRequest();
    }

    @Test
    public void testLoadFundRequest() throws Exception {
        Assert.assertEquals("NFC", loadFundRequest.getTxnType());
        Assert.assertEquals("001", loadFundRequest.getTerminalId());
        Assert.assertEquals("0101", loadFundRequest.getMerchantId());
        Assert.assertEquals("123", loadFundRequest.getAccountNumber());
        Assert.assertEquals("Type", loadFundRequest.getEntryMode());
        Assert.assertEquals("8888899999", loadFundRequest.getMobileNumber());
        Assert.assertEquals("100", loadFundRequest.getTotalTxnAmount().toString());
    }
}