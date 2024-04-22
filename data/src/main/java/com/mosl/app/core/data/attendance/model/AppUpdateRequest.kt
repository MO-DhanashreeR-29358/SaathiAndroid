package com.mosl.data.attendance.model

import com.google.gson.annotations.SerializedName

data class AppUpdateRequest (
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("DeviceType") val appMarket: String = "AndroidMarket",
    @JvmField @SerializedName("DeviceModelType") val deviceModel: String,
    @JvmField @SerializedName("MyzoneVersion") val appVersion: String
        )