package com.girmiti.mobilepos.util;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 06-Oct-2017 2:50:00 pm
 * @version 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class UtilsTest {

    @InjectMocks
    private Utils utils = new Utils();

    @Mock
    Context context;

    @Mock
    Properties properties;

    @Test(expected = RuntimeException.class)
    public void testHasNFCHardware() {
        assertTrue(Utils.hasNFCHardware(context));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadProperties() {
        utils.loadProperties(1, properties);
        assertNotNull(utils);
    }

    @Test
    public void testIsEmptyString() {
        assertTrue(Utils.isEmptyString(null));
        assertTrue(Utils.isEmptyString(new String()));
        assertFalse(Utils.isEmptyString("123"));
    }

    @Test
    public void testFormattedAmount() {
        String formattedAmount = Utils.getFormattedAmount(25786);
        assertNotNull(formattedAmount);
        assertEquals("25786.00", formattedAmount);
    }

    @Test
    public void testMaskedCardNumber() throws Exception {
        String maskedCardNumber = Utils.getMaskedCardNumber("1234567890123456");
        assertNotNull(maskedCardNumber);
        assertEquals("1234 **** **** 3456", maskedCardNumber);
    }

    @Test
    public void testformatAmount() throws Exception {
        String formattedAmount = Utils.formatAmount("25");
        assertNotNull(formattedAmount);
        assertEquals("25", formattedAmount);
    }

}
