package com.mosl.app.core.data.report

import com.mosl.app.core.data.util.defaultSelection
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportInput
import com.mosl.mobile.domain.report.SelectBranch
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.api.model.reports.report.LeadReportRequest

fun HierarchyReportInput.requestTransform(): LeadReportRequest {
  return LeadReportRequest(
    inputTotalCount = inputTotalCount,
    totalCount = totalCount,
    isExportToExcel = isExportToExcel,
    periodFrom = periodFrom,
    employeeCode = employeeCode,
    pageSize = pageSize,
    calculatedPage = calculatedPage,
    page = page,
    reportTypeSystemName = reportTypeSystemName,
    businessChannel = businessChannel,
    periodTo = periodTo,
    branchCode = branchCode

  )

}

fun List<AssignedLeadReportItem>.requestTransform(): List<AssignedLeadReportModel> {
  return map {
    AssignedLeadReportModel(
      businessUnitId = it.businessUnitId.toInt(),
      branchCode = it.branchCode

    )
  }.orEmpty()
    .defaultSelection(SelectBranch)

}