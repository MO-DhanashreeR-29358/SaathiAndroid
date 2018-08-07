package com.mosl.tabapp.model.profile;

/**
 * Created by Sanket Gurav on 4/6/2017.
 * <p>
 * {
 * "leadProfileParameterID": 0,
 * "profileParameterID": 1,
 * "profileSubParameterID": 2,
 * "leadID": 2384,
 * "value": null,
 * "ProfileParameter": null,
 * "ProfileSubParameter": null,
 * "lead": null,
 * "isAdminMode": true,
 * "Status": 1,
 * "InApproval": false,
 * "ApprovalFieldList": null
 * }
 */

public class ProfileAnswerModal {
    private int leadProfileParameterID = 0;
    private int profileParameterID;
    private int profileSubParameterID;
    private String leadID;//: 2384,
    private String value;

    public ProfileAnswerModal(int leadProfileParameterID, int profileParameterID, int profileSubParameterID, String leadID, String value) {
        this.leadProfileParameterID = leadProfileParameterID;
        this.profileParameterID = profileParameterID;
        this.profileSubParameterID = profileSubParameterID;
        this.leadID = leadID;
        this.value = value;
    }

    public ProfileAnswerModal(String[] strArr, String leadID, String value) {
        this.leadProfileParameterID = 0;
        this.profileParameterID = Integer.parseInt(strArr[0]);
        this.profileSubParameterID = Integer.parseInt(strArr[1]);
        this.leadID = leadID;
        this.value = value;
    }

    public ProfileAnswerModal( int profileParameterID, int profileSubParameterID,String value) {
        this.profileParameterID = profileParameterID;
        this.profileSubParameterID = profileSubParameterID;
        this.value = value;
    }
    @Override
    public String toString() {
        String json = "{";
        json += "\"LeadProfileParameterID\":" + getLeadProfileParameterID();
        json += ",\"ProfileParameterID\":" + getProfileParameterID();
        json += ",\"ProfileSubParameterID\":" + getProfileSubParameterID();
        json += ",\"LeadID\":" + getLeadID();
        if (getValue() != null) {
            json += ",\"Value\":\"" + getValue() + "\"";
        } else {
            json += ",\"Value\":" + getValue();
        }
        json += "}";
        return json;
    }

    public int getLeadProfileParameterID() {
        return leadProfileParameterID;
    }

    public void setLeadProfileParameterID(int leadProfileParameterID) {
        this.leadProfileParameterID = leadProfileParameterID;
    }

    public int getProfileParameterID() {
        return profileParameterID;
    }

    public void setProfileParameterID(int profileParameterID) {
        this.profileParameterID = profileParameterID;
    }

    public int getProfileSubParameterID() {
        return profileSubParameterID;
    }

    public void setProfileSubParameterID(int profileSubParameterID) {
        this.profileSubParameterID = profileSubParameterID;
    }

    public String getLeadID() {
        return leadID;
    }

    public void setLeadID(String leadID) {
        this.leadID = leadID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
