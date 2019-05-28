package com.mosl.saathi.saathiapi.model.reports.report

import com.google.gson.annotations.SerializedName

data class LeadReportRequest(

  @SerializedName("TotalCount")
  @JvmField val totalCount: String,

  @SerializedName("Period_From")
  @JvmField val periodFrom: String,

  @SerializedName("IsExportToExcel")
  @JvmField val isExportToExcel: Boolean,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("PageSize")
  @JvmField val pageSize: String,

  @SerializedName("CalculatedPage")
  @JvmField val calculatedPage: String,

  @SerializedName("InputTotalCount")
  @JvmField val inputTotalCount: Int,

  @SerializedName("Page")
  @JvmField val page: String,

  @SerializedName("ReportTypeSystemName")
  @JvmField val reportTypeSystemName: String,

  @SerializedName("BusinessChannel")
  @JvmField val businessChannel: String,

  @SerializedName("Period_To")
  @JvmField val periodTo: String,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String
)