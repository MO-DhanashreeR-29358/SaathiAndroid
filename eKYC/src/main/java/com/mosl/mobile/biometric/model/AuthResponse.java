package com.mosl.mobile.biometric.model;

/**
 * Created by lalkishore on 5/26/2016.
 */
public class AuthResponse {
    private String isSuccess;

    public boolean isSuccess() {
        return "Y".equals(isSuccess);
    }

    public void setSuccess(String success) {
        isSuccess = success;
    }
}
