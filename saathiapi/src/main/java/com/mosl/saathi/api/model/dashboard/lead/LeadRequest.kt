package com.mosl.saathi.api.model.dashboard.lead

import com.google.gson.annotations.SerializedName

data class LeadRequest(
    @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
    @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: AdditionalParametersRequest,
    @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
    @JvmField @SerializedName("ProductId") val productId: String?,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)