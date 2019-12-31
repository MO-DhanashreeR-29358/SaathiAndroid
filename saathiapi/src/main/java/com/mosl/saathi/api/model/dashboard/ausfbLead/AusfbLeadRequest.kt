package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class AusfbLeadRequest(
    @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
    @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: AdditionalParametersRequest,
    @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
    @JvmField @SerializedName("ProductId") val productId: String?,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)