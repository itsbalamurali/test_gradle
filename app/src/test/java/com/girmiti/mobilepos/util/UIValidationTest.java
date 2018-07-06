package com.girmiti.mobilepos.util;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 28-Jul-2017 5:10:10 pm
 * @version 1.0
 * @date 28-Jul-2017 5:10:10 pm
 */
public class UIValidationTest {

    @Test
    public void testCardHolderNameEmpty() {
        String name = null;
        boolean isEmpty = UIValidation.isCardHolderNameNotEmpty(name);
        assertEquals(false, isEmpty);

        name = "";
        isEmpty = UIValidation.isCardHolderNameNotEmpty(name);
        assertEquals(false, isEmpty);

        name = "Test";
        isEmpty = UIValidation.isCardHolderNameNotEmpty(name);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testisExpDateEmpty() {
        String expDate = null;
        boolean isEmpty = UIValidation.isExpDateNotEmpty(expDate);
        assertEquals(false, isEmpty);

        expDate = "";
        isEmpty = UIValidation.isExpDateNotEmpty(expDate);
        assertEquals(false, isEmpty);

        expDate = "2025";
        isEmpty = UIValidation.isExpDateNotEmpty(expDate);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testisPanEmpty() {
        String pan = null;
        boolean isEmpty = UIValidation.isPanNotEmpty(pan);
        assertEquals(false, isEmpty);

        pan = "BWM";
        isEmpty = UIValidation.isPanNotEmpty(pan);
        assertEquals(false, isEmpty);

        pan = "";
        isEmpty = UIValidation.isPanNotEmpty(pan);
        assertEquals(false, isEmpty);

        pan = "BWMP";
        isEmpty = UIValidation.isPanNotEmpty(pan);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testisPanValid() {
        String cardNumber = null;
        boolean isEmpty = UIValidation.isPanValid(cardNumber);
        assertEquals(false, isEmpty);

        cardNumber = "557897";
        isEmpty = UIValidation.isPanValid(cardNumber);
        assertEquals(false, isEmpty);

        cardNumber = "5578971239366399";
        isEmpty = UIValidation.isPanValid(cardNumber);
        assertEquals(true, isEmpty);

    }

    @Test
    public void testisValidAccountNumber() {
        String accountNumber = null;
        boolean isEmpty = UIValidation.isValidAccountNumber(accountNumber);
        assertEquals(false, isEmpty);

        accountNumber = "557897123";
        isEmpty = UIValidation.isValidAccountNumber(accountNumber);
        assertEquals(false, isEmpty);

        accountNumber = "";
        isEmpty = UIValidation.isValidAccountNumber(accountNumber);
        assertEquals(false, isEmpty);

        accountNumber = "5578971239";
        isEmpty = UIValidation.isValidAccountNumber(accountNumber);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testisValidAmount() {
        String amount = null;
        boolean isEmpty = UIValidation.isValidAmount(amount);
        assertEquals(false, isEmpty);

        amount = "0.00";
        isEmpty = UIValidation.isValidAmount(amount);
        assertEquals(false, isEmpty);

        amount = "";
        isEmpty = UIValidation.isValidAmount(amount);
        assertEquals(false, isEmpty);

        amount = "10";
        isEmpty = UIValidation.isValidAmount(amount);
        assertEquals(true, isEmpty);

    }

    @Test
    public void testisValidManualTipAmount() {
        String amount = null;
        boolean isEmpty = UIValidation.isValidManualTipAmount(amount);
        assertEquals(false, isEmpty);

        amount = "0.00";
        isEmpty = UIValidation.isValidManualTipAmount(amount);
        assertEquals(false, isEmpty);

        amount = ".";
        isEmpty = UIValidation.isValidManualTipAmount(amount);
        assertEquals(false, isEmpty);

        amount = "10";
        isEmpty = UIValidation.isValidManualTipAmount(amount);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testisCVVEmpty() throws Exception {

        String cvv = null;
        boolean isEmpty = UIValidation.isCVVEmpty(cvv);
        assertEquals(false, isEmpty);

        cvv = "12";
        isEmpty = UIValidation.isCVVEmpty(cvv);
        assertEquals(false, isEmpty);

        cvv = "a11";
        isEmpty = UIValidation.isCVVEmpty(cvv);
        assertEquals(false, isEmpty);

        cvv = "911";
        isEmpty = UIValidation.isCVVEmpty(cvv);
        assertEquals(true, isEmpty);
    }
}