package com.mosl.tabapp.pendingApproval

sealed class PendingApprovalUiEvent {
  data class ItemClick(@JvmField val entityField: String) : PendingApprovalUiEvent()
}