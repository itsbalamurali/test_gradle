package com.girmiti.mobilepos.net.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Created by aravind on 20-07-2017.
 */
public class LoginResponseTest {

    @InjectMocks
    LoginResponse loginResponse = new LoginResponse("response");

    @Mock
    private CurrencyDTO currencyDTO;

    @Mock
    private TSMResponse tsmResponse;

    private String terminalID = "100";
    private String bussinessName = "PYco";
    private String email = "User@girmiti.com";
    private String address="Marathahalli";
    private String city ="Bangalore";
    private String state ="Karanataka";
    private String country ="India";
    private String pin ="560037";

    @Before
    public void setLoginResponse() throws Exception {
        loginResponse.setAddress(address);
        loginResponse.setCity(city);
        loginResponse.setState(state);
        loginResponse.setCountry(country);
        loginResponse.setPin(pin);
        loginResponse.setTerminalID(terminalID);
        loginResponse.setBussinessName(bussinessName);
        loginResponse.setCurrencyDTO(currencyDTO);
        loginResponse.setTerminalData(tsmResponse);
        loginResponse.setEmail(email);
    }

    @Test
    public void testLoginRequest() throws Exception {
        Assert.assertEquals(address, loginResponse.getAddress());
        Assert.assertEquals(city, loginResponse.getCity());
        Assert.assertEquals(state, loginResponse.getState());
        Assert.assertEquals(country, loginResponse.getCountry());
        Assert.assertEquals(pin, loginResponse.getPin());
        Assert.assertEquals(terminalID, loginResponse.getTerminalID());
        Assert.assertEquals(bussinessName, loginResponse.getBussinessName());
        Assert.assertEquals(currencyDTO, loginResponse.getCurrencyDTO());
        Assert.assertEquals(tsmResponse, loginResponse.getTerminalData());
        Assert.assertEquals(email, loginResponse.getEmail());
    }
}