package com.mosl.mobile.biometric.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.mosl.mobile.ekyc.common.MOSLCommon.getLocalIpAddress;

/**
 * Created by Pramod on 29-Jul-17
 */
public class AuthRequest {
    private String userId;
    private String aadharNumber;
    private String timeStamp;
    private String authMode;
    private String pidData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getTimeStamp() {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            timeStamp =  sdf.format(new Date());
        }
        catch (Exception ex){

        }
        return timeStamp;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(boolean isIris) {
        this.authMode = isIris ? "IRIS" : "FINGER";
    }

    public String getPidData() {
        return pidData;
    }

    public void setPidData(String pidData) {
        this.pidData = pidData;
    }

    public String getIpAddress() {
        return getLocalIpAddress();
    }


}
