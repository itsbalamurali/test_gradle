package com.girmiti.mobilepos.net.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by aravind on 20-07-2017.
 */


@RunWith(MockitoJUnitRunner.class)
public class BillingDataTest {

    @InjectMocks
    BillingData billingData = new BillingData();

    @Before
    public void beforesetup() throws Exception {
        billingData = new BillingData();
    }

    @Test
    public void setBillingData() throws Exception {
    }

}