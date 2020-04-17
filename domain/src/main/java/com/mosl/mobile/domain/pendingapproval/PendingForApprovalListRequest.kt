package com.mosl.mobile.domain.pendingapproval

data class PendingForApprovalListRequest(

  val updatedBy: String,

  val totalCount: String,

  val pageSize: String,

  val employeeCode: String?,

  val calculatedPage: String,

  val updatedStartDate: String,

  val updatedEndDate: String,

  val entityField: String
)