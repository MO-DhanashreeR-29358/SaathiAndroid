package com.mosl.mobile.ekyc.model;

/**
 * Created by srv_admin on 2/3/2016.
 */
public class LeadModel {
    String leadName;
    String leadCode;
    String date;
    String branch;
    String status;
    String mobNum;
    String leadID;
    String id;

    public LeadModel(String leadName, String leadCode, String date, String branch, String status, String mobNum,String leadID) {
        this.leadName = leadName;
        this.leadCode = leadCode;
        this.date = date;
        this.branch = branch;
        this.status = status;
        this.mobNum = mobNum;
        this.leadID = leadID;
    }

    public LeadModel(String id, String leadName) {
        this.id = id;
        this.leadName = leadName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LeadModel(String clientName) {
        this.leadName = clientName;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getLeadCode() {
        return leadCode;
    }

    public void setLeadCode(String leadCode) {
        this.leadCode = leadCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getLeadID() {
        return leadID;
    }

    public void setLeadID(String leadID) {
        this.leadID = leadID;
    }

    @Override
    public String toString() {
        return "";
    }
}
