package com.mosl.tabapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vijay on 13/04/2017.
 */

public class FranchiseLeadCreationModel implements Serializable {
    private String businessUnitID;
    private String contactPerson;
    private String sourceID;
    private String leadSourceID;
    private String leadSourceDetailsID;
    private String refereeCode;
    private String refereeName;
    private String refereeEmail;
    private String refereeMobile;
    private String comments;
    private String profileID;
    private String active;
    private String createdThrough;
    private String createdBy;
    private String creationDate;
    private String name;
    private String primaryRM;
    private String secondaryRM;
    private String clientCode;
    private String assignedBy;
    private String assignedByDate;
    private String stageID;
    private String statusID;
    private List<ContactDetailModel> contactDetailModels;
    private List<AddressDetailModel> addressDetailModels;

    public String getBusinessUnitID() {
        return businessUnitID;
    }

    public void setBusinessUnitID(String businessUnitID) {
        this.businessUnitID = businessUnitID;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getLeadSourceID() {
        return leadSourceID;
    }

    public void setLeadSourceID(String leadSourceID) {
        this.leadSourceID = leadSourceID;
    }

    public String getLeadSourceDetailsID() {
        return leadSourceDetailsID;
    }

    public void setLeadSourceDetailsID(String leadSourceDetailsID) {
        this.leadSourceDetailsID = leadSourceDetailsID;
    }

    public String getRefereeCode() {
        return refereeCode;
    }

    public void setRefereeCode(String refereeCode) {
        this.refereeCode = refereeCode;
    }

    public String getRefereeName() {
        return refereeName;
    }

    public void setRefereeName(String refereeName) {
        this.refereeName = refereeName;
    }

    public String getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeEmail(String refereeEmail) {
        this.refereeEmail = refereeEmail;
    }

    public String getRefereeMobile() {
        return refereeMobile;
    }

    public void setRefereeMobile(String refereeMobile) {
        this.refereeMobile = refereeMobile;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreatedThrough() {
        return createdThrough;
    }

    public void setCreatedThrough(String createdThrough) {
        this.createdThrough = createdThrough;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryRM() {
        return primaryRM;
    }

    public void setPrimaryRM(String primaryRM) {
        this.primaryRM = primaryRM;
    }

    public String getSecondaryRM() {
        return secondaryRM;
    }

    public void setSecondaryRM(String secondaryRM) {
        this.secondaryRM = secondaryRM;
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

    public List<ContactDetailModel> getContactDetailModels() {
        return contactDetailModels;
    }

    public void setContactDetailModels(List<ContactDetailModel> contactDetailModels) {
        this.contactDetailModels = contactDetailModels;
    }

    public List<AddressDetailModel> getAddressDetailModels() {
        return addressDetailModels;
    }

    public void setAddressDetailModels(List<AddressDetailModel> addressDetailModels) {
        this.addressDetailModels = addressDetailModels;
    }

    public String getJson() {
        String json = "{";
        return json;
    }
}
