package com.girmiti.mobilepos.exception;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 12-Oct-2017 10:12:00 am
 * @version 1.0
 */

public class GenericExceptionTest {

    @InjectMocks
    GenericException genericException;

    @Test
    public void testGenericException() throws Exception {
        genericException = new GenericException("exception");
        Assert.assertNotNull(genericException);
    }
}
