package com.mosl.saathi.api.model.dashboard.targetnew

import com.google.gson.annotations.SerializedName

data class AuTargetRequest(
    @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
    @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: APTargetRequest,
    @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
    @JvmField @SerializedName("ProductId") val productId: String?,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)