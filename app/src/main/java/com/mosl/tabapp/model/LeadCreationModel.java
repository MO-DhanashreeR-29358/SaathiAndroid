package com.mosl.tabapp.model;

import com.mosl.tabapp.common.CRMAppUtil;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Vijay on 30/03/2017.
 */

public class LeadCreationModel implements Serializable {
	private String businessUnitID;
	private String leadCategoryID;
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
	private String interestedIn;
	private String professionid = "";
	private String productID;
	private String primaryRM;
	private String secondaryRM;
	private String clientCode;
	private String assignedBy;
	private String assignedByDate;
	private String stageID;
	private String statusID;
	private List<ContactDetailModel> contactDetailModels;
	private List<AddressDetailModel> addressDetailModels;
	private String eMail;
	private String alternateEmail;
	private String mobileNo;
	private String landLineNo;
	private String ekycMobileNo;
	private String panNo;
	private String aadharNo;
	private AddressDetailModel residenceAddress, officeAddress;
	private String alternateMobileNo;
	private String leadSourceDetails;
	private String prospectRefereName = "";
	private String prospectRefereMobile = "";
	private String ProspectRefereEmail = "";
	private boolean sameAsAbove;

	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}

	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getLandLineNo() {
		return landLineNo;
	}

	public void setLandLineNo(String landLineNo) {
		this.landLineNo = landLineNo;
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

	public AddressDetailModel getResidenceAddress() {
		return residenceAddress;
	}

	public void setResidenceAddress(AddressDetailModel residenceAddress) {
		this.residenceAddress = residenceAddress;
	}

	public AddressDetailModel getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(AddressDetailModel officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getEkycMobileNo() {
		return ekycMobileNo;
	}

	public void setEkycMobileNo(String ekycMobileNo) {
		this.ekycMobileNo = ekycMobileNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getProfessionid() {
		return professionid;
	}

	public LeadCreationModel setProfessionid(String professionid) {
		this.professionid = professionid;
		return this;
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
		json += ",\"contact\":" + getContactJson();
		json += ",\"leadProductMapping\":" + getLeadProductMapping();

		json += "}";
		return json;
	}

	private String getContactJson() {
		String json = "{";
		json += "\"Salutation\":" + null;
		json += ",\"Name\":\"" + getName() + "\"";
		json += ",\"EmailId\":\"" + geteMail() + "\"";
		json += ",\"AlternateEmailId\":\"" + getAlternateEmail() + "\"";
		json += ",\"AlternateMobileNo\":\"" + getAlternateMobileNo() + "\"";
		json += ",\"Mobile\":\"" + getMobileNo() + "\"";
		json += ",\"LandlineNo\":\"" + getLandLineNo() + "\"";
		json += ",\"EKYCMobileNo\":\"" + getEkycMobileNo() + "\"";
		json += ",\"PanNumber\":\"" + getPanNo() + "\"";
		json += ",\"AadharNumber\":\"" + getAadharNo() + "\"";
		json += ",\"IsSameAsAbove\":\"" + isSameAsAbove() + "\"";
		json += ",\"contactAddress\":" + getAddressJson();
		json += "}";
		return json;
	}

	private String getAddressJson() {
		String json = "[";
		String meetingJson = officeAddress.getJson();
		json += meetingJson + ",";

		meetingJson = residenceAddress.getJson();
		json += meetingJson + ",";
		json = json.substring(0, json.length() - 1); //remove lash comma
		json += "]";
		return json;
	}

	private String getLeadProductMapping() {
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

	public boolean isSameAsAbove() {
		return sameAsAbove;
	}

	public void setSameAsAbove(boolean sameAsAbove) {
		this.sameAsAbove = sameAsAbove;
	}

	public String getLeadSourceDetails() {
		return leadSourceDetails;
	}

	public void setLeadSourceDetails(String leadSourceDetails) {
		this.leadSourceDetails = leadSourceDetails;
	}
}
