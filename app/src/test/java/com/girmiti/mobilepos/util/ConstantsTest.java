package com.girmiti.mobilepos.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 06-Oct-2017 11:30:00 am
 * @version 1.0
 */
public class ConstantsTest {
    @Test
    public void testIsValidConstant() {
        assertEquals(0, Constants.ZERO);
        assertEquals(1, Constants.ONE);
        assertEquals(2, Constants.TWO);
        assertEquals(3, Constants.THREE);
        assertEquals(4, Constants.FOUR);
        assertEquals(5, Constants.FIVE);
        assertEquals(6, Constants.SIX);
        assertEquals(7, Constants.SEVEN);
        assertEquals(8, Constants.EIGHT);
        assertEquals(9, Constants.NINE);
        assertEquals(10, Constants.TEN);
        assertEquals(11, Constants.ELEVEN);
        assertEquals(12, Constants.TWELVE);
        assertEquals(13, Constants.THIRTEEN);
    }
}