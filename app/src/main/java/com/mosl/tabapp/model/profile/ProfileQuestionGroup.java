
package com.mosl.tabapp.model.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pramod on 5-Apr-2017
 * question group
 */


public class ProfileQuestionGroup {

    private Integer profileParameterGroupID;
    private String groupName;

    public String getProfileParameterID() {
        return profileParameterID;
    }

    public void setProfileParameterID(String profileParameterID) {
        this.profileParameterID = profileParameterID;
    }

    private String profileParameterID;
    private Integer businessUnitId;
    private List<ProfileQuestion> profileQuestionList = new ArrayList<>();
    private Integer status;
    private Boolean isAdminMode;
    private Object permissions;
    private Boolean inApproval;
    private Object approvalFieldList;

    public Integer getProfileParameterGroupID() {
        return profileParameterGroupID;
    }

    public void setProfileParameterGroupID(Integer profileParameterGroupID) {
        this.profileParameterGroupID = profileParameterGroupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public List<ProfileQuestion> getProfileQuestionList() {
        return profileQuestionList;
    }

    public void setProfileQuestionList(List<ProfileQuestion> profileQuestionList) {
        this.profileQuestionList = profileQuestionList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsAdminMode() {
        return isAdminMode;
    }

    public void setIsAdminMode(Boolean isAdminMode) {
        this.isAdminMode = isAdminMode;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    public Boolean getInApproval() {
        return inApproval;
    }

    public void setInApproval(Boolean inApproval) {
        this.inApproval = inApproval;
    }

    public Object getApprovalFieldList() {
        return approvalFieldList;
    }

    public void setApprovalFieldList(Object approvalFieldList) {
        this.approvalFieldList = approvalFieldList;
    }

}
