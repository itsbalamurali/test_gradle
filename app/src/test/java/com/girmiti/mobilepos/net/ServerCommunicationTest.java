package com.girmiti.mobilepos.net;

import com.girmiti.mobilepos.net.model.ChangePasswordData;
import com.girmiti.mobilepos.net.model.LoadFundRequest;
import com.girmiti.mobilepos.net.model.LoginRequest;
import com.girmiti.mobilepos.net.model.RefundRequest;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.net.model.SaleRequest;
import com.girmiti.mobilepos.net.model.VoidRequest;

import org.junit.Test;

import java.net.HttpURLConnection;

import static junit.framework.Assert.assertNotNull;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 29-Jul-2017 10:00:00 am
 * @version 1.0
 */
public class ServerCommunicationTest {

// NOTE : while running these test casses make ServerCommunication.MOCK_RESPONSE to false else create valid request objects

    ServerCommunication serverCommunication = ServerCommunication.getInstance();

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(ServerCommunication.getInstance());
    }

    @Test(expected = RuntimeException.class)
    public void testGetUrl() throws Exception {
        assertNotNull(ServerCommunication.getInstance());
        String url = serverCommunication.getUrl("card_digitize");
        assertNotNull(url);
    }

    @Test(expected = RuntimeException.class)
    public void testGetUrlResponse() throws Exception {
        HttpURLConnection urlConnection = null;
        assertNotNull(ServerCommunication.getInstance());
        String url = serverCommunication.getUrlResponse(urlConnection);
        assertNotNull(url);
    }

    @Test(expected = RuntimeException.class)
    public void testLoginUser() throws Exception {
        Response response = serverCommunication.login(new LoginRequest(), "English");
        assertNotNull(response);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessSale() throws Exception {
        Response response = serverCommunication.processSale(new SaleRequest());
        assertNotNull(response);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessVoid() throws Exception {
        Response response = serverCommunication.processVoid(new VoidRequest());
        assertNotNull(response);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessRefund() throws Exception {
        Response response = serverCommunication.processRefund(new RefundRequest());
        assertNotNull(response);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessLoadMoney() throws Exception {
        Response response = serverCommunication.processLoadMoney(new LoadFundRequest());
        assertNotNull(response);
    }

    @Test(expected = RuntimeException.class)
    public void testChangePassword() throws Exception {
        ServerCommunication serverCommunication = ServerCommunication.getInstance();
        String changePwd = serverCommunication.changePasswordPostRequest(new ChangePasswordData());
        assertNotNull(changePwd);
    }

    @Test(expected = RuntimeException.class)
    public void testForgotPassword() throws Exception {
        ServerCommunication serverCommunication = ServerCommunication.getInstance();
        String forgotPwd = serverCommunication.forgotPasswordPostRequest(new ChangePasswordData());
        assertNotNull(forgotPwd);
    }
}