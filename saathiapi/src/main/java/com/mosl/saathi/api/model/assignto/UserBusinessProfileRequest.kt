package com.mosl.saathi.api.model.assignto

import com.google.gson.annotations.SerializedName

data class UserBusinessProfileRequest(

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("EmployeeName")
  @JvmField val employeeName: String
)