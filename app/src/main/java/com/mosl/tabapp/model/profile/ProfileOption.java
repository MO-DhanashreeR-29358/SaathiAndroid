
package com.mosl.tabapp.model.profile;

import android.view.View;

import java.util.List;

/**
 * Created by Pramod on 5-Apr-2017
 * <p>
 * options for questions
 */


public class ProfileOption {
    //for extrafields
    private Integer profileParameterID;
    private List<List<View>> extraViewList;

    private Integer profileSubParameterID;
    private String questionSubText;
    private String value;
    private boolean isSelected;
    private Integer subParameterControlType; //2=radio, 1=textfield type (textbox, radio etc)
    private Integer status;
    private Boolean isAdminMode;
    private Object permissions;
    private Boolean inApproval;
    private Object approvalFieldList;

    //Others section
    private String extraInfo="";
    private int extraInfoRowCount;
    private String extraInfoFieldName=""; //this will contain name separated by pipe (|)
    private String hideDepedentQuestion="";
    private String hide="";

    public String getHideDepedentQuestion() {
        return hideDepedentQuestion;
    }

    public void setHideDepedentQuestion(String hideDepedentQuestion) {
        this.hideDepedentQuestion = hideDepedentQuestion;
    }

    public String isHide() {
        return hide;
    }

    public void setIsHide(String isHide) {
        hide = isHide;
    }

    public Integer getProfileSubParameterID() {
        return profileSubParameterID;
    }

    public void setProfileSubParameterID(Integer profileSubParameterID) {
        this.profileSubParameterID = profileSubParameterID;
    }

    public Integer getProfileParameterID() {
        return profileParameterID;
    }

    public void setProfileParameterID(Integer profileParameterID) {
        this.profileParameterID = profileParameterID;
    }

    public String getQuestionSubText() {
        return questionSubText;
    }

    public void setQuestionSubText(String questionSubText) {
        this.questionSubText = questionSubText;
    }

    public Integer getSubParameterControlType() {
        return subParameterControlType;
    }

    public void setSubParameterControlType(Integer subParameterControlType) {
        this.subParameterControlType = subParameterControlType;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public String toString() {
        return  questionSubText ;
    }

    public Boolean getAdminMode() {
        return isAdminMode;
    }

    public void setAdminMode(Boolean adminMode) {
        isAdminMode = adminMode;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public int getExtraInfoRowCount() {
        return extraInfoRowCount;
    }

    public void setExtraInfoRowCount(int extraInfoRowCount) {
        this.extraInfoRowCount = extraInfoRowCount;
    }

    public String getExtraInfoFieldName() {
        return extraInfoFieldName;
    }

    public void setExtraInfoFieldName(String extraInfoFieldName) {
        this.extraInfoFieldName = extraInfoFieldName;
    }

    public  List<List<View>>  getExtraViewList() {
        return extraViewList;
    }

    public void setExtraViewList( List<List<View>> extraViewList) {
        this.extraViewList = extraViewList;
    }
}
