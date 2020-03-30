package com.mosl.app.core.data.lead.reports

import com.mosl.mobile.domain.lead.report.LeadsReportInput
import com.mosl.mobile.domain.lead.report.LeadsReportModel
import com.mosl.saathi.api.model.dashboard.lead.report.LeadReportResponse
import com.mosl.saathi.api.model.dashboard.lead.report.LeadsReportRequest

fun LeadsReportInput.requestTransform(): LeadsReportRequest {
  return LeadsReportRequest(
    businessProfileId = businessProfileId,
    addtionalParameters = addtionalParameters,
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName

  )
}

fun LeadReportResponse.responseTransform(): LeadsReportModel {
  return LeadsReportModel(
    table = table,
    table2 = table2
  )
}

