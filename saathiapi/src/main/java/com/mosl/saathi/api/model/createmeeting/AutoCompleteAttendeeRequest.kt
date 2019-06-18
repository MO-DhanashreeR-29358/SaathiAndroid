package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class AutoCompleteAttendeeRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,

  @JvmField @SerializedName("EmployeeName") val employeeName: String
)