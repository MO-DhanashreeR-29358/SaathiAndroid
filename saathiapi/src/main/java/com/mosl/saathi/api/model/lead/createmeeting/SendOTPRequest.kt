package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(
    @JvmField @SerializedName("LeadId") val leadId: String,
    @JvmField @SerializedName("UCID") val ucid: String,
    @JvmField @SerializedName("GeoArea") val geoArea: String,
    @JvmField @SerializedName("GeoLongitude") val longitude: String,
    @JvmField @SerializedName("GeoLatitude") val latitude: String,
    @JvmField @SerializedName("DeviceType") val deviceType: String,
    @JvmField @SerializedName("SendFlag") val sendFlag: Int,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String

)