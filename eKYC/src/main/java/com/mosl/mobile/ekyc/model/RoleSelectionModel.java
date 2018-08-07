package com.mosl.mobile.ekyc.model;

/**
 * Created by Shefali on 13/09/16.
 */
public class RoleSelectionModel {

    public static String boBranch;
    public static String boSubBroker;
    public static String boTraders;
    public static String crmBranch;
    public static String loginUserID;
    public static String roleMappingID;
    public static String businessUnitID;
    public static String teamID;
    public static String roleID;

    public RoleSelectionModel(String boBranch, String boSubBroker, String boTraders,
                              String crmBranch, String loginUserID, String roleMappingID,
                              String businessUnitID, String teamID, String roleID) {
        this.boBranch = boBranch;
        this.boSubBroker = boSubBroker;
        this.boTraders = boTraders;
        this.crmBranch = crmBranch;
        this.loginUserID = loginUserID;
        this.roleMappingID = roleMappingID;
        this.businessUnitID = businessUnitID;
        this.teamID = teamID;
        this.roleID = roleID;
    }

    public static String getBoBranch() {
        return boBranch;
    }

    public static void setBoBranch(String boBranch) {
        RoleSelectionModel.boBranch = boBranch;
    }

    public static String getBoSubBroker() {
        return boSubBroker;
    }

    public static void setBoSubBroker(String boSubBroker) {
        RoleSelectionModel.boSubBroker = boSubBroker;
    }

    public static String getBoTraders() {
        return boTraders;
    }

    public static void setBoTraders(String boTraders) {
        RoleSelectionModel.boTraders = boTraders;
    }

    public static String getCrmBranch() {
        return crmBranch;
    }

    public static void setCrmBranch(String crmBranch) {
        RoleSelectionModel.crmBranch = crmBranch;
    }

    public static String getLoginUserID() {
        return loginUserID;
    }

    public static void setLoginUserID(String loginUserID) {
        RoleSelectionModel.loginUserID = loginUserID;
    }

    public static String getRoleMappingID() {
        return roleMappingID;
    }

    public static void setRoleMappingID(String roleMappingID) {
        RoleSelectionModel.roleMappingID = roleMappingID;
    }

    public static String getBusinessUnitID() {
        return businessUnitID;
    }

    public static void setBusinessUnitID(String businessUnitID) {
        RoleSelectionModel.businessUnitID = businessUnitID;
    }

    public static String getTeamID() {
        return teamID;
    }

    public static void setTeamID(String teamID) {
        RoleSelectionModel.teamID = teamID;
    }

    public static String getRoleID() {
        return roleID;
    }

    public static void setRoleID(String roleID) {
        RoleSelectionModel.roleID = roleID;
    }
}
