package com.mosl.tabapp.pendingApproval.report

sealed class PendingForApprovalUIEvent {

  data class CheckBoxEvent(
    @JvmField val checkBoxStatus: Boolean
  ) :
    PendingForApprovalUIEvent()

  data class CheckBoxSelectedArrayList(
    @JvmField val selectedArrayList: ArrayList<String>
  ) :
    PendingForApprovalUIEvent()
}