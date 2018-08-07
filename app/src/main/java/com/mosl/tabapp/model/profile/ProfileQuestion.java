
package com.mosl.tabapp.model.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pramod on 5-Apr-2017
 * Questions
 */


public class ProfileQuestion {

    private Integer profileParameterID;
    private Integer profileParameterGroupID;
    private String questionText;
    private Integer questionType; //1=radio,
    private Integer selectedProfileSubParameterID; //selected id for radio button
    private Object value; //???? submitted value for inputbox
    private List<ProfileOption> profileOptionList = new ArrayList<>();
    private Integer status;
    private Boolean isAdminMode;
    private Object permissions;
    private Boolean inApproval;
    private Object approvalFieldList;
    private int controlType;
    private String defaultHidden="";
    public String isDefaultHidden() {
        return defaultHidden;
    }

    public void setDefaultHidden(String defaultHidden) {
        this.defaultHidden = defaultHidden;
    }




    public Integer getProfileParameterID() {
        return profileParameterID;
    }

    public void setProfileParameterID(Integer profileParameterID) {
        this.profileParameterID = profileParameterID;
    }

    public Integer getProfileParameterGroupID() {
        return profileParameterGroupID;
    }

    public void setProfileParameterGroupID(Integer profileParameterGroupID) {
        this.profileParameterGroupID = profileParameterGroupID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getSelectedProfileSubParameterID() {
        return selectedProfileSubParameterID;
    }

    public void setSelectedProfileSubParameterID(Integer selectedProfileSubParameterID) {
        this.selectedProfileSubParameterID = selectedProfileSubParameterID;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<ProfileOption> getProfileOptionList() {
        return profileOptionList;
    }

    public void setProfileOptionList(List<ProfileOption> profileOptionList) {
        this.profileOptionList = profileOptionList;
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

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    /***
     * givesposition for select value
     * @return -1 if not found
     * */
    public int getDropdownValueArrPosition() {
        //  if (profileOptionList != null) {

        for (int i = 0; i < profileOptionList.size(); i++) {
            if (profileOptionList.get(i).isSelected()) {
                return i;
            }
        }
        // return 0;
        //} else
        return 0;
    }

}
