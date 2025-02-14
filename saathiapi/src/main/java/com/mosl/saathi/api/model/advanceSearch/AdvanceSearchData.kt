package com.mosl.saathi.api.model.advanceSearch

import com.google.gson.annotations.SerializedName

data class AdvanceSearchData(

	@JvmField  @SerializedName("EquityAdvisor")
	val equityAdvisor: Any? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("LastModifiedDate")
	val lastModifiedDate: String? = null,

	@JvmField @SerializedName("ActionablesList")
	val actionablesList: List<Any?>? = null,

	@JvmField @SerializedName("Email")
	val email: String? = null,

	@JvmField @SerializedName("ClientProfile")
	val clientProfile: Any? = null,

	@JvmField @SerializedName("ProductName")
	val productName: String? = null,

	@JvmField @SerializedName("StateName")
	val stateName: Any? = null,

	@JvmField @SerializedName("ClientStatusList")
	val clientStatusList: List<Any?>? = null,

	@JvmField @SerializedName("StageStatus")
	val stageStatus: String? = null,

	@JvmField @SerializedName("ProspectSourceList")
	val prospectSourceList: List<Any?>? = null,

	@JvmField @SerializedName("UCID")
	val uCID: Int? = null,

	@JvmField @SerializedName("Name")
	val name: String? = null,

	@JvmField @SerializedName("ProspectSource")
	val prospectSource: String? = null,

	@JvmField @SerializedName("StatusNameID")
	val statusNameID: Int? = null,

	@JvmField @SerializedName("AumValue")
	val aumValue: Any? = null,

	@JvmField @SerializedName("LeadTypeList")
	val leadTypeList: List<Any?>? = null,

	@JvmField @SerializedName("Stage")
	val stage: String? = null,

	@JvmField @SerializedName("AUM")
	val aUM: Any? = null,

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("res")
	val res: Int? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: Any? = null,

	@JvmField @SerializedName("ActivityDate")
	val activityDate: Any? = null,

	@JvmField @SerializedName("City")
	val city: String? = null,

	@JvmField @SerializedName("CityName")
	val cityName: Any? = null,

	@JvmField @SerializedName("SearchTypeId")
	val searchTypeId: Int? = null,

	@JvmField @SerializedName("StageCodeID")
	val stageCodeID: Int? = null,

	@JvmField @SerializedName("LeadProfileID")
	val leadProfileID: Int? = null,

	@JvmField @SerializedName("LastMeetingDate")
	val lastMeetingDate: Any? = null,

	@JvmField @SerializedName("LeadTypeID")
	val leadTypeID: Int? = null,

	@JvmField @SerializedName("DPValueID")
	val dPValueID: Int? = null,

	@JvmField @SerializedName("AadhaarNumber")
	val aadhaarNumber: Any? = null,

	@JvmField @SerializedName("CityID")
	val cityID: Int? = null,

	@JvmField @SerializedName("AUMList")
	val aUMList: List<Any?>? = null,

	@JvmField @SerializedName("ClientProfileList")
	val clientProfileList: List<Any?>? = null,

	@JvmField @SerializedName("ClientProfileID")
	val clientProfileID: Int? = null,

	@JvmField @SerializedName("EncUCID")
	val encUCID: Any? = null,

	@JvmField @SerializedName("AumID")
	val aumID: Int? = null,

	@JvmField @SerializedName("FamilyUCID")
	val familyUCID: Int? = null,

	@JvmField @SerializedName("CallDate")
	val callDate: Any? = null,

	@JvmField @SerializedName("AcActivationDate")
	val acActivationDate: String? = null,

	@JvmField @SerializedName("LeadAssignedDate")
	val leadAssignedDate: Any? = null,

	@JvmField @SerializedName("ActivityTypeId")
	val activityTypeId: Int? = null,

	@JvmField @SerializedName("Equity")
	val equity: String? = null,

	@JvmField @SerializedName("AdvancedSearchID")
	val advancedSearchID: Any? = null,

	@JvmField @SerializedName("Source")
	val source: String? = null,

	@JvmField @SerializedName("Profile")
	val profile: Any? = null,

	@JvmField @SerializedName("DPValue")
	val dPValue: Any? = null,

	@JvmField @SerializedName("CityList")
	val cityList: List<Any?>? = null,

	@JvmField @SerializedName("Branch")
	val branch: Any? = null,

	@JvmField @SerializedName("ProductCategoryID")
	val productCategoryID: Int? = null,

	@JvmField @SerializedName("MeetingID")
	val meetingID: Int? = null,

	@JvmField @SerializedName("ActionableID")
	val actionableID: Int? = null,

	@JvmField @SerializedName("ProductCategoryList")
	val productCategoryList: List<Any?>? = null,

	@JvmField @SerializedName("ProspectSourceID")
	val prospectSourceID: Int? = null,

	@JvmField @SerializedName("InterestedInList")
	val interestedInList: List<Any?>? = null,

	@JvmField @SerializedName("LeadProfileList")
	val leadProfileList: List<Any?>? = null,

	@JvmField @SerializedName("EquityCode")
	val equityCode: Any? = null,

	@JvmField @SerializedName("StageCodeList")
	val stageCodeList: List<Any?>? = null,

	@JvmField @SerializedName("SourceID")
	val sourceID: Int? = null,

	@JvmField @SerializedName("StateID")
	val stateID: Int? = null,

	@JvmField @SerializedName("StatusNameList")
	val statusNameList: List<Any?>? = null,

	@JvmField @SerializedName("ProductID")
	val productID: Int? = null,

	@JvmField @SerializedName("SubbrokerCode")
	val subbrokerCode: Any? = null,

	@JvmField @SerializedName("Mobile")
	val mobile: String? = null,

	@JvmField @SerializedName("SavedSearchName")
	val savedSearchName: Any? = null,

	@JvmField @SerializedName("InterestedIn")
	val interestedIn: String? = null,

	@JvmField @SerializedName("AssignedTo")
	val assignedTo: String? = null,

	@JvmField @SerializedName("FirstTradedDate")
	val firstTradedDate: Any? = null,

	@JvmField @SerializedName("ClientStatusID")
	val clientStatusID: Int? = null,

	@JvmField @SerializedName("MeetingCallList")
	val meetingCallList: List<Any?>? = null,

	@JvmField @SerializedName("SourceList")
	val sourceList: List<Any?>? = null,

	@JvmField @SerializedName("ClientCode")
	val clientCode: Any? = null,

	@JvmField @SerializedName("EncLeadID")
	val encLeadID: Any? = null,

	@JvmField @SerializedName("InterestedInID")
	val interestedInID: Int? = null,

	@JvmField @SerializedName("LeadID")
	val leadID: Int? = null,

	@JvmField @SerializedName("CallTime")
	val callTime: Any? = null,

	@JvmField @SerializedName("PAN")
	val pAN: Any? = null,

	@JvmField @SerializedName("MTDRevenue")
	val mTDRevenue: Any? = null,

	@JvmField @SerializedName("DPValueList")
	val dPValueList: List<Any?>? = null
)
