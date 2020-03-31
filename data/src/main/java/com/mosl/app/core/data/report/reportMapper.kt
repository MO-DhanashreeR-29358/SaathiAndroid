package com.mosl.app.core.data.report

import com.mosl.mobile.domain.report.HierarchyReportInput
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