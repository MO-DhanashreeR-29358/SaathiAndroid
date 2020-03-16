package com.mosl.mobile.domain.calendar

data class LeadActivitiesAppointData(
  @JvmField val status: String,
  @JvmField val meetingId: String,
  @JvmField val leadId: String,
  @JvmField val name: String,
  @JvmField val email: String,
  @JvmField val mobNo: String,
  @JvmField val date: String,
  @JvmField val time: String,
  @JvmField val attendeeEmp: String,
  @JvmField val product: String,
  @JvmField val statusName: String,
  @JvmField val activityType: String,
  @JvmField val meetingReason: String,
  @JvmField val meetingDuration: String,
  @JvmField val category: String,
  @JvmField val actionable: String,
  @JvmField val comments: String,
  @JvmField val stage: String,
  @JvmField val callback: String,
  @JvmField val queryDesc: String,
  @JvmField val ucId: String,
  @JvmField val createdBy: String,
  @JvmField val repeatOnCode: String
)