package com.girmiti.mobilepos.net;

import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.net.model.BalanceInquiryRequest;
import com.girmiti.mobilepos.net.model.BalanceInquiryResponse;
import com.girmiti.mobilepos.net.model.ChangePasswordData;
import com.girmiti.mobilepos.net.model.LoadFundRequest;
import com.girmiti.mobilepos.net.model.LoginRequest;
import com.girmiti.mobilepos.net.model.LoginResponse;
import com.girmiti.mobilepos.net.model.RefundRequest;
import com.girmiti.mobilepos.net.model.Request;
import com.girmiti.mobilepos.net.model.Response;
import com.girmiti.mobilepos.net.model.SaleRequest;
import com.girmiti.mobilepos.net.model.VoidRequest;
import com.girmiti.mobilepos.util.ApplicationProperties;
import com.girmiti.mobilepos.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ServerCommunication {

    private static final ServerCommunication instance = new ServerCommunication();
    public static final String SERVER_URL = "server_url";
    private Logger logger = Logger.getNewLogger("com.girmiti.mobilepos.net.ServerCommunication");
    private Gson gson = new Gson();
    private  String errorText = null;
    private static final String MESSAGE = "Inside Server-Communication";
    private static final String URL_MESSAGE = "StatementResult URL ->";
    private static final String TRANSACTION = "transaction";

    public static ServerCommunication getInstance() {
        return instance;
    }

    public String getUrl(String id) {
        String server = Utils.getPreference("SQP");
        if (server == null) {
            server = ApplicationProperties.getInstance().getProperty(SERVER_URL);
        }
        String endpoint = ApplicationProperties.getInstance().getProperty(id);
        endpoint = endpoint.trim();
        return server + "/" + endpoint;
    }

    private byte[] getInputData(Request reg, String debugMsg) {
        String register = gson.toJson(reg);
        logger.debug(debugMsg + " REQUEST SENT -> " + register);
        return register.getBytes();

    }

    public String getUrlResponse(HttpURLConnection urlConnection) throws NetworkException,IOException {

        try {
            int response = urlConnection.getResponseCode();
            logger.debug("URL Response Code last : " + response);
            if (response == HttpURLConnection.HTTP_OK) {
                byte[] r = Network.readResponse(urlConnection.getInputStream());
                String responseJson = new String(r, 0, r.length, "UTF-8");
                logger.debug("URL Responses data" + responseJson);
                return responseJson;
            } else {
                logger.debug("Failed to fetch data");
                errorText = "Failed to Fetch Data";
                throw new NetworkException(errorText);
            }
        } catch (NetworkException e) {
            throw e;
        } catch (IOException e) {
            logger.severe("IOException"+ e );
        }
        if (errorText != null) {
            throw new IOException(errorText);
        }
        return null;
    }

    public Response processSale(SaleRequest request) throws NetworkException,IOException {

        logger.debug(MESSAGE);
        String url = getUrl("sale");
        logger.debug(URL_MESSAGE + url);

        HttpURLConnection urlConnection = Network.upload(url, getInputData(request, TRANSACTION), true,null);
        return gson.fromJson(getUrlResponse(urlConnection), Response.class);
    }

    public Response processVoid(VoidRequest request) throws NetworkException,IOException {

        logger.debug(MESSAGE);
        String url = getUrl("sale");
        logger.debug(URL_MESSAGE + url);

        HttpURLConnection urlConnection = Network.upload(url, getInputData(request, TRANSACTION), true,null);
        return gson.fromJson(getUrlResponse(urlConnection), Response.class);
    }

    public Response processRefund(RefundRequest request) throws NetworkException,IOException {

        logger.debug(MESSAGE);
        String url = getUrl("sale");
        logger.debug(URL_MESSAGE + url);

        HttpURLConnection urlConnection = Network.upload(url, getInputData(request, TRANSACTION), true,null);
        return gson.fromJson(getUrlResponse(urlConnection), Response.class);
    }

    public Response processLoadMoney(LoadFundRequest request) throws NetworkException,IOException {

        logger.debug(MESSAGE);
        String url = getUrl("sale");
        logger.debug(URL_MESSAGE + url);

        HttpURLConnection urlConnection = Network.upload(url, getInputData(request, TRANSACTION), true,null);
        return gson.fromJson(getUrlResponse(urlConnection), Response.class);
    }

    public LoginResponse login(LoginRequest loginReq, String selectedLanguage) throws NetworkException,IOException {
        logger.debug(MESSAGE);
        String url = getUrl("login");
        logger.debug("Login URL -> " + url);

        HttpURLConnection urlConnection = Network.upload(url, getInputData(loginReq, "Login"), true,selectedLanguage);
        return gson.fromJson(getUrlResponse(urlConnection), LoginResponse.class);
    }

    public String changePasswordPostRequest(ChangePasswordData changePasswordData) throws NetworkException,IOException {
        logger.debug(MESSAGE);
        gson.toJson(changePasswordData);
        String url = getUrl("change_password");
        logger.debug("ChangePasswordPostRequest URL -> " + url);
        HttpURLConnection urlConnection = Network.upload(url, getInputData(changePasswordData, "ChangePasswordPostRequest"), true,null);
        return getUrlResponse(urlConnection);
    }

    public String forgotPasswordPostRequest(ChangePasswordData changePasswordData) throws NetworkException,IOException {
        logger.debug(MESSAGE);
        gson.toJson(changePasswordData);
        String url = getUrl("forgot_password");
        logger.debug("ForgotPasswordPostRequest URL -> " + url);
        HttpURLConnection urlConnection = Network.upload(url, getInputData(changePasswordData, "ForgotPasswordPostRequest"),true,null);
        return getUrlResponse(urlConnection);
    }

    public BalanceInquiryResponse processBalanceInquiry(BalanceInquiryRequest balanceInquiryRequest) throws NetworkException,IOException {
        logger.debug("Inside Server-Communication");
        String url = getUrl("sale");
        logger.debug("StatementResult URL -> " + url);
        HttpURLConnection urlConnection = Network.upload(url, getInputData(balanceInquiryRequest, "balanceInquiry"), true,null);
        return gson.fromJson(getUrlResponse(urlConnection), BalanceInquiryResponse.class);
    }

}

















