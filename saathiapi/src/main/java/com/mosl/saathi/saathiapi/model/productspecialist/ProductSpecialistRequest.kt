package com.mosl.saathi.saathiapi.model.productspecialist

import com.google.gson.annotations.SerializedName

data class ProductSpecialistRequest(

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: String,

  @SerializedName("EmployeeName")
  @JvmField val employeeName: String,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: String
)