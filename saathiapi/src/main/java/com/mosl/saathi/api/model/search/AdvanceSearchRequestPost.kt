package com.mosl.saathi.api.model.search

import com.google.gson.annotations.SerializedName

data class AdvanceSearchRequestPost(

	@SerializedName("FamilyUCID")
	@JvmField var familyUCID: Int? = null,

	@SerializedName("LastModifiedDate")
	@JvmField var lastModifiedDate: String? = null,

	@SerializedName("Email")
	@JvmField var email: String? = null,

	@SerializedName("ClientProfile")
	@JvmField var clientProfile: String? = null,

	@SerializedName("AcActivationDate")
	@JvmField var acActivationDate: String? = null,

	@SerializedName("PageSize")
	@JvmField var pageSize: Int? = 10,

	@SerializedName("LeadAssignedDate")
	@JvmField var leadAssignedDate: String? = null,

	@SerializedName("ActivityTypeId")
	@JvmField var activityTypeId: Int? = 0,

	@SerializedName("Page")
	@JvmField var page: Int? = 1,

	@SerializedName("UCID")
	@JvmField var uCID: Int? = 0,

	@SerializedName("Name")
	@JvmField var name: String? = null,

	@SerializedName("DPValue")
	@JvmField var dPValue: String? = null,

	@SerializedName("StatusNameID")
	@JvmField var statusNameID: Int? = 0,

	@SerializedName("PANNumber")
	@JvmField var pANNumber: String? = null,

	@SerializedName("ProductCategoryID")
	@JvmField var productCategoryID: Int? = 0,

	@SerializedName("AumValue")
	@JvmField var aumValue: String? = null,

	@SerializedName("MeetingID")
	@JvmField var meetingID: Int? = 0,

	@SerializedName("ActionableID")
	@JvmField var actionableID: Int? = 0,

	@SerializedName("ProspectSourceID")
	@JvmField var prospectSourceID: Int? = 0,

	@SerializedName("ActivityDate")
	@JvmField var activityDate: String? = null,

	@SerializedName("CalculatedPage")
	@JvmField var calculatedPage: Int? = 0,

	@SerializedName("SourceID")
	@JvmField var sourceID: Int? = 0,

	@SerializedName("StateID")
	@JvmField var stateID: Int? = 0,

	@SerializedName("SearchTypeId")
	@JvmField var searchTypeId: Int? = 0,

	@SerializedName("Mobile")
	@JvmField var mobile: String? = null,

	@SerializedName("StageCodeID")
	@JvmField var stageCodeID: Int? = 0,

	@SerializedName("LeadProfileID")
	@JvmField var leadProfileID: Int? = 0,

	@SerializedName("LeadTypeID")
	@JvmField var leadTypeID: Int? = 0,

	@SerializedName("DPValueID")
	@JvmField var dPValueID: Int? = 0,

	@SerializedName("FirstTradedDate")
	@JvmField var firstTradedDate: String? = null,

	@SerializedName("TotalCount")
	@JvmField var totalCount: Int? = 0,

	@SerializedName("AadhaarNumber")
	@JvmField var aadhaarNumber: String? = null,

	@SerializedName("CityID")
	@JvmField var cityID: Int? = 0,

	@SerializedName("Filter")
	@JvmField var filter: String? = null,

	@SerializedName("ClientStatusID")
	@JvmField var clientStatusID: Int? = 0,

	@SerializedName("ClientCode")
	@JvmField var clientCode: String? = null,

	@SerializedName("InterestedInID")
	@JvmField var interestedInID: Int? = 0,

	@SerializedName("LeadID")
	@JvmField var leadID: Int? = 0,

	@SerializedName("ClientProfileID")
	@JvmField var clientProfileID: Int? = 0,

	@SerializedName("AumID")
	@JvmField var aumID: Int? = 0
)
