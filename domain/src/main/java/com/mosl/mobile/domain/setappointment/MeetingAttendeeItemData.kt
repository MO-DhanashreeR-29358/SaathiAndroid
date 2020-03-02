package com.mosl.mobile.domain.setappointment

data class MeetingAttendeeItemData(

  @JvmField val attendeeType: String?,

  @JvmField val status: Int,

  @JvmField val inApproval: Boolean,

  @JvmField val meetingAttendeeID: String?,

  @JvmField val attendeeEmpcode: String?,

  @JvmField val approvalFieldList: Any,

  @JvmField val meetingID: Int,

  @JvmField val isAdminMode: Boolean,

  @JvmField val attendeeEmpName: String?,

  @JvmField val attended: Boolean,

  @JvmField val defaultAttendee: Boolean
)