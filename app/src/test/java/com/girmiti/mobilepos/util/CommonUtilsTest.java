package com.girmiti.mobilepos.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 06-Oct-2017 12:30:00 pm
 * @version 1.0
 * @date 06-Oct-2017 12:30:00 pm
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonUtilsTest {

    @Test
   public void testhexToString() {
       String result = CommonUtils.hexToString("74657374");
       Assert.assertNotNull(result);
       Assert.assertEquals("test", result);
   }

    @Test
    public void testvalidateCardNumber() {
        String cardNumber = null;

        cardNumber = "4062406015";
        boolean isValid = CommonUtils.validateCardNumber(cardNumber);
        assertEquals(false, isValid);

        cardNumber = "40624060155";
        isValid = CommonUtils.validateCardNumber(cardNumber);
        assertEquals(false, isValid);

        cardNumber = "5105105105105100";
        isValid = CommonUtils.validateCardNumber(cardNumber);
        assertEquals(true, isValid);

        String invalidcardNumber = "5105105105105122";
        isValid = CommonUtils.validateCardNumber(invalidcardNumber);
        assertEquals(false, isValid);
    }

    @Test
    public void getCCType() throws Exception {
        String result = null;
        String visacard = "4062406015513555";
        String mastercard = "5339834726593489";
        String amexcard = "374257528212362";
        String dinersClubrcard = "30114392952886";
        String discovercard = "214907213128018";
        String jcbcard = "3528944095022360";

        result = CommonUtils.getCCType(visacard);
        Assert.assertNotNull(result);
        Assert.assertEquals("VI", result);

        result = CommonUtils.getCCType(mastercard);
        Assert.assertEquals("MC", result);

        result = CommonUtils.getCCType(amexcard);
        Assert.assertEquals("AX", result);

        result = CommonUtils.getCCType(dinersClubrcard);
        Assert.assertEquals("DC", result);

        result = CommonUtils.getCCType(discovercard);
        Assert.assertEquals("IP", result);

        result = CommonUtils.getCCType(jcbcard);
        Assert.assertEquals("JC", result);

    }

    @Test
    public void removeLastCharacters() {
        String result = CommonUtils.removeLastCharacters("Girmiti", 1);
        Assert.assertNotNull(result);
        Assert.assertEquals("Girmit", result);
    }

    @Test
    public void substringsBetween() {
        String[] result = CommonUtils.substringsBetween("WorkAtGirmiti", "Work", "Girmiti");
        Assert.assertNotNull(result);
        Assert.assertEquals("At", result[0]);
    }
}
