package com.girmiti.mobilepos.net;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 28-Jul-2017 10:00:00 am
 * @version 1.0
 */
public class NetworkTest {

    @Test
    public void testNetwork() {
        Network network = new Network();
        assertNotNull(network);
    }

    @Test
    public void testNetworkEnum() {
        Network network = new Network();
        assertEquals("POST", Network.HttpMethod.POST.name());
        assertEquals("PUT", Network.HttpMethod.PUT.name());
        assertEquals("GET", Network.HttpMethod.GET.name());
        assertEquals("DELETE", Network.HttpMethod.DELETE.name());
        assertNotNull(network);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetConnectionHttps() throws Exception {
        HttpURLConnection urlConnection = null;
        Network.getConnection("https", "POST");
        assertNotNull(urlConnection);
    }

    @Test(expected = MalformedURLException.class)
    public void testPost() throws Exception {
        HttpURLConnection urlConnection = null;
        Network.post("https", true);
        assertNotNull(urlConnection);
    }

    @Test(expected = RuntimeException.class)
    public void testUpload() throws Exception {
        byte[] data = new byte[]{87, 79, 87, 46, 46, 46};
        HttpURLConnection urlConnection = null;
        Network.upload("http://192.168.0.120:8080/mPos/test", data, true, "English");
        assertNotNull(urlConnection);
    }

    @Test
    public void testReadREsponse() {

        InputStream is = new ByteArrayInputStream( "Test".getBytes() );
        try {
            byte[] data = Network.readResponse(is);
            assertEquals("Test", new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}