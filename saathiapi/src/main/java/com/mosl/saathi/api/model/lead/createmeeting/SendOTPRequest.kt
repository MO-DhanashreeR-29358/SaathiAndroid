package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(
    @JvmField @SerializedName("LeadId") val leadId: String,
    @JvmField @SerializedName("UCID") val ucid: String,
    @JvmField @SerializedName("GeoArea") val geoArea: String,
    @JvmField @SerializedName("GeoLongitude") val longitude: String,
    @JvmField @SerializedName("GeoLatitude") val latitude: String,
    @JvmField @SerializedName("DeviceType") val deviceType: String = "2",
    @JvmField @SerializedName("SendFlag") val sendFlag: Int = 0,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String

)