package com.mosl.saathi.api.model.productspecialist

import com.google.gson.annotations.SerializedName

data class ProductSpecialistRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?,
  @JvmField @SerializedName("EmployeeName") val employeeName: String?,
  @JvmField @SerializedName("PrimaryRM") val primaryRM: String?
)