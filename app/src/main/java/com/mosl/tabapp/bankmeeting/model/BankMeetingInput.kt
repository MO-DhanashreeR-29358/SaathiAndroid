package com.mosl.tabapp.bankmeeting.model

data class BankMeetingInput(
  @JvmField val bankMeetingId: String = "",

  @JvmField val meetingDate: String,
  @JvmField val meetingTypeId: String,
  @JvmField val meetingDurationId: String,
  @JvmField val meetingDescription: String,
  @JvmField val bankId: String,
  @JvmField val bankBranchId: String,
  @JvmField val subBrokerId: String,
  @JvmField val subject: String,
  @JvmField val isSubjectVisible: Boolean,
  @JvmField val noOfClient: String = "0",
  @JvmField val clientName: String = "",
  @JvmField val bankEmployeeCode: String
)