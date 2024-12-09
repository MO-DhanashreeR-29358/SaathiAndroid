package com.mosl.tabapp.model

import com.google.gson.annotations.SerializedName

data class CreateMeetingPostReq(

	@SerializedName("meetingLatitude")
	var meetingLatitude: String? = null,

	@SerializedName("geoArea")
	var geoArea: String? = null,

	@SerializedName("MEETING_TYPE_MEETING")
	var mEETINGTYPEMEETING: String? = null,

	@SerializedName("meetingType")
	var meetingType: String? = null,

	@SerializedName("meetingDuration")
	var meetingDuration: String? = null,

	@SerializedName("TravellingModeIds")
	var travellingList: String? = null,

	@SerializedName("meetingDate")
	var meetingDate: String? = null,

	@SerializedName("MeetingDescription")
	var meetingDescription: String? = null,

	@SerializedName("leadId")
	var leadId: String? = null,

	@SerializedName("meetingId")
	var meetingId: String? = null,

	@SerializedName("meetingReason")
	var meetingReason: String? = null,

	@SerializedName("ucid")
	var ucid: String? = null,

	@SerializedName("pincode")
	var pincode: String? = null,

	@SerializedName("amount")
	var amount: String? = null,

	@SerializedName("product")
	var product: String? = null,

	@SerializedName("meetingAttendee")
	var meetingAttendee: MutableList<MeetingAttendeeItems?>? = null,

	@SerializedName("meetingLongitude")
	var meetingLongitude: String? = null,

	@SerializedName("MeetingDurationID")
	var meetingDurationID: Int? = null,

	@SerializedName("geoLongitude")
	var geoLongitude: String? = null,

	@SerializedName("MEETING_TYPE_APPT")
	var mEETINGTYPEAPPT: String? = null,

	@SerializedName("meetingAddress")
	var meetingAddress: String? = null,

	@SerializedName("displayType")
	var displayType: String? = null,

	@SerializedName("geoLatitude")
	var geoLatitude: String? = null,

	@SerializedName("DISPLAY_TYPE_MEETING")
	var dISPLAYTYPEMEETING: String? = null,

	@SerializedName("meetingAddressTypeId")
	var meetingAddressTypeId: String? = null,

	@SerializedName("meetingObjectiveID")
	var meetingObjectiveID: String? = null,

	@SerializedName("meetingReasonID")
	var meetingReasonID: Int? = null,

	@SerializedName("activityTypeID")
	var activityTypeID: String? = null,

	@SerializedName("ProductTypeID")
	var productTypeID:String?=null,

	@SerializedName("AttendeeCode")
	var attendeeCode:String?=null
)

data class MeetingAttendeeItems(

	@SerializedName("Attended")
	var attended: Boolean? = null,

	@SerializedName("AttendeeEmpName")
	var attendeeName: String? = null,

	@SerializedName("meetingAttendeeCode")
	var meetingAttendeeCode: String? = null,

	@SerializedName("AttendeeEmpcode")
	var attendeeEmplyeeCode: String? = null,

	@SerializedName("AttendeeType")
	var attendeeType: String? = null
)
