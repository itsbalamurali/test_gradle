package com.girmiti.mobilepos.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 20-Oct-17 05:30:00 pm
 * @version 1.0
 */
public class ApplicationPropertiesTest {

    @InjectMocks
    ApplicationProperties applicationProperties;

    @Test(expected = NullPointerException.class)
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(applicationProperties.getInstance());
    }
}