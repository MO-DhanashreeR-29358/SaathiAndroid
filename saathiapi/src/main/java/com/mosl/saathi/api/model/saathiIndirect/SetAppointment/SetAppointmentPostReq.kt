package com.mosl.saathi.api.model.saathiIndirect.SetAppointment

import com.google.gson.annotations.SerializedName

data class SetAppointmentPostReq(

	@JvmField @SerializedName("FranchiseeCode")
	val franchiseeCode: Any? = null,

	@JvmField @SerializedName("MeetingReasonID")
	val meetingReasonID: Int? = null,

	@JvmField @SerializedName("AppTrainingType")
	val appTrainingType: Any? = null,

	@JvmField @SerializedName("lstAttendee")
	val lstAttendee: List<Any?>? = null,

	@JvmField @SerializedName("Product")
	val product: List<Any?>? = null,

	@JvmField @SerializedName("GeoTagStatus")
	val geoTagStatus: Any? = null,

	@JvmField @SerializedName("StageCode")
	val stageCode: Any? = null,

	@JvmField @SerializedName("AccountOpeningDate")
	val accountOpeningDate: Any? = null,

	@JvmField @SerializedName("UCID")
	val uCID: Int? = null,

	@JvmField @SerializedName("FeedbackTrainingId")
	val feedbackTrainingId: Any? = null,

	@JvmField @SerializedName("TrainingTypeID")
	val trainingTypeID: Any? = null,

	@JvmField @SerializedName("MeetingReason")
	val meetingReason: List<Any?>? = null,

	@JvmField @SerializedName("IFACode")
	val iFACode: Any? = null,

	@JvmField @SerializedName("repeatEveryDayCode")
	val repeatEveryDayCode: Any? = null,

	@JvmField @SerializedName("Actionables")
	val actionables: List<Any?>? = null,

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("repeatationStartOn")
	val repeatationStartOn: Any? = null,

	@JvmField @SerializedName("EmailId")
	val emailId: Any? = null,

	@JvmField @SerializedName("SubCategoryDescription")
	val subCategoryDescription: Any? = null,

	@JvmField @SerializedName("meetingAttendee")
	val meetingAttendee: List<MeetingAttendeeItem?>? = null,

	@JvmField @SerializedName("LookUpTypeid")
	val lookUpTypeid: Any? = null,

	@JvmField @SerializedName("ChkStageCode")
	val chkStageCode: Any? = null,

	@JvmField @SerializedName("FeedbackTraining")
	val feedbackTraining: Any? = null,

	@JvmField @SerializedName("repeatOn")
	val repeatOn: List<Any?>? = null,

	@JvmField @SerializedName("MeetingDurationID")
	val meetingDurationID: Int? = null,

	@JvmField @SerializedName("GeoArea")
	val geoArea: Any? = null,

	@JvmField @SerializedName("BAName")
	val bAName: Any? = null,

	@JvmField @SerializedName("SpMeetingAttendeeID")
	val spMeetingAttendeeID: Int? = null,

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
	val finalAction: Any? = null,

	@JvmField @SerializedName("TravellingModes")
	val travellingModes: List<Any?>? = null,

	@JvmField @SerializedName("repeatEveryWeekCode")
	val repeatEveryWeekCode: Any? = null,

	@JvmField @SerializedName("MeetingWith")
	val meetingWith: Any? = null,

	@JvmField @SerializedName("BAClientMeeting")
	val bAClientMeeting: List<Any?>? = null,

	@JvmField @SerializedName("BAType")
	val bAType: Any? = null,

	@JvmField @SerializedName("OutstationMeeting")
	val outstationMeeting: List<Any?>? = null,

	@JvmField @SerializedName("ProductTypeID")
	val productTypeID: Int? = null,

	@JvmField @SerializedName("lstSpAttendee")
	val lstSpAttendee: List<Any?>? = null,

	@JvmField @SerializedName("FeedbackcheckIds")
	val feedbackcheckIds: Any? = null,

	@JvmField @SerializedName("TravellingMode")
	val travellingMode: List<Any?>? = null,

	@JvmField @SerializedName("MeetingAddress")
	val meetingAddress: Any? = null,

	@JvmField @SerializedName("leaddetails")
	val leaddetails: Any? = null,

	@JvmField @SerializedName("MeetingID")
	val meetingID: Int? = null,

	@JvmField @SerializedName("ActionableID")
	val actionableID: Int? = null,

	@JvmField @SerializedName("AttendeeCode")
	val attendeeCode: String? = null,

	@JvmField @SerializedName("IsSubcategoryData")
	val isSubcategoryData: Int? = null,

	@JvmField @SerializedName("EmailId2")
	val emailId2: Any? = null,

	@JvmField @SerializedName("DisplayType")
	val displayType: String? = null,

	@JvmField @SerializedName("Clientname")
	val clientname: Any? = null,

	@JvmField @SerializedName("MandaysID")
	val mandaysID: Any? = null,

	@JvmField @SerializedName("MeetingAttendeeID")
	val meetingAttendeeID: Int? = null,

	@JvmField @SerializedName("Amount")
	val amount: Any? = null,

	@JvmField @SerializedName("LocationId")
	val locationId: Int? = null,

	@JvmField @SerializedName("TravellingModeIds")
	val travellingModeIds: Any? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("TotalCount")
	val totalCount: Any? = null,

	@JvmField @SerializedName("MeetingTypeR")
	val meetingTypeR: Any? = null,

	@JvmField @SerializedName("LookUpid")
	val lookUpid: Any? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("CustomerRemark")
	val customerRemark: Any? = null,

	@JvmField @SerializedName("MeetingDuration")
	val meetingDuration: List<Any?>? = null,

	@JvmField @SerializedName("ParentID")
	val parentID: Any? = null,

	@JvmField @SerializedName("Products")
	val products: List<Any?>? = null,

	@JvmField @SerializedName("LadMeetingType")
	val ladMeetingType: List<Any?>? = null,

	@JvmField @SerializedName("GeoLongitude")
	val geoLongitude: Any? = null,

	@JvmField @SerializedName("SubTopics")
	val subTopics: Any? = null,

	@JvmField @SerializedName("SubCategoryID")
	val subCategoryID: Int? = null,

	@JvmField @SerializedName("NextMeetingDate")
	val nextMeetingDate: Any? = null,

	@JvmField @SerializedName("MeetingObjectiveID")
	val meetingObjectiveID: Int? = null,

	@JvmField @SerializedName("IsSelected")
	val isSelected: Boolean? = null,

	@JvmField @SerializedName("Mandays")
	val mandays: Any? = null,

	@JvmField @SerializedName("ProspectName")
	val prospectName: Any? = null,

	@JvmField @SerializedName("Permissions")
	val permissions: Any? = null,

	@JvmField @SerializedName("SelectedMeetingReason")
	val selectedMeetingReason: String? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@JvmField @SerializedName("CategoryID")
	val categoryID: Int? = null,

	@JvmField @SerializedName("SubActivityType")
	val subActivityType: List<Any?>? = null,

	@JvmField @SerializedName("SpAttendee")
	val spAttendee: Any? = null,

	@JvmField @SerializedName("GeoLatitude")
	val geoLatitude: Any? = null,

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

	@JvmField @SerializedName("meetingSubject")
	val meetingSubject: List<Any?>? = null,

	@JvmField @SerializedName("BAClientMeetingID")
	val bAClientMeetingID: Int? = null,

	@JvmField @SerializedName("Flag")
	val flag: Any? = null,

	@JvmField @SerializedName("repeatEveryWeek")
	val repeatEveryWeek: List<Any?>? = null,

	@JvmField @SerializedName("FranchiseeName")
	val franchiseeName: Any? = null,

	@JvmField @SerializedName("repeatationEndsOn")
	val repeatationEndsOn: Any? = null,

	@JvmField @SerializedName("DeviceType")
	val deviceType: Any? = null,

	@JvmField @SerializedName("MassTraining")
	val massTraining: Any? = null,

	@JvmField @SerializedName("TrainingTypes")
	val trainingTypes: Any? = null,

	@JvmField @SerializedName("TopicsId")
	val topicsId: Any? = null,

	@JvmField @SerializedName("MeetingSubjectID")
	val meetingSubjectID: Int? = null,

	@JvmField @SerializedName("ProductCategory")
	val productCategory: List<Any?>? = null,

	@JvmField @SerializedName("IFAName")
	val iFAName: Any? = null,

	@JvmField @SerializedName("TopicsType")
	val topicsType: List<Any?>? = null,

	@JvmField @SerializedName("Pincode")
	val pincode: Any? = null,

	@JvmField @SerializedName("BranchCode")
	val branchCode: Any? = null,

	@JvmField @SerializedName("Category")
	val category: List<Any?>? = null,

	@JvmField @SerializedName("ActivityTypeName")
	val activityTypeName: Any? = null,

	@JvmField @SerializedName("ViewFlag")
	val viewFlag: String? = null,

	@JvmField @SerializedName("ProspectMobile")
	val prospectMobile: Any? = null,

	@JvmField @SerializedName("PID_txn_meetingProduct")
	val pIDTxnMeetingProduct: Int? = null,

	@JvmField @SerializedName("LookupTypeName")
	val lookupTypeName: Any? = null,

	@JvmField @SerializedName("MeetingDate")
	val meetingDate: String? = null,

	@JvmField @SerializedName("Clientcode")
	val clientcode: Any? = null,

	@JvmField @SerializedName("ProductCategoryID")
	val productCategoryID: Int? = null,

	@JvmField @SerializedName("LookUpName")
	val lookUpName: Any? = null,

	@JvmField @SerializedName("SplAttendeeCode")
	val splAttendeeCode: String? = null,

	@JvmField @SerializedName("LeadId")
	val leadId: Int? = null,

	@JvmField @SerializedName("MeetingDescription")
	val meetingDescription: Any? = null,

	@JvmField @SerializedName("repeatEveryDay")
	val repeatEveryDay: List<Any?>? = null,

	@JvmField @SerializedName("MeetingAddressType")
	val meetingAddressType: List<Any?>? = null,

	@JvmField @SerializedName("lstMandays")
	val lstMandays: List<Any?>? = null,

	@JvmField @SerializedName("IFAMeeting")
	val iFAMeeting: List<Any?>? = null,

	@JvmField @SerializedName("TrainingTypeName")
	val trainingTypeName: Any? = null,

	@JvmField @SerializedName("Mode")
	val mode: Any? = null,

	@JvmField @SerializedName("ActivityType")
	val activityType: List<Any?>? = null,

	@JvmField @SerializedName("TrainingType")
	val trainingType: List<Any?>? = null,

	@JvmField @SerializedName("MeetingAddressTypeId")
	val meetingAddressTypeId: Int? = null,

	@JvmField @SerializedName("SelectedCount")
	val selectedCount: Any? = null,

	@JvmField @SerializedName("SelectedMeetingSubject")
	val selectedMeetingSubject: Any? = null,

	@JvmField @SerializedName("SubCategory")
	val subCategory: List<Any?>? = null,

	@JvmField @SerializedName("MeetingObjective")
	val meetingObjective: List<Any?>? = null,

	@JvmField @SerializedName("AddressTypeName")
	val addressTypeName: Any? = null,

	@JvmField @SerializedName("SubActivityTypeID")
	val subActivityTypeID: Any? = null,

	@JvmField @SerializedName("hdnMeetingID")
	val hdnMeetingID: Any? = null,

	@JvmField @SerializedName("IsPotentialPMSProduct")
	val isPotentialPMSProduct: Any? = null,

	@JvmField @SerializedName("NineToElevenMeet")
	val nineToElevenMeet: Any? = null,

	@JvmField @SerializedName("LeadName")
	val leadName: Any? = null,

	@JvmField @SerializedName("repeatOnCode")
	val repeatOnCode: Any? = null
)

data class MeetingAttendeeItem(

	@JvmField @SerializedName("AttendeeType")
	val attendeeType: String? = null,

	@JvmField @SerializedName("Status")
	val status: Int? = null,

	@JvmField @SerializedName("InApproval")
	val inApproval: Boolean? = null,

	@JvmField @SerializedName("MeetingAttendeeID")
	val meetingAttendeeID: Int? = null,

	@JvmField @SerializedName("AttendeeEmpcode")
	val attendeeEmpcode: String? = null,

	@JvmField @SerializedName("ApprovalFieldList")
	val approvalFieldList: Any? = null,

	@JvmField @SerializedName("MeetingID")
	val meetingID: Int? = null,

	@JvmField @SerializedName("Permissions")
	val permissions: Any? = null,

	@JvmField @SerializedName("IsAdminMode")
	val isAdminMode: Boolean? = null,

	@JvmField @SerializedName("AttendeeEmpName")
	val attendeeEmpName: Any? = null,

	@JvmField @SerializedName("Attended")
	val attended: Boolean? = null,

	@JvmField @SerializedName("DefaultAttendee")
	val defaultAttendee: Boolean? = null
)
