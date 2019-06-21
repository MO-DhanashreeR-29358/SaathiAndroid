package com.mosl.tabapp.model;

import com.google.gson.annotations.SerializedName;
import com.mosl.tabapp.common.CRMAppUtil;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Vijay on 30/03/2017.
 */

public class LeadCreationModel implements Serializable {
    @SerializedName("BusinessUnitID") private String businessUnitID;
    @SerializedName("LeadCategoryID") private String leadCategoryID;
    @SerializedName("ContactPerson") private String contactPerson;
    @SerializedName("SourceID") private String sourceID;
    @SerializedName("LeadSourceDetails") private String leadSourceDetails;
    @SerializedName("LeadSourceID") private String leadSourceID;
    @SerializedName("LeadSourceDetailsID") private String leadSourceDetailsID;
    @SerializedName("ProfessionID") private String professionid = "";
    @SerializedName("RefereeCode") private String refereeCode;
    @SerializedName("RefereeName") private String refereeName;
    @SerializedName("RefereeEmail") private String refereeEmail;
    @SerializedName("RefereeMobile") private String refereeMobile;
    @SerializedName("Comments") private String comments;
    @SerializedName("CreatedBy") private String createdBy;
    @SerializedName("CreatedThrough") private String createdThrough;
    @SerializedName("ProspectRefereName") private String prospectRefereName = "";
    @SerializedName("ProspectRefereMobile") private String prospectRefereMobile = "";
    @SerializedName("ProspectRefereEmail") private String ProspectRefereEmail = "";
    @SerializedName("CreationDate") private String creationDate;
    @SerializedName("contact") private LeadCreationContact contact = new LeadCreationContact();
    @SerializedName("leadProductMapping") private LeadCreationProductMapping productMapping =
        new LeadCreationProductMapping();
    private transient String profileID;
    private transient List<ContactDetailModel> contactDetailModels;
    private transient List<AddressDetailModel> addressDetailModels;
    private transient List<LeadCreationProductMapping> productDetailModels;

    public void setAlternateMobileNo(String alternateMobileNo) {
        contact.setAlternateMobileNo(alternateMobileNo);
    }

    public LeadCreationContact getContact() {
        return contact;
    }

    public void setContact(LeadCreationContact contact) {
        this.contact = contact;
    }

    public void seteMail(String eMail) {
        contact.seteMail(eMail);
    }

    public void setAlternateEmail(String alternateEmail) {
        contact.setAlternateEmail(alternateEmail);
    }

    public void setMobileNo(String mobileNo) {
        contact.setMobileNo(mobileNo);
    }

    public void setLandLineNo(String landLineNo) {
        contact.setLandLineNo(landLineNo);
    }

    public String getBusinessUnitID() {
        return businessUnitID;
    }

    public void setBusinessUnitID(String businessUnitID) {
        this.businessUnitID = businessUnitID;
    }

    public String getLeadCategoryID() {
        return leadCategoryID;
    }

    public void setLeadCategoryID(String leadCategoryID) {
        this.leadCategoryID = leadCategoryID;
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

    public void setName(String name) {
        contact.setName(name);
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

    public void setInterestedIn(String interestedIn) {
        productMapping.setInterestedIn(interestedIn);
    }

    public void setProductID(String productID) {
        productMapping.setProductID(productID);
    }

    public void setPrimaryRM(String primaryRM) {
        productMapping.setPrimaryRM(primaryRM);
    }

    public void setClientCode(String clientCode) {
        productMapping.setClientCode(clientCode);
    }

    public void setAssignedBy(String assignedBy) {
        productMapping.setAssignedBy(assignedBy);
    }

    public void setAssignedByDate(String assignedByDate) {
        productMapping.setAssignedByDate(assignedByDate);
    }

    public void setStageID(String stageID) {
        productMapping.setStageID(stageID);
    }

    public void setStatusID(String statusID) {
        productMapping.setStatusID(statusID);
    }

    public void setAddresses(AddressDetailModel officeAddress,
        AddressDetailModel residenceAddress) {
        contact.setAddresses(officeAddress, residenceAddress);
    }

    public void setEkycMobileNo(String ekycMobileNo) {
        contact.setEkycMobileNo(ekycMobileNo);
    }

    public void setPanNo(String panNo) {
        contact.setPanNo(panNo);
    }

    public void setAadharNo(String aadharNo) {
        contact.setAadharNo(aadharNo);
    }

    public String getProfessionid() {
        return professionid;
    }

    public void setProfessionid(String professionid) {
        this.professionid = professionid;
    }

    public String getProspectRefereName() {
        return prospectRefereName;
    }

    public void setProspectRefereName(String prospectRefereName) {
        this.prospectRefereName = prospectRefereName;
    }

    public String getProspectRefereMobile() {
        return prospectRefereMobile;
    }

    public void setProspectRefereMobile(String prospectRefereMobile) {
        this.prospectRefereMobile = prospectRefereMobile;
    }

    public String getProspectRefereEmail() {
        return ProspectRefereEmail;
    }

    public void setProspectRefereEmail(String prospectRefereEmail) {
        ProspectRefereEmail = prospectRefereEmail;
    }

    public List<LeadCreationProductMapping> getProductDetailModels() {
        return productDetailModels;
    }

    public void setProductDetailModels(
        List<LeadCreationProductMapping> productDetailModels) {
        this.productDetailModels = productDetailModels;
    }

    public LeadCreationProductMapping getProductMapping() {
        return productMapping;
    }

    public String getJson() {
        String json = "{";
        json += "\"BusinessUnitID\":" + getBusinessUnitID();
        json += ",\"LeadCategoryID\":" + getLeadCategoryID();
        if (!CRMAppUtil.isBlank(getContactPerson())) {
            json += ",\"ContactPerson\":\"" + getContactPerson() + "\"";
        }
        json += ",\"SourceID\":" + getSourceID();
        json += ",\"LeadSourceDetails\":\"" + getLeadSourceDetails() + "\"";
        json += ",\"LeadSourceID\":" + getLeadSourceID();
        json += ",\"LeadSourceDetailsID\":" + getLeadSourceDetailsID();
        json += ",\"ProfessionID\":" + getProfessionid();
        if (!CRMAppUtil.isBlank(getRefereeCode())) {
            json += ",\"RefereeCode\":\"" + getRefereeCode() + "\"";
        }
        if (!CRMAppUtil.isBlank(getRefereeName())) {
            json += ",\"RefereeName\":\"" + getRefereeName() + "\"";
        }
        if (!CRMAppUtil.isBlank(getRefereeEmail())) {
            json += ",\"RefereeEmail\":\"" + getRefereeEmail() + "\"";
        }
        if (!CRMAppUtil.isBlank(getRefereeMobile())) {
            json += ",\"RefereeMobile\":\"" + getRefereeMobile() + "\"";
        }
        json += ",\"Comments\":\"" + getComments() + "\"";
        if (!CRMAppUtil.isBlank(getCreatedBy())) {
            json += ",\"CreatedBy\":\"" + getCreatedBy() + "\"";
        }
        json += ",\"CreatedThrough\":\"" + getCreatedThrough() + "\"";
        json += ",\"ProspectRefereName\":\"" + getProspectRefereName() + "\"";
        json += ",\"ProspectRefereMobile\":\"" + getProspectRefereMobile() + "\"";
        json += ",\"ProspectRefereEmail\":\"" + getProspectRefereEmail() + "\"";
        json += ",\"CreationDate\":\"" + getCreationDate() + "\"";
        json += ",\"contact\":" + contact.getContactJson();
        json += ",\"leadProductMapping\":" + productMapping.getLeadProductMapping();

        json += "}";
        return json;
    }

    public void setSameAsAbove(boolean sameAsAbove) {
        contact.setSameAsAbove(sameAsAbove);
    }

    public String getLeadSourceDetails() {
        return leadSourceDetails;
    }

    public void setLeadSourceDetails(String leadSourceDetails) {
        this.leadSourceDetails = leadSourceDetails;
    }
}
