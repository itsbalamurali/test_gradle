package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class RefundRequestTest {

    @InjectMocks
    RefundRequest refundRequest;

    @Before
    public void setRefundRequest() throws Exception {

        refundRequest = new RefundRequest();

        refundRequest.setTerminalId("001");
        refundRequest.setTxnType("manual");
        refundRequest.setCgRefNumber("001");
        refundRequest.setMerchantId("01");
        refundRequest.setCardNum("111111");
        refundRequest.setExpDate("190717");
        refundRequest.setInvoiceNumber("1111");
        refundRequest.setMerchantAmount("120");
        refundRequest.setTotalTxnAmount("150");
        refundRequest.setTxnRefNumber("10101");
        refundRequest.createRequest();
    }

    @Test
    public void testRefundRequest() throws Exception {
        Assert.assertEquals("001", refundRequest.getTerminalId());
        Assert.assertEquals("manual", refundRequest.getTxnType());
        Assert.assertEquals("001", refundRequest.getCgRefNumber());
        Assert.assertEquals("01", refundRequest.getMerchantId());
        Assert.assertEquals("111111", refundRequest.getCardNum());
        Assert.assertEquals("190717", refundRequest.getExpDate());
        Assert.assertEquals("1111", refundRequest.getInvoiceNumber());
        Assert.assertEquals("120", refundRequest.getMerchantAmount());
        Assert.assertEquals("150", refundRequest.getTotalTxnAmount());
        Assert.assertEquals("10101", refundRequest.getTxnRefNumber());

    }
}