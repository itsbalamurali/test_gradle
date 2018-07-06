package com.girmiti.mobilepos.net.model;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/*
 * Created by aravind on 20-07-2017.
 */

public class SaleRequestTest {

    @InjectMocks
    SaleRequest saleRequest;

    BillingData billingData;
    CardData cardData;

    @Mock
    JSONObject requestObject;

    @Before
    public void setSaleRequest() throws Exception {
        saleRequest = new SaleRequest();
        billingData = new BillingData();
        cardData = new CardData();
        saleRequest.setMerchantId("100");
        saleRequest.setTxnType("NFC");
        saleRequest.setTerminalId("111");
        saleRequest.setBillingData(billingData);
        saleRequest.setCardData(cardData);
        saleRequest.setEntryMode("new");
        saleRequest.setFeeAmount("5");
        saleRequest.setInvoiceNumber("123456");
        saleRequest.setMerchantAmount("50");
        saleRequest.setMerchatntName("Decathlon");
        saleRequest.setOrderId("new");
        saleRequest.setQrCode("QR");
        saleRequest.setRegisterNumber("45654");
        saleRequest.setTotalTxnAmount("500");
        saleRequest.setCardData(cardData);
        saleRequest.setBillingData(billingData);
        saleRequest.createRequest();
    }

    @Test
    public void testSaleRequest() throws Exception {
        Assert.assertEquals("100", saleRequest.getMerchantId());
        Assert.assertEquals("NFC", saleRequest.getTxnType());
        Assert.assertEquals("111", saleRequest.getTerminalId());
        Assert.assertEquals("new", saleRequest.getEntryMode());
        Assert.assertEquals("5", saleRequest.getFeeAmount());
        Assert.assertEquals("123456", saleRequest.getInvoiceNumber());
        Assert.assertEquals("50", saleRequest.getMerchantAmount());
        Assert.assertEquals("Decathlon", saleRequest.getMerchatntName());
        Assert.assertEquals("new", saleRequest.getOrderId());
        Assert.assertEquals("QR", saleRequest.getQrCode());
        Assert.assertEquals("45654", saleRequest.getRegisterNumber());
        Assert.assertEquals("500", saleRequest.getTotalTxnAmount());
        Assert.assertEquals(cardData, saleRequest.getCardData());
        Assert.assertEquals(billingData, saleRequest.getBillingData());
    }
}
