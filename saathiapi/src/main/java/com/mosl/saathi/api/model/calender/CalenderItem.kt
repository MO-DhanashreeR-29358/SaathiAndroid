package com.mosl.saathi.api.model.calender

import com.google.gson.annotations.SerializedName

data class CalenderItem(

  @JvmField @SerializedName("ParentID")
  val parentID: Any,

  @JvmField @SerializedName("Products")
  val products: List<Any>,

  @JvmField @SerializedName("Product")
  val product: String?,

  @JvmField @SerializedName("MeetingReason")
  val meetingReason: String?,

  @JvmField @SerializedName("MeetingReasonID")
  val meetingReasonID: Int,

  @JvmField @SerializedName("GeoLongitude")
  val geoLongitude: Any,

  @JvmField @SerializedName("SubCategoryID")
  val subCategoryID: Int,

  @JvmField @SerializedName("GeoTagStatus")
  val geoTagStatus: Any,

  @JvmField @SerializedName("MeetingObjectiveID")
  val meetingObjectiveID: Int,

  @JvmField @SerializedName("UCID")
  val uCID: Int,

  @JvmField @SerializedName("Name")
  val name: String?,

  @JvmField @SerializedName("EMAIL")
  val email: String?,

  @JvmField @SerializedName("MOBNO")
  val mobNo: String?,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean,

  @JvmField @SerializedName("CategoryID")
  val categoryID: Int,

  @JvmField @SerializedName("repeatEveryDayCode")
  val repeatEveryDayCode: Any,

  @JvmField @SerializedName("Status")
  val status: String?,

  @JvmField @SerializedName("StatusName")
  val statusName: String?,

  @JvmField @SerializedName("repeatationStartOn")
  val repeatationStartOn: String,

  @JvmField @SerializedName("SubCategoryDescription")
  val subCategoryDescription: Any,

  @JvmField @SerializedName("CreatedBy")
  val createdBy: String,

  @JvmField @SerializedName("meetingAttendee")
  val meetingAttendee: List<Any>,

  @JvmField @SerializedName("GeoLatitude")
  val geoLatitude: Any,

  @JvmField @SerializedName("ActivityTypeID")
  val activityTypeID: Int,

  @JvmField @SerializedName("BusinessUnitId")
  val businessUnitId: Int,

  @JvmField @SerializedName("OutstationMeetingID")
  val outstationMeetingID: Int,

  @JvmField @SerializedName("BAClientMeetingID")
  val bAClientMeetingID: Int,

  @JvmField @SerializedName("MeetingDurationID")
  val meetingDurationID: Int,

  @JvmField @SerializedName("repeatationEndsOn")
  val repeatationEndsOn: String,

  @JvmField @SerializedName("GeoArea")
  val geoArea: Any,

  @JvmField @SerializedName("DeviceType")
  val deviceType: Any,

  @JvmField @SerializedName("MeetingType")
  val meetingType: String,

  @JvmField @SerializedName("CategoryDescription")
  val categoryDescription: Any,

  @JvmField @SerializedName("FinalAction")
  val finalAction: String,

  @JvmField @SerializedName("Pincode")
  val pincode: Any,

  @JvmField @SerializedName("repeatEveryWeekCode")
  val repeatEveryWeekCode: Any,

  @JvmField @SerializedName("MeetingWith")
  val meetingWith: String,

  @JvmField @SerializedName("AttendeeEmp")
  val attendeeEmp: String?,

  @JvmField @SerializedName("AttendeeName")
  val attendeeName: String,

  @JvmField @SerializedName("ProductTypeID")
  val productTypeID: Int,

  @JvmField @SerializedName("PID_txn_meetingProduct")
  val pIDTxnMeetingProduct: Int,

  @JvmField @SerializedName("MeetingDate")
  val meetingDate: String?,

  @JvmField @SerializedName("Clientcode")
  val clientcode: Any,

  @JvmField @SerializedName("ProductCategoryID")
  val productCategoryID: Int,

  @JvmField @SerializedName("MeetingAddress")
  val meetingAddress: Any,

  @JvmField @SerializedName("MeetingID")
  val meetingID: String?,

  @JvmField @SerializedName("MeetingDuration")
  val meetingDuration: String?,

  @JvmField @SerializedName("ActionableID")
  val actionableID: Int,

  @JvmField @SerializedName("Actionable")
  val actionable: String?,

  @JvmField @SerializedName("LeadId")
  val leadId: String?,

  @JvmField @SerializedName("Comments")
  val comments: String?,

  @JvmField @SerializedName("Stage")
  val stage: String?,

  @JvmField @SerializedName("QueryDesc")
  val queryDesc: String?,

  @JvmField @SerializedName("DATE_DISPLAY")
  val dateDisplay: String?,

  @JvmField @SerializedName("CustomerServiceRemark")
  val customerServiceRemark: String?,

  @JvmField @SerializedName("Category")
  val category: String?,

  @JvmField @SerializedName("MeetingDescription")
  val meetingDescription: Any,

  @JvmField @SerializedName("AttendeeCode")
  val attendeeCode: Any,

  @JvmField @SerializedName("IsSubcategoryData")
  val isSubcategoryData: Int,

  @JvmField @SerializedName("Clientname")
  val clientname: Any,

  @JvmField @SerializedName("Amount")
  val amount: Any,

  @JvmField @SerializedName("TravellingModeIds")
  val travellingModeIds: Any,

  @JvmField @SerializedName("MeetingAddressTypeId")
  val meetingAddressTypeId: Int,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any,

  @JvmField @SerializedName("ActivityType")
  val activityType: String?,

  @JvmField @SerializedName("CustomerRemark")
  val customerRemark: Any,

  @JvmField @SerializedName("IsPotentialPMSProduct")
  val isPotentialPMSProduct: Any,

  @JvmField @SerializedName("repeatOnCode")
  val repeatOnCode: Any
)