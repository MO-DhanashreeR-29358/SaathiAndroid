package com.mosl.mobile.ekyc.model;

import java.io.Serializable;

/**
 * Created by Shefali on 11/08/16.
 */
public class FranchiseLeadsModel implements Serializable {
    String userID, clientName, mobNo, modifiedDate, clientStatus, numAccount, clientCode, baCode;

    /* {
                           "UserID":1451430.0,
                               "ClientName":"Asad K Sheikh",
                               "PanNo":"BEAPP3096Q",
                               "MobileNo":"9819025988",
                               "EmailID":"jeev@gmail.com",
                               "ModifiedDate":"05/08/2016 14:40:00",
                               "ClientStatus":"Completed",
                               "NumAccount":2*/
    public FranchiseLeadsModel(String userID, String clientName, String mobNo, String modifiedDate, String clientStatus, String numAccount, String clientCode, String baCode) {
        this.userID = userID;
        this.clientName = clientName;
        this.mobNo = mobNo;
        this.modifiedDate = modifiedDate;
        this.clientStatus = clientStatus;
        this.numAccount = numAccount;
        this.clientCode = clientCode;
        this.baCode = baCode;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getNumAccount() {
        return numAccount;
    }

    public void setNumAccount(String numAccount) {
        this.numAccount = numAccount;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getBaCode() {
        return baCode;
    }

    public void setBaCode(String baCode) {
        this.baCode = baCode;
    }
}
