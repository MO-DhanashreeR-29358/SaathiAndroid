
package com.mosl.tabapp.model.profile;

import java.util.List;
/**
 * Created by Pramod on 5-Apr-2017
 */
public class ProfileModel {

    private List<ProfileQuestionGroup> profileQuestionGroup = null;
    private String message;
    private Boolean status;
    private List<Object> erros = null;

    public List<ProfileQuestionGroup> getProfileQuestionGroup() {
        return profileQuestionGroup;
    }

    public void setProfileQuestionGroup(List<ProfileQuestionGroup> profileQuestionGroup) {
        this.profileQuestionGroup = profileQuestionGroup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Object> getErros() {
        return erros;
    }

    public void setErros(List<Object> erros) {
        this.erros = erros;
    }

}
