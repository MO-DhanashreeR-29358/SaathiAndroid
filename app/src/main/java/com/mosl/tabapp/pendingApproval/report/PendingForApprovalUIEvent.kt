package com.mosl.tabapp.pendingApproval.report

sealed class PendingForApprovalUIEvent {

  data class DatePickerEvent(
    @JvmField val startDate: String,
    @JvmField val endDate: String
  ) :
    PendingForApprovalUIEvent()
}