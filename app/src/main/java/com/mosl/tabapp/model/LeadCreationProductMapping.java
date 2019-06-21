package com.mosl.tabapp.model;

import com.google.gson.annotations.SerializedName;
import com.mosl.tabapp.common.CRMAppUtil;

public class LeadCreationProductMapping {
    @SerializedName("InterestedIn") private String interestedIn;
    @SerializedName("ProductID") private String productID;
    @SerializedName("PrimaryRM") private String primaryRM;
    @SerializedName("ClientCode") private String clientCode;
    @SerializedName("AssignedBy") private String assignedBy;
    @SerializedName("AssignedByDate") private String assignedByDate;
    @SerializedName("StageID") private String stageID;
    @SerializedName("StatusID") private String statusID;

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPrimaryRM() {
        return primaryRM;
    }

    public void setPrimaryRM(String primaryRM) {
        this.primaryRM = primaryRM;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedByDate() {
        return assignedByDate;
    }

    public void setAssignedByDate(String assignedByDate) {
        this.assignedByDate = assignedByDate;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getLeadProductMapping() {
        String json = "[";
        json += "{";
        json += "\"InterestedIn\":" + getInterestedIn();
        if (!CRMAppUtil.isBlank(getProductID())) {
            json += ",\"ProductID\":" + getProductID();
        }
        json += ",\"PrimaryRM\":\"" + getPrimaryRM() + "\"";
        if (!CRMAppUtil.isBlank(getClientCode())) {
            json += ",\"ClientCode\":\"" + getClientCode() + "\"";
        }

        if (!CRMAppUtil.isBlank(getAssignedBy())) {
            json += ",\"AssignedBy\":\"" + getAssignedBy() + "\"";
        }
        if (!CRMAppUtil.isBlank(getAssignedByDate())) {
            json += ",\"AssignedByDate\":\"" + getAssignedByDate() + "\"";
        }
        json += ",\"StageID\":" + getStageID();
        json += ",\"StatusID\":" + getStatusID();
        json += ",\"lead\":" + null;
        json += "}";
        json += "]";
        return json;
    }
}
