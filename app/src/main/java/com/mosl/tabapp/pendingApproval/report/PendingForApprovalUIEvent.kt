package com.mosl.tabapp.pendingApproval.report

sealed class PendingForApprovalUIEvent {

  data class BtnEvent(
    @JvmField val status: String
  ) :
    PendingForApprovalUIEvent()

  data class CheckBoxSelectedArrayList(
    @JvmField val selectedArrayList: ArrayList<String>
  ) :
    PendingForApprovalUIEvent()
}