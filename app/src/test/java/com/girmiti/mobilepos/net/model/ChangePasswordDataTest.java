package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by aravind on 20-07-2017.
 */
public class ChangePasswordDataTest {

    @InjectMocks
    ChangePasswordData changePasswordData;

    @Before
    public void setChangePasswordData() throws Exception {

        changePasswordData = new ChangePasswordData();

        changePasswordData.setUserName("Aravind");
        changePasswordData.setUserId(1L);
        changePasswordData.setEmail("aravind@girmiti.com");
        changePasswordData.setMerchantId("1");
        changePasswordData.setTerminalId("100");
        changePasswordData.setTxnType("Manual");
        changePasswordData.setCurrentPassword("Girmiti@12345");
        changePasswordData.setNewPassword("Asd@123!");
        changePasswordData.setConfirmPassword("Asd@123!");

        changePasswordData.createRequest();
    }

    @Test
    public void testChangePasswordData() throws Exception {
        Assert.assertEquals("Aravind", changePasswordData.getUserName());
        Assert.assertEquals("1", changePasswordData.getUserId().toString());
        Assert.assertEquals("aravind@girmiti.com", changePasswordData.getEmail());
        Assert.assertEquals("1", changePasswordData.getMerchantId());
        Assert.assertEquals("100", changePasswordData.getTerminalId());
        Assert.assertEquals("Manual", changePasswordData.getTxnType());
        Assert.assertEquals("Girmiti@12345", changePasswordData.getCurrentPassword());
        Assert.assertEquals("Asd@123!", changePasswordData.getNewPassword());
        Assert.assertEquals("Asd@123!", changePasswordData.getConfirmPassword());
    }

}