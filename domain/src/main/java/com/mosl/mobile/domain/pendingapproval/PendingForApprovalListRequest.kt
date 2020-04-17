package com.mosl.mobile.domain.pendingapproval

data class PendingForApprovalListRequest(
  @JvmField val updatedBy: String,

  @JvmField val totalCount: String,

  @JvmField val pageSize: String,

  @JvmField val employeeCode: String?,

  @JvmField val calculatedPage: String,

  @JvmField val updatedStartDate: String,

  @JvmField val updatedEndDate: String,

  @JvmField val entityField: String
)