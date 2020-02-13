package com.mosl.mobile.domain.leadviewdetail.activities

data class ActivityData(
  @JvmField val listOfAppointment: List<Appointment>,
  @JvmField val listOfMeeting: List<Meeting>
)

data class ActivityRequest(
  @JvmField val leadId: String,
  @JvmField val ucid: String
)

data class Appointment(
  @JvmField val mobileNumber: String,
  @JvmField val product: String,
  @JvmField val email: String,
  @JvmField val name: String,
  @JvmField val attendeeEmployee: String,
  @JvmField val meetingId: String,
  @JvmField val status: String,
  @JvmField val createdBy: String,
  @JvmField val activityType: String,
  @JvmField val time: String,
  @JvmField val statusName: String,
  @JvmField val date: String,
  @JvmField val leadId: String,
  @JvmField val repeatOnCode: String,
  @JvmField val ucid: String
)

data class Meeting(
  @JvmField val mobileNumber: String,
  @JvmField val category: String,
  @JvmField val actionable: String,
  @JvmField val product: String,
  @JvmField val email: String,
  @JvmField val name: String,
  @JvmField val attendeeEmployee: String,
  @JvmField val travellingMode: String,
  @JvmField val meetingAddress: String,
  @JvmField val meetingId: String,
  @JvmField val stage: String,
  @JvmField val meetingAddressType: String,
  @JvmField val customerServiceRemark: String,
  @JvmField val meetingReason: String,
  @JvmField val status: String,
  @JvmField val queryDescription: String,
  @JvmField val comment: String,
  @JvmField val amount: String,
  @JvmField val activityType: String,
  @JvmField val time: String,
  @JvmField val statusName: String,
  @JvmField val date: String,
  @JvmField val leadId: String,
  @JvmField val meetingDuration: String,
  @JvmField val pincode: String,
  @JvmField val location: String
)