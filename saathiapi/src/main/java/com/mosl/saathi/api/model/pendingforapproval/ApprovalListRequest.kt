package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovalListRequest(

  @JvmField @SerializedName("UpdatedBy")
  val updatedBy: String,

  @JvmField @SerializedName("TotalCount")
  val totalCount: String,

  @JvmField @SerializedName("PageSize")
  val pageSize: String,

  @JvmField @SerializedName("EmployeeCode")
  val employeeCode: String,

  @JvmField @SerializedName("CalculatedPage")
  val calculatedPage: String,

  @JvmField @SerializedName("UpdatedStartDate")
  val updatedStartDate: String,

  @JvmField @SerializedName("UpdatedEndDate")
  val updatedEndDate: String,

  @JvmField @SerializedName("EntityField")
  val entityField: String
)