package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class MeetingByIdItem(

  @JvmField @SerializedName("ParentID") val parentID: Any,

  @JvmField @SerializedName("Products") val products: List<Any?>,

  @JvmField @SerializedName("MeetingReasonID") val meetingReasonID: String?,

  @JvmField @SerializedName("GeoLongitude") val geoLongitude: Any,

  @JvmField @SerializedName("SubCategoryID") val subCategoryID: String?,

  @JvmField @SerializedName("GeoTagStatus") val geoTagStatus: Any,

  @JvmField @SerializedName("MeetingObjectiveID") val meetingObjectiveID: Int,

  @JvmField @SerializedName("UCID") val uCID: Int,

  @JvmField @SerializedName("Name") val name: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("CategoryID") val categoryID: String?,

  @JvmField @SerializedName("repeatEveryDayCode") val repeatEveryDayCode: String?,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("repeatationStartOn") val repeatationStartOn: String?,

  @JvmField @SerializedName("SubCategoryDescription") val subCategoryDescription: String?,

  @JvmField @SerializedName("CreatedBy") val createdBy: String,

  @JvmField @SerializedName("meetingAttendee") val meetingAttendee: List<MeetingAttendeeItem>,

  @JvmField @SerializedName("GeoLatitude") val geoLatitude: Any,

  @JvmField @SerializedName("ActivityTypeID") val activityTypeID: String?,

  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,

  @JvmField @SerializedName("OutstationMeetingID") val outstationMeetingID: Int,

  @JvmField @SerializedName("BAClientMeetingID") val bAClientMeetingID: Int,

  @JvmField @SerializedName("MeetingDurationID") val meetingDurationID: String?,

  @JvmField @SerializedName("repeatationEndsOn") val repeatationEndsOn: String?,

  @JvmField @SerializedName("GeoArea") val geoArea: Any,

  @JvmField @SerializedName("DeviceType") val deviceType: Any,

  @JvmField @SerializedName("MeetingType") val meetingType: String?,

  @JvmField @SerializedName("CategoryDescription") val categoryDescription: String?,

  @JvmField @SerializedName("FinalAction") val finalAction: String,

  @JvmField @SerializedName("Pincode") val pincode: Any,

  @JvmField @SerializedName("repeatEveryWeekCode") val repeatEveryWeekCode: String?,

  @JvmField @SerializedName("MeetingWith") val meetingWith: String,

  @JvmField @SerializedName("AttendeeName") val attendeeName: String?,

  @JvmField @SerializedName("ProductTypeID") val productTypeID: String?,

  @JvmField @SerializedName("PID_txn_meetingProduct") val pIDTxnMeetingProduct: Int,

  @JvmField @SerializedName("MeetingDate") val meetingDate: String?,

  @JvmField @SerializedName("Clientcode") val clientcode: Any,

  @JvmField @SerializedName("ProductCategoryID") val productCategoryID: Int,

  @JvmField @SerializedName("MeetingAddress") val meetingAddress: Any,

  @JvmField @SerializedName("MeetingID") val meetingID: String?,

  @JvmField @SerializedName("ActionableID") val actionableID: String?,

  @JvmField @SerializedName("LeadId") val leadId: Int,

  @JvmField @SerializedName("MeetingDescription") val meetingDescription: String?,

  @JvmField @SerializedName("AttendeeCode") val attendeeCode: Any,

  @JvmField @SerializedName("IsSubcategoryData") val isSubcategoryData: Int,

  @JvmField @SerializedName("Clientname") val clientname: Any,

  @JvmField @SerializedName("Amount") val amount: Any,

  @JvmField @SerializedName("TravellingModeIds") val travellingModeIds: Any,

  @JvmField @SerializedName("MeetingAddressTypeId") val meetingAddressTypeId: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("CustomerRemark") val customerRemark: String?,

  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: Any,

  @JvmField @SerializedName("repeatOnCode") val repeatOnCode: String?
)