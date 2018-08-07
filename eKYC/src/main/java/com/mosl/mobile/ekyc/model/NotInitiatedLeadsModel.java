package com.mosl.mobile.ekyc.model;

/**
 * Created by Shefali on 11/08/16.
 */
public class NotInitiatedLeadsModel {
    String leadID, name, mobNo, stage, status, createdDate, numAcc;


    public NotInitiatedLeadsModel(String leadID, String name, String mobNo, String stage, String status, String createdDate, String numAcc) {
        this.leadID = leadID;
        this.name = name;
        this.mobNo = mobNo;
        this.stage = stage;
        this.status = status;
        this.createdDate = createdDate;
        this.numAcc = numAcc;
    }


    public String getNumAcc() {
        return numAcc;
    }

    public void setNumAcc(String numAcc) {
        this.numAcc = numAcc;
    }

    public String getLeadID() {
        return leadID;
    }

    public void setLeadID(String leadID) {
        this.leadID = leadID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
