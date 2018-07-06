package com.girmiti.mobilepos.net.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by aravind on 20-07-2017.
 */
public class TSMResponseTest {
    private String terminalId = "100";
    private String updateType = "Manual";
    private String updateURL = "www.ipsidy.com";
    private String updateVersion = "1";
    private TSMResponse tsmResponse;
    private Response response;
    String object;


    @Before
    public void BeforeTestMethod() throws Exception {

        object = "{\n" +
                "\t\"errorCode\": \"00\",\n" +
                "\t\"errorMessage\": \"Approved\",\n" +
                "\t\"deviceLocalTxnTime\": \"1500544445870\",\n" +
                "\t\"txnRefNumber\": \"681417424841\",\n" +
                "\t\"authId\": \"122471\",\n" +
                "\t\"cgRefNumber\": \"2000002130\",\n" +
                "\t\"merchantCode\": \"156165263616431\",\n" +
                "\t\"paymentDetails\": \"100\",\n" +
                "\t\"merchantName\": \"Ipsidy\"\n" +
                "}";

        tsmResponse = new TSMResponse(object.toString());
        tsmResponse.setTerminalId(terminalId);
        tsmResponse.setUpdateType(updateType);
        tsmResponse.setUpdateURL(updateURL);
        tsmResponse.setUpdateVersion(updateVersion);
    }


    @Test
    public void testTerminalId() {
        assertEquals(terminalId, tsmResponse.getTerminalId());
    }

    @Test
    public void testUpdateType() {
        assertEquals(updateType, tsmResponse.getUpdateType());
    }

    @Test
    public void testUpdateURL() {
        assertEquals(updateURL, tsmResponse.getUpdateURL());
    }

    @Test
    public void testUpdateVersion() {
        assertEquals(updateVersion, tsmResponse.getUpdateVersion());
    }
}