package com.mosl.saathi.api.model.addupdatemeeting

import com.google.gson.annotations.SerializedName

data class AddUpdateMeetingNewResposne(

	@JvmField @SerializedName("Response")
	val response: Response? = null,

	@JvmField @SerializedName("Status")
	val status: Boolean? = null,

	@JvmField @SerializedName("Type")
	val type: String? = null,

	@JvmField @SerializedName("Message")
	val message: String? = null,

	@JvmField @SerializedName("Erros")
	val erros: List<ApiError>? = null
){
	val isSuccessful: Boolean
		get() = status == true

	fun getMessage(): String {
		return if(!erros.isNullOrEmpty()) erros.let { it[0].message } else ""
	}
}
data class ApiError(
	@JvmField @SerializedName("Code") val code: Int,
	@JvmField @SerializedName("Message") val message: String
)
data class Response(

	@JvmField @SerializedName("ParentID")
	val parentID: Any? = null,

	@JvmField @SerializedName("CreationDate")
	val creationDate: String? = null,

	@JvmField @SerializedName("Products")
	val products: List<Any?>? = null,

	@JvmField @SerializedName("FranchiseeCode")
	val franchiseeCode: Any? = null,

	@JvmField @SerializedName("MeetingReasonID")
	val meetingReasonID: Int? = null,

	@JvmField @SerializedName("AppTrainingType")
	val appTrainingType: Any? = null,

	@JvmField @SerializedName("GeoLongitude")
	val geoLongitude: Any? = null,

	@JvmField @SerializedName("SubTopics")
	val subTopics: Any? = null,

	@JvmField @SerializedName("SubCategoryID")
	val subCategoryID: Int? = null,

	@JvmField @SerializedName("GeoTagStatus")
	val geoTagStatus: Any? = null,

	@JvmField @SerializedName("NextMeetingDate")
	val nextMeetingDate: Any? = null,

	@JvmField @SerializedName("MeetingObjectiveID")
	val meetingObjectiveID: Int? = null,

	@JvmField @SerializedName("IsSelected")
	val isSelected: Boolean? = null,

	@JvmField @SerializedName("UCID")
	val uCID: Int? = null,

	@JvmField @SerializedName("Name")
	val name: Any? = null,

	@JvmField @SerializedName("Mandays")
	val mandays: Any? = null,

	@JvmField @SerializedName("ProspectName")
	val prospectName: Any? = null,

	@JvmField @SerializedName("FeedbackTrainingId")
	val feedbackTrainingId: Any? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@JvmField @SerializedName("TrainingTypeID")
	val trainingTypeID: Any? = null,

	@JvmField @SerializedName("IFACode")
	val iFACode: Any? = null,

	@JvmField @SerializedName("CategoryID")
	val categoryID: Int? = null,

	@JvmField @SerializedName("repeatEveryDayCode")
	val repeatEveryDayCode: Any? = null,

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("repeatationStartOn")
	val repeatationStartOn: Any? = null,

	@JvmField @SerializedName("SubCategoryDescription")
	val subCategoryDescription: Any? = null,

	@JvmField @SerializedName("CreatedBy")
	val createdBy: String? = null,

	@JvmField @SerializedName("meetingAttendee")
	val meetingAttendee: List<Any?>? = null,

	@JvmField @SerializedName("GeoLatitude")
	val geoLatitude: Any? = null,

	@JvmField @SerializedName("LookUpTypeid")
	val lookUpTypeid: Int? = null,

	@JvmField @SerializedName("ActivityTypeID")
	val activityTypeID: Int? = null,

	@JvmField @SerializedName("ProspectEmail")
	val prospectEmail: Any? = null,

	@JvmField @SerializedName("BusinessUnitId")
	val businessUnitId: Int? = null,

	@JvmField @SerializedName("OutstationMeetingID")
	val outstationMeetingID: Int? = null,

	@JvmField @SerializedName("GeoTagging")
	val geoTagging: Any? = null,

	@JvmField @SerializedName("StatusName")
	val statusName: Any? = null,

	@JvmField @SerializedName("BAClientMeetingID")
	val bAClientMeetingID: Int? = null,

	@JvmField @SerializedName("MeetingDurationID")
	val meetingDurationID: Int? = null,

	@JvmField @SerializedName("FranchiseeName")
	val franchiseeName: Any? = null,

	@JvmField @SerializedName("repeatationEndsOn")
	val repeatationEndsOn: Any? = null,

	@JvmField @SerializedName("GeoArea")
	val geoArea: Any? = null,

	@JvmField @SerializedName("DeviceType")
	val deviceType: Any? = null,

	@JvmField @SerializedName("MassTraining")
	val massTraining: Int? = null,

	@JvmField @SerializedName("TrainingTypes")
	val trainingTypes: Any? = null,

	@JvmField @SerializedName("TopicsId")
	val topicsId: Any? = null,

	@JvmField @SerializedName("MeetingSubjectID")
	val meetingSubjectID: Int? = null,

	@JvmField @SerializedName("FranchiseePrefix")
	val franchiseePrefix: Any? = null,

	@JvmField @SerializedName("IFAMeetingID")
	val iFAMeetingID: Int? = null,

	@JvmField @SerializedName("MeetingType")
	val meetingType: String? = null,

	@JvmField @SerializedName("Clientmobile")
	val clientmobile: Any? = null,

	@JvmField @SerializedName("CategoryDescription")
	val categoryDescription: Any? = null,

	@JvmField @SerializedName("LadAdvMeetingType")
	val ladAdvMeetingType: Any? = null,

	@JvmField @SerializedName("FinalAction")
	val finalAction: String? = null,

	@JvmField @SerializedName("IFAName")
	val iFAName: Any? = null,

	@JvmField @SerializedName("Pincode")
	val pincode: String? = null,

	@JvmField @SerializedName("repeatEveryWeekCode")
	val repeatEveryWeekCode: Any? = null,

	@JvmField @SerializedName("MeetingWith")
	val meetingWith: Any? = null,

	@JvmField @SerializedName("BAType")
	val bAType: Any? = null,

	@JvmField @SerializedName("ActivityTypeName")
	val activityTypeName: Any? = null,

	@JvmField @SerializedName("AttendeeName")
	val attendeeName: Any? = null,

	@JvmField @SerializedName("ProspectMobile")
	val prospectMobile: Any? = null,

	@JvmField @SerializedName("ProductTypeID")
	val productTypeID: Int? = null,

	@JvmField @SerializedName("PID_txn_meetingProduct")
	val pIDTxnMeetingProduct: Int? = null,

	@JvmField @SerializedName("LookupTypeName")
	val lookupTypeName: Any? = null,

	@JvmField @SerializedName("FeedbackcheckIds")
	val feedbackcheckIds: Any? = null,

	@JvmField @SerializedName("MeetingDate")
	val meetingDate: String? = null,

	@JvmField @SerializedName("Clientcode")
	val clientcode: Any? = null,

	@JvmField @SerializedName("ProductCategoryID")
	val productCategoryID: Int? = null,

	@JvmField @SerializedName("MeetingAddress")
	val meetingAddress: String? = null,

	@JvmField @SerializedName("LookUpName")
	val lookUpName: Any? = null,

	@JvmField @SerializedName("MeetingID")
	val meetingID: Int? = null,

	@JvmField @SerializedName("ActionableID")
	val actionableID: Int? = null,

	@JvmField @SerializedName("LeadId")
	val leadId: Int? = null,

	@JvmField @SerializedName("MeetingDescription")
	val meetingDescription: String? = null,

	@JvmField @SerializedName("AttendeeCode")
	val attendeeCode: Any? = null,

	@JvmField @SerializedName("IsSubcategoryData")
	val isSubcategoryData: Int? = null,

	@JvmField @SerializedName("EmailId2")
	val emailId2: Any? = null,

	@JvmField @SerializedName("Clientname")
	val clientname: Any? = null,

	@JvmField @SerializedName("TrainingTypeName")
	val trainingTypeName: Any? = null,

	@JvmField @SerializedName("MandaysID")
	val mandaysID: Any? = null,

	@JvmField @SerializedName("Amount")
	val amount: Int? = null,

	@JvmField @SerializedName("Mode")
	val mode: Any? = null,

	@JvmField @SerializedName("TravellingModeIds")
	val travellingModeIds: String? = null,

	@JvmField @SerializedName("MeetingAddressTypeId")
	val meetingAddressTypeId: Int? = null,

	@JvmField @SerializedName("SelectedCount")
	val selectedCount: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("TotalCount")
	val totalCount: Any? = null,

	@JvmField @SerializedName("MeetingTypeR")
	val meetingTypeR: Any? = null,

	@JvmField @SerializedName("LookUpid")
	val lookUpid: Int? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("CustomerRemark")
	val customerRemark: Any? = null,

	@JvmField @SerializedName("SubActivityTypeID")
	val subActivityTypeID: Any? = null,

	@JvmField @SerializedName("hdnMeetingID")
	val hdnMeetingID: Any? = null,

	@JvmField @SerializedName("IsPotentialPMSProduct")
	val isPotentialPMSProduct: Any? = null,

	@JvmField @SerializedName("NineToElevenMeet")
	val nineToElevenMeet: Any? = null,

	@JvmField @SerializedName("repeatOnCode")
	val repeatOnCode: Any? = null
)
