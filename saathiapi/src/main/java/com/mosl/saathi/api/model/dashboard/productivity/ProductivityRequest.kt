package com.mosl.saathi.api.model.dashboard.productivity

import com.google.gson.annotations.SerializedName

data class ProductivityRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("AddtionalParameters")
  val addtionalParameters: ProductivityAdditionalParameterRequest,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)

data class ProductivityAdditionalParameterRequest(
  @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String?
)