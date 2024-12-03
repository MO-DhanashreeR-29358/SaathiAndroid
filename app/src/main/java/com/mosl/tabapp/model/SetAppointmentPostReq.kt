package com.mosl.tabapp.model

import com.google.gson.annotations.SerializedName

data class SetAppointmentPostReq(

	@SerializedName("product")
	var product: String? = null,

	@SerializedName("meetingAttendee")
	var meetingAttendee: MutableList<MeetingAttendeeItem?>? = null,

	@SerializedName("MEETING_TYPE_MEETING")
	var mEETINGTYPEMEETING: String? = null,

	@SerializedName("meetingDurationID")
	var meetingDurationID: Int? = null,

	@SerializedName("splMeetingAttendee")
	var splMeetingAttendee: List<SplMeetingAttendeeItem?>? = null,

	@SerializedName("meetingType")
	var meetingType: String? = null,

	@SerializedName("MeetingDescription")
	var meetingDes: String? = null,

	@SerializedName("MEETING_TYPE_APPT")
	var mEETINGTYPEAPPT: String? = null,

	@SerializedName("displayType")
	var displayType: String? = null,

	@SerializedName("DISPLAY_TYPE_MEETING")
	var dISPLAYTYPEMEETING: String? = null,

	@SerializedName("meetingObjectiveID")
	var meetingObjectiveID: String? = null,

	@SerializedName("meetingDate")
	var meetingDate: String? = null,

	@SerializedName("meetingReasonID")
	var meetingReasonID: Int? = null,

	@SerializedName("activityTypeID")
	var activityTypeID: String? = null,

	@SerializedName("leadId")
	var leadId: String? = null,

	@SerializedName("ucid")
	var ucid: String? = null
)

data class MeetingAttendeeItem(

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

data class SplMeetingAttendeeItem(

	@SerializedName("attended")
	var attended: Boolean? = null,

	@SerializedName("attendeeName")
	var attendeeName: String? = null,

	@SerializedName("meetingAttendeeCode")
	var meetingAttendeeCode: String? = null,

	@SerializedName("attendeeEmplyeeCode")
	var attendeeEmplyeeCode: String? = null,

	@SerializedName("attendeeType")
	var attendeeType: String? = null
)
