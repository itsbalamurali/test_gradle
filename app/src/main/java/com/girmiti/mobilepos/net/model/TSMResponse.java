package com.girmiti.mobilepos.net.model;

/**
 * Created by nayan on 1/3/17.
 */
public class TSMResponse extends Response {

    private static final long serialVersionUID = 8227190264868980458L;

    private String terminalId;
    private String updateType;
    private String updateURL;
    private String updateVersion;

    public TSMResponse(String response) {
        super(response);
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }
}
