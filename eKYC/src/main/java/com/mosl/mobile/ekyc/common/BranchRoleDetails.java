package com.mosl.mobile.ekyc.common;

/**
 * Created by Shefali on 13/09/16.
 */
public class BranchRoleDetails  {
    String text;
    String roleMappedID;
    String defaultStr;

    public BranchRoleDetails(String text, String roleMappedID,String defaultStr) {
        this.text = text;
        this.roleMappedID = roleMappedID;
        this.defaultStr = defaultStr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRoleMappedID() {
        return roleMappedID;
    }

    public void setRoleMappedID(String roleMappedID) {
        this.roleMappedID = roleMappedID;
    }

    public String getDefaultStr() {
        return defaultStr;
    }

    public void setDefaultStr(String defaultStr) {
        this.defaultStr = defaultStr;
    }
}
