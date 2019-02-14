package com.mosl.tabapp.model;

import com.mosl.tabapp.constants.MOSLConstants;

import java.io.Serializable;

/**
 * Created by raju on 3/27/2017.
 */

public class AccountDetails implements MOSLConstants,Serializable {
    private static String userId="";
    private static String firstName="";
    private static String lastName="";
    private static String imageUrl="";
    private static String mobileNo="";
    private static String email="";
    private static String businessProfileId="";
    private static String businessProfileName="";
    private static String employeeCode="";
    private static String businessUnitId="";
    private static String token="";
    private static String branchCode="";
    private static String userType="";
    private static String profileType="";
    private static boolean newEkyc;
    private static boolean smtEnabled;
    private static boolean aufsb;


    public static boolean isFranchiseUser() {
        return userType != null && userType.equalsIgnoreCase(USER_TYPE_FRANCHISE);
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        AccountDetails.userType = userType;
    }

    public static String getBusinessUnitId() {
        return businessUnitId;
    }

    public static void setBusinessUnitId(String businessUnitId) {
        AccountDetails.businessUnitId = businessUnitId;
    }

    public AccountDetails(String userId, String firstName, String lastName, String imageUrl,
                          String mobileNo, String email, String businessProfileId, String businessProfileName,
                          String employeeCode, String token, String businessUnitId, String branchCode,
                          String userType, boolean smtEnabled, boolean aufsb) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.mobileNo = mobileNo;
        this.email = email;
        this.businessProfileId = businessProfileId;
        this.businessProfileName = businessProfileName;
        this.employeeCode = employeeCode.trim();
        this.token = token;
        this.businessUnitId = businessUnitId;
        this.branchCode = branchCode;
        this.userType = userType;
        this.profileType = profileFlag(businessUnitId);
        this.smtEnabled = smtEnabled;
        this.aufsb = aufsb;
    }


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AccountDetails.token = token;
    }

    public static String getEmployeeCode() {
        return employeeCode;
    }

    public static void setEmployeeCode(String employeeCode) {
        AccountDetails.employeeCode = employeeCode;
    }

    public static String getBusinessProfileName() {
        return businessProfileName;
    }

    public static void setBusinessProfileName(String businessProfileName) {
        AccountDetails.businessProfileName = businessProfileName;
    }

    public static String getBusinessProfileId() {
        return businessProfileId;
    }

    public static void setBusinessProfileId(String businessProfileId) {
        AccountDetails.businessProfileId = businessProfileId;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        AccountDetails.email = email;
    }

    public static String getMobileNo() {
        return mobileNo;
    }

    public static void setMobileNo(String mobileNo) {
        AccountDetails.mobileNo = mobileNo;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static void setImageUrl(String imageUrl) {
        AccountDetails.imageUrl = imageUrl;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        AccountDetails.lastName = lastName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        AccountDetails.firstName = firstName;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        AccountDetails.userId = userId;
    }

    public static String getBranchCode() {
        return branchCode;
    }

    public static void setBranchCode(String branchCode) {
        AccountDetails.branchCode = branchCode;
    }

    public static String getProfileType() {
        return profileType;
    }

    public static void setProfileType(String profileType) {
        AccountDetails.profileType = profileType;
    }

    private String profileFlag(String businessUnitId) {

        if (businessUnitId.equalsIgnoreCase("1")) {
            //businessUnitId=1,then RETAIL;
            return RETAIL;
        } else if (businessUnitId.equalsIgnoreCase("3")) {
            //businessUnitId=3,then FRANCHIES;
            return FRANCHIES;
        } else if (businessUnitId.equalsIgnoreCase("4")) {
            //businessUnitId=4,then IFA;
            return IFA;
        }
        return RETAIL;

    }

    public static boolean isNewEkyc() {
        return newEkyc;
    }

    public static void setNewEkyc(boolean newEkyc) {
        AccountDetails.newEkyc = newEkyc;
    }

    public static boolean isSmtEnabled() {
        return smtEnabled;
    }

    public static void setSmtEnabled(boolean smtEnabled) {
        AccountDetails.smtEnabled = smtEnabled;
    }

    public static boolean isAufsb() {
        return aufsb;
    }

    public static void setAufsb(boolean aufsb) {
        AccountDetails.aufsb = aufsb;
    }
}
