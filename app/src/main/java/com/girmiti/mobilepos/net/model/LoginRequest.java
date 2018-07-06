package com.girmiti.mobilepos.net.model;


public class LoginRequest extends Request {

    private String username;
    private String password;
    private String deviceSerial;
    private String currentAppVersion;

    public String getUserName() {
        return username;
    }


    public void setUserName(String userName) {
        this.username = userName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentAppVersion() {
        return currentAppVersion;
    }

    public void setCurrentAppVersion(String currentAppVersion) {
        this.currentAppVersion = currentAppVersion;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    @Override
    public String createRequest() {
        return null;
    }

}
