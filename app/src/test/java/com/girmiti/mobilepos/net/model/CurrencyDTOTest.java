package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class CurrencyDTOTest {

    @InjectMocks
    CurrencyDTO currencyDTO;

    @Before
    public void setCurrencyDTO() throws Exception {

        currencyDTO = new CurrencyDTO();

        currencyDTO.setCurrencyCodeAlpha("USD");
        currencyDTO.setCurrencyCodeNumeric("1");
        currencyDTO.setCurrencyExponent("1");
        currencyDTO.setCurrencyMinorUnit("0");
        currencyDTO.setCurrencySeparatorPosition("0");
        currencyDTO.setCurrencyThousandsUnit("1000");
    }

    @Test
    public void testCurrencyDTO() throws Exception {
        Assert.assertEquals("USD", currencyDTO.getCurrencyCodeAlpha());
        Assert.assertEquals("1", currencyDTO.getCurrencyCodeNumeric());
        Assert.assertEquals("1", currencyDTO.getCurrencyExponent());
        Assert.assertEquals("0", currencyDTO.getCurrencyMinorUnit());
        Assert.assertEquals("0", currencyDTO.getCurrencySeparatorPosition());
        Assert.assertEquals("1000", currencyDTO.getCurrencyThousandsUnit());
    }
}