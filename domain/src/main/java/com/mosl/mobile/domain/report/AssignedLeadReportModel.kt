package com.mosl.mobile.domain.report

data class AssignedLeadReportModel(
  @JvmField val businessUnitId: Int,
  @JvmField val branchCode: String
) {
  override fun toString(): String {
    return branchCode
  }
}