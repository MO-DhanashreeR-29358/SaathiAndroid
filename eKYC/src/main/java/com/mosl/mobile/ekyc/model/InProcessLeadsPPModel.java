package com.mosl.mobile.ekyc.model;

/**
 * Created by Shefali on 11/08/16.
 */
public class InProcessLeadsPPModel {
    String userID, clientName, mobNo, emailID, clientStatus, modifiedDate, numAccount;
    /*"UserID":1451430,
            "ClientName":"Asad K Sheikh",
            "PanNo":"BEAPP3096Q",
            "MobileNo":"9819025988",
            "EmailID":"jeev@gmail.com",
            "ModifiedDate":"05\/08\/2016 14:40:00",
            "ClientStatus":"Completed",
            "NumAccount":2*/

    public InProcessLeadsPPModel(String userID, String clientName, String mobNo, String emailID, String clientStatus, String modifiedDate, String numAccount) {
        this.userID = userID;
        this.clientName = clientName;
        this.mobNo = mobNo;
        this.emailID = emailID;
        this.clientStatus = clientStatus;
        this.modifiedDate = modifiedDate;
        this.numAccount = numAccount;

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

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getNumAccount() {
        return numAccount;
    }

    public void setNumAccount(String numAccount) {
        this.numAccount = numAccount;
    }
}
