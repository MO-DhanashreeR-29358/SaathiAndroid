package com.mosl.app.core.data.pendingapproval

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.pendingapproval.ApprovalSummaryByEmployeeItemModel
import com.mosl.mobile.domain.pendingapproval.ApprovalSummaryCountItemModel
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryByEmployeeItem
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryCountItem

fun ApprovalSummaryCountItem.mapToSummaryCount(): ApprovalSummaryCountItemModel {
  return ApprovalSummaryCountItemModel(
    pendingCount = pendingCount ?: 0,
    approvedCount = approvedCount ?: 0
  )

}

fun List<ApprovalSummaryByEmployeeItem>.mapToListOfPendingApproval(): List<ApprovalSummaryByEmployeeItemModel> {
  return safeMap {
    it.mapToApproval()
  }
}

private fun ApprovalSummaryByEmployeeItem.mapToApproval(): ApprovalSummaryByEmployeeItemModel {
  return ApprovalSummaryByEmployeeItemModel(
    pendingCount = pendingCount ?: "0",
    approvedCount = approvedCount ?: "0",
    entityField = entityField ?: ""
  )
}

