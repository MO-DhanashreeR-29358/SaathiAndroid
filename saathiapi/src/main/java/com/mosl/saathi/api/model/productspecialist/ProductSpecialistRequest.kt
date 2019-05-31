package com.mosl.saathi.api.model.productspecialist

import com.google.gson.annotations.SerializedName

data class ProductSpecialistRequest(

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("EmployeeName")
  @JvmField val employeeName: String,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: String
)