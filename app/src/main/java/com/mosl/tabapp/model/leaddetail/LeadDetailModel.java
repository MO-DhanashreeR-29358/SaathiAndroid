package com.mosl.tabapp.model.leaddetail;

import com.mosl.saathi.api.model.lead.LeadFieldValueRequest;
import com.mosl.tabapp.model.AddressDetailModel;
import com.mosl.tabapp.model.ContactDetailModel;
import java.util.List;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;

/**
 * Created by Vijay on 06/04/2017.
 */

public class LeadDetailModel {
    private List<LeadFieldValueRequest> leadFieldValueModels;
	private String leadID;
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
	private String professionID = "";
	private String productID;
	private String primaryRM;
	private String secondaryRM;
	private String stageID;
	private String statusID;
	private String eMail;
	private String alternateEmail;
	private String mobileNo;
	private String EkycMobileNo;
	private String landLineNo;
	private String dob;
	private String anniversary;
	private String panNum;
	private String aadharNum;
	private List<ContactDetailModel> contactDetailModels;
	private List<AddressDetailModel> addressDetailModels;
	private List<LeadDetailAddProductModel> leadDetailAddProductModels;
	private String alternateMobileNo;
	private String leadSourceDetails;
	private boolean sameAsAbove;
	private String isPotentialPMSProduct;
	private String prospectRefereName = "";
	private String prospectRefereMobile = "";
	private String ProspectRefereEmail = "";

	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}

	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}

	public String getLeadID() {
		return leadID;
	}

	public void setLeadID(String leadID) {
		this.leadID = leadID;
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

	public List<LeadDetailAddProductModel> getLeadDetailAddProductModels() {
		return leadDetailAddProductModels;
	}

	public void setLeadDetailAddProductModels(
		List<LeadDetailAddProductModel> leadDetailAddProductModels) {
		this.leadDetailAddProductModels = leadDetailAddProductModels;
	}

	public String getPanNum() {
		return panNum;
	}

	public void setPanNum(String panNum) {
		this.panNum = panNum;
	}

	public String getAadharNum() {
		return aadharNum;
	}

	public void setAadharNum(String aadharNum) {
		this.aadharNum = aadharNum;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(String anniversary) {
		this.anniversary = anniversary;
	}

	public String getProfessionID() {
		return professionID;
	}

    public void setProfessionID(String professionID) {
		this.professionID = professionID;
	}

	public String getIsPotentialPMSProduct() {
		return isPotentialPMSProduct;
	}

	public void setIsPotentialPMSProduct(String isPotentialPMSProduct) {
		this.isPotentialPMSProduct = isPotentialPMSProduct;
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
		json += "\"LeadID\":" + getLeadID();
		json += ",\"BusinessUnitID\":" + getBusinessUnitID();
		json += ",\"IsPotentialPMSProductValue\":\"" + getIsPotentialPMSProduct() + "\"";
		if (!isBlank(getLeadCategoryID())) {
			json += ",\"LeadCategoryID\":" + getLeadCategoryID();
		}
		if (!isBlank(getContactPerson())) {
			json += ",\"ContactPerson\":\"" + getContactPerson() + "\"";
		}
		json += ",\"SourceID\":" + getSourceID();
		json += ",\"LeadSourceID\":" + getLeadSourceID();
		json += ",\"LeadSourceDetailsID\":" + getLeadSourceDetailsID();
		json += ",\"LeadSourceDetails\":\"" + getLeadSourceDetails() + "\"";
		if (!isBlank(getRefereeCode())) {
			json += ",\"RefereeCode\":\"" + getRefereeCode() + "\"";
		}
		if (!isBlank(getRefereeName())) {
			json += ",\"RefereeName\":\"" + getRefereeName() + "\"";
		}
		if (!isBlank(getRefereeEmail())) {
			json += ",\"RefereeEmail\":\"" + getRefereeEmail() + "\"";
		}
		if (!isBlank(getRefereeMobile())) {
			json += ",\"RefereeMobile\":\"" + getRefereeMobile() + "\"";
		}
		if (!isBlank(getComments())) {
			json += ",\"Comments\":\"" + getComments() + "\"";
		}
		json += ",\"ProfileID\":" + getProfileID();
		json += ",\"ProfessionID\":" + getProfessionID();
		if (!isBlank(getCreatedBy())) {
			json += ",\"CreatedBy\":\"" + getCreatedBy() + "\"";
		}
		json += ",\"ProspectRefereName\":\"" + getProspectRefereName() + "\"";
		json += ",\"ProspectRefereMobile\":\"" + getProspectRefereMobile() + "\"";
		json += ",\"ProspectRefereEmail\":\"" + getProspectRefereEmail() + "\"";
		json += ",\"CreatedThrough\":" + getCreatedThrough();
		json += ",\"CreationDate\":\"" + getCreationDate() + "\"";
		json += ",\"contact\":" + getContactJson();
		json += ",\"leadFieldValueModels\":" + leadFieldValueModels;
		// json += ",\"leadProductMapping\":" + getLeadProductMapping();

		json += "}";
		return json;
	}

	private String getContactJson() {
		String json = "{";
		json += ",\"Name\":\"" + getName() + "\"";
		json += ",\"EmailId\":\"" + geteMail() + "\"";
		json += ",\"AlternateEmailId\":\"" + getAlternateEmail() + "\"";
		json += ",\"Mobile\":\"" + getMobileNo() + "\"";
		json += ",\"EKYCMobileNo\":\"" + getEkycMobileNo() + "\"";
		json += ",\"LandlineNo\":\"" + getLandLineNo() + "\"";
		json += ",\"AlternateMobileNo\":\"" + getAlternateMobileNo() + "\"";
		json += ",\"AadharNumber\":\"" + getAadharNum() + "\"";
		json += ",\"PanNumber\":\"" + getPanNum() + "\"";
		json += ",\"DOB\":\"" + getDob() + "\"";
		json += ",\"DOA\":\"" + getAnniversary() + "\"";
		json += ",\"IsSameAsAbove\":\"" + isSameAsAbove() + "\"";
		json += ",\"contactAddress\":" + getAddressJson();
		json += "}";
		return json;
	}

	private String getAddressJson() {
		String json = "[";
		for (AddressDetailModel model : addressDetailModels) {
			String meetingJson = model.getJson();
			json += meetingJson + ",";
		}

		json = json.substring(0, json.length() - 1); //remove lash comma
		json += "]";
		return json;
	}

	private String getLeadProductMapping() {
		String json = "[";
		for (LeadDetailAddProductModel model : leadDetailAddProductModels) {
			String meetingJson = model.getJson();
			json += meetingJson + ",";
		}
		json = json.substring(0, json.length() - 1); //remove lash comma
		json += "]";
		return json;
	}

    public List<LeadFieldValueRequest> getLeadFieldValueModels() {
		return leadFieldValueModels;
	}

    public void setLeadFieldValueModels(List<LeadFieldValueRequest> leadFieldValueModels) {
		this.leadFieldValueModels = leadFieldValueModels;
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

	public String getEkycMobileNo() {
		return EkycMobileNo;
	}

	public void setEkycMobileNo(String ekycMobileNo) {
		EkycMobileNo = ekycMobileNo;
	}
}
