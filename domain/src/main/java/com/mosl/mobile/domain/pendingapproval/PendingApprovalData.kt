package com.mosl.mobile.domain.pendingapproval

data class PendingApprovalData(
  @JvmField val summaryCount: ApprovalSummaryCountItemModel,
  @JvmField val listOfApprovals: List<ApprovalSummaryByEmployeeItemModel>
)

class ApprovalSummaryByEmployeeItemModel(
  @JvmField val pendingCount: String,

  @JvmField val approvedCount: String,

  @JvmField val entityField: String
)

data class ApprovalSummaryCountItemModel(

  @JvmField val pendingCount: Int,

  @JvmField val approvedCount: Int
)

