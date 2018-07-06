package com.girmiti.mobilepos.util;

import android.content.Context;

import com.girmiti.mobilepos.net.model.LoginResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 20-Oct-17 05:04:00 pm
 * @version 1.0
 */
public class CurrencyFormatTest {

    Context context;

    @InjectMocks
    CurrencyFormat currencyFormat;

    @Mock
    LoginResponse loginResponse;

    @Test(expected = NullPointerException.class)
    public void testSetCurrencyData() throws Exception {
        loginResponse = new LoginResponse("login");
        currencyFormat.setCurrencyData(loginResponse);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrencySeparater() throws Exception {
        Assert.assertNotNull(currencyFormat.getCurrencySeparater());
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrencyCodeAlplha() throws Exception {
        Assert.assertNotNull(currencyFormat.getCurrencyCodeAlplha());
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrencyCodeMinorUnit() throws Exception {
        Assert.assertNotNull(currencyFormat.getCurrencyCodeMinorUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrencyCodeThousandUnit() throws Exception {
        Assert.assertNotNull(currencyFormat.getCurrencyCodeThousandUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testGetFormattedCurrency() throws Exception {
        Assert.assertNotNull(currencyFormat.getFormattedCurrency("1"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetFormattedCurrencyLocale() throws Exception {
        Assert.assertNotNull(currencyFormat.getFormattedCurrency("1", "locale"));
    }
}