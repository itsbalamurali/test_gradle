package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class ConfirmTransactionDetailsDTOTest {

    @InjectMocks
    ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;

    @Before
    public void setConfirmTransactionDetailsDTO() throws Exception {

        confirmTransactionDetailsDTO = new ConfirmTransactionDetailsDTO();

        confirmTransactionDetailsDTO.setAmount("100");
        confirmTransactionDetailsDTO.setCardholderName("Aravind");
        confirmTransactionDetailsDTO.setCardNumber("Gir492");
        confirmTransactionDetailsDTO.setCardType("Contactless");
        confirmTransactionDetailsDTO.setCvv("115");
        confirmTransactionDetailsDTO.setEmvField55("55");
        confirmTransactionDetailsDTO.setEntryMode("Start");
        confirmTransactionDetailsDTO.setExpDate("2015");
        confirmTransactionDetailsDTO.setQrCode("1");
        confirmTransactionDetailsDTO.setTipAmount("1");
        confirmTransactionDetailsDTO.setTrack2("Success");
        confirmTransactionDetailsDTO.setTransactionAmount("99");
        Assert.assertNotNull(confirmTransactionDetailsDTO);
    }

    @Test
    public void testConfirmTransactionDetailsDTO() throws Exception {
        Assert.assertEquals("100", confirmTransactionDetailsDTO.getAmount());
        Assert.assertEquals("Aravind", confirmTransactionDetailsDTO.getCardholderName());
        Assert.assertEquals("Gir492", confirmTransactionDetailsDTO.getCardNumber());
        Assert.assertEquals("Contactless", confirmTransactionDetailsDTO.getCardType());
        Assert.assertEquals("115", confirmTransactionDetailsDTO.getCvv());
        Assert.assertEquals("55", confirmTransactionDetailsDTO.getEmvField55());
        Assert.assertEquals("Start", confirmTransactionDetailsDTO.getEntryMode());
        Assert.assertEquals("2015", confirmTransactionDetailsDTO.getExpDate());
        Assert.assertEquals("1", confirmTransactionDetailsDTO.getQrCode());
        Assert.assertEquals("1", confirmTransactionDetailsDTO.getTipAmount());
        Assert.assertEquals("Success", confirmTransactionDetailsDTO.getTrack2());
        Assert.assertEquals("99", confirmTransactionDetailsDTO.getTransactionAmount());
    }
}