package com.girmiti.mobilepos.net;

import android.util.Base64;

import com.girmiti.mobilepos.exception.GenericException;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.OAuthToken;
import com.girmiti.mobilepos.util.ApplicationProperties;
import com.girmiti.mobilepos.util.Constants;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class Network {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String LOCALE = "Locale";
    private static final String ACCEPT = "Accept";
    private static String selected_language;

    private static Logger logger = getNewLogger(Network.class.getName());
    public enum HttpMethod {
        POST, PUT, GET, DELETE
    }

    static HttpsURLConnection getConnection(String url, String requestType) throws IOException {

        HttpsURLConnection urlConnection = null;
        if (url.contains("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) new URL(url).openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            urlConnection = https;
        } else {
            urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        }
        urlConnection.setRequestMethod(requestType);
        urlConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        urlConnection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        return urlConnection;
    }

    public static HttpsURLConnection post(String url, boolean oauth) throws IOException, NetworkException {
        return post(url, oauth, null);
    }

    public static HttpsURLConnection post(String url, boolean oauth, String etag) throws IOException, NetworkException {

        HttpsURLConnection urlConnection = getConnection(url,  HttpMethod.POST.name());
        urlConnection.setRequestProperty("etag", etag);

        if (oauth) {
            OAuthToken accessToken = getAuthToken();
            if(accessToken!=null) {
                logger.debug( "Auth token: " + accessToken.getAccess_token() );
                String oauthHeader = "Bearer " + accessToken.getAccess_token();
                logger.debug("Register" + oauthHeader);
                urlConnection.setRequestProperty(AUTHORIZATION, oauthHeader);
            }
        }

        if (etag != null) {
            urlConnection.setRequestProperty("If-None-Match", etag);
        }

        OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.flush();
        writer.close();
        os.close();
        urlConnection.connect();

        return urlConnection;
    }

    public static byte[] readResponse(InputStream input) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[Constants.READ_RESPONSE_BYTE_SIZE];

        while (true) {
            int r = input.read(buf, 0, buf.length);
            if (r == -1) {
                break;
            }
            baos.write(buf, 0, r);
        }
        return baos.toByteArray();
    }

    private static OAuthToken getAuthToken() throws NetworkException, IllegalStateException, IOException {

        String server = ApplicationProperties.getInstance().getProperty("server_url");
        String endpoint = ApplicationProperties.getInstance().getProperty("oauth_endpoint");
        endpoint = endpoint.trim();
        server += "/" + endpoint;

        logger.debug("Auth url: "  + server);

        byte[] data = ApplicationProperties.getInstance().getProperty("oauth_creds").getBytes("UTF-8");
        String base64EncodedCredentials = Base64.encodeToString(data, Base64.NO_WRAP);

        HttpsURLConnection urlConnection = getConnection(server, HttpMethod.GET.name());
        urlConnection.setRequestProperty(AUTHORIZATION, "Basic " + base64EncodedCredentials);

        int response = urlConnection.getResponseCode();
        if (response == HttpsURLConnection.HTTP_OK) {
            byte[] r = readResponse(urlConnection.getInputStream());
            String responseStr = new String(r, 0, r.length);
            return new Gson().fromJson(responseStr, OAuthToken.class);
        }
        return null;
    }

    public static HttpsURLConnection upload(String url, byte[] data, boolean oauth,String selectedLanguage) throws  NetworkException,IOException {

        logger.debug("network URL :::::" + url);
        HttpsURLConnection urlConnection = getConnection(url, HttpMethod.POST.name());

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        if(selectedLanguage!=null) {
            selected_language=selectedLanguage;
        }
        urlConnection.setRequestProperty( LOCALE, selected_language );

        if (oauth) {
            OAuthToken accessToken = null;
            accessToken = getAuthToken();

            if(accessToken!=null) {
                logger.debug( "Auth token: " + accessToken.getAccess_token() );
                String oauthHeader = "Bearer " + accessToken.getAccess_token();
                logger.debug( "Register" + oauthHeader );
                urlConnection.setRequestProperty(AUTHORIZATION, oauthHeader);
            }
        }

        OutputStream out = new DataOutputStream(urlConnection.getOutputStream());
        out.write(data);
        out.flush();
        out.close();
        urlConnection.connect();
        return urlConnection;
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    throw new GenericException("Client host is not trusted");
                } catch (GenericException e) {
                    logger.info( "Exception ::"+e );
                }
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    throw new GenericException("Server Host is not trusted");
                } catch (GenericException e) {
                    logger.info( "Exception ::"+e );
                }
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            logger.severe("Error occured in SSLContext" +e);
        }
    }
}