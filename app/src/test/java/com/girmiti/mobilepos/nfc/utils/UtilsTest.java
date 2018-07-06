package com.girmiti.mobilepos.nfc.utils;

import com.girmiti.mobilepos.logger.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Oct-2017 11:00:00 am
 * @version 1.0
 */

public class UtilsTest {

    @InjectMocks
    Utils utils;

    @Mock
    private Logger logger;

    @Test(expected = AssertionError.class)
    public void testUtils() throws Exception {
        logger = Logger.getNewLogger("com.girmiti.mobilepos.nfc.utils.Utils");
        byte[] val = new byte[]{0x03, 0x05};
        byte[] res = utils.hexStringToByteArray("0305");
        assertTrue(Arrays.equals(val, res));
        assertEquals(utils.byteArrayToHexString(new byte[]{0x01, 0x02}), "0102");
        utils.hexToDec("A");
        utils.paddingWithZeros("randomNumber", 8);
        utils.rightPaddingWithZeros("randomNumber", 6);
        Assert.assertNotNull(utils);
    }
}