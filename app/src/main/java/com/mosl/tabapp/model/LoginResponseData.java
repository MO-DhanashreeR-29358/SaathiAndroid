package com.mosl.tabapp.model;


import java.io.Serializable;

/**
 * Created by raju 27/03/2017.
 */
public class LoginResponseData implements Serializable {
    String userId = "";
    String firstName = "";
    String lastName = "";
    String imageUrl = "";
    String mobileNo = "";
    String email = "";
    String businessProfileId = "";
    String businessProfileName = "";
    String employeeCode = "";
    String businessUnitId = "";
    String userType = "";
    boolean smtEnabled;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    String branchCode;

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessProfileId() {
        return businessProfileId;
    }

    public void setBusinessProfileId(String businessProfileId) {
        this.businessProfileId = businessProfileId;
    }

    public String getBusinessProfileName() {
        return businessProfileName;
    }

    public void setBusinessProfileName(String businessProfileName) {
        this.businessProfileName = businessProfileName;
    }

    public boolean isSmtEnabled() {
        return smtEnabled;
    }

    public void setSmtEnabled(boolean smtEnabled) {
        this.smtEnabled = smtEnabled;
    }
}
