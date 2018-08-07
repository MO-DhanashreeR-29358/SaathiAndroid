package com.mosl.mobile.ekyc.model;

import java.io.Serializable;

/**
 * Created by Shefali on 09/09/16.
 */
public class BranchLeadsModel implements Serializable {
    String leadID, leadName, mobNo, stage, status, createdDate, userID, isAadhar;


    public BranchLeadsModel(String leadID, String leadName, String mobNo, String stage, String status, String createdDate, String userID, String isAadhar) {
        this.leadID = leadID;
        this.leadName = leadName;
        this.mobNo = mobNo;
        this.stage = stage;
        this.status = status;
        this.createdDate = createdDate;
        this.userID = userID;
        this.isAadhar = isAadhar;
    }

    public String getLeadID() {
        return leadID;
    }

    public void setLeadID(String leadID) {
        this.leadID = leadID;
    }


    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIsAadhar() {
        return isAadhar;
    }

    public void setIsAadhar(String isAadhar) {
        this.isAadhar = isAadhar;
    }
}

