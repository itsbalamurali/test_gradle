package com.girmiti.mobilepos.store;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 12-Oct-2017 11:00:00 am
 * @version 1.0
 */
public class TransactionTest {

    @InjectMocks
    Transaction transaction;

    @Before
    public void setUp() throws Exception {
        transaction = new Transaction();
        transaction.setEntrymode("SALE");
        transaction.setInvoiceno("96787654");
        transaction.setTransactionamount("100");
        transaction.setTransactionfee("0");
        transaction.setTotalamount("100");
        transaction.setRegno("1");
        transaction.setOrderid("10");
        transaction.setStatus("Approved");
        transaction.setTxnDateTime("1507809585411");
        transaction.setTxnRefNumber("268142710605");
        transaction.setAuthId("646621");
        transaction.setCgRefNumber("2000005458");
        transaction.setMerchantCode("105125315363841");
        transaction.setMaskedCardNumber("************2123");
        transaction.setExpDate("0818");
        transaction.setCardHolderName("Girmiti");
        transaction.setMerchantName("Chatak Merchant");
        transaction.setTipAmount("10");
        transaction.setTransactionType("MANUAL - SALE");
    }

    @Test
    public void testTransaction() throws Exception {
        Assert.assertEquals("SALE", transaction.getEntrymode());
        Assert.assertEquals("96787654", transaction.getInvoiceno());
        Assert.assertEquals("100", transaction.getTransactionamount());
        Assert.assertEquals("0", transaction.getTransactionfee());
        Assert.assertEquals("100", transaction.getTotalamount());
        Assert.assertEquals("1", transaction.getRegno());
        Assert.assertEquals("10", transaction.getOrderid());
        Assert.assertEquals("Approved", transaction.getStatus());
        Assert.assertEquals("1507809585411", transaction.getTxnDateTime());
        Assert.assertEquals("268142710605", transaction.getTxnRefNumber());
        Assert.assertEquals("646621", transaction.getAuthId());
        Assert.assertEquals("2000005458", transaction.getCgRefNumber());
        Assert.assertEquals("105125315363841", transaction.getMerchantCode());
        Assert.assertEquals("************2123", transaction.getMaskedCardNumber());
        Assert.assertEquals("0818", transaction.getExpDate());
        Assert.assertEquals("Girmiti", transaction.getCardHolderName());
        Assert.assertEquals("Chatak Merchant", transaction.getMerchantName());
        Assert.assertEquals("10", transaction.getTipAmount());
        Assert.assertEquals("MANUAL - SALE", transaction.getTransactionType());
    }
}