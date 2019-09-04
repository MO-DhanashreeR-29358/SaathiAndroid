package com.mosl.saathi.api.model.reports.report

import com.google.gson.annotations.SerializedName

data class LeadReportRequest(

  @SerializedName("TotalCount")
  @JvmField val totalCount: Int = 0,

  @SerializedName("Period_From")
  @JvmField val periodFrom: String,

  @SerializedName("IsExportToExcel")
  @JvmField val isExportToExcel: Boolean,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("PageSize")
  @JvmField val pageSize: String,

  @SerializedName("CalculatedPage")
  @JvmField val calculatedPage: Int = 0,

  @SerializedName("InputTotalCount")
  @JvmField val inputTotalCount: Int = 0,

  @SerializedName("Page")
  @JvmField val page: Int = 0,

  @SerializedName("ReportTypeSystemName")
  @JvmField val reportTypeSystemName: String,

  @SerializedName("BusinessChannel")
  @JvmField val businessChannel: String = "-1",

  @SerializedName("Period_To")
  @JvmField val periodTo: String,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String
)