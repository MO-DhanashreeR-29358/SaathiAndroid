package com.mosl.saathi.api.model.location

import com.google.gson.annotations.SerializedName

data class LocationDataItemModel(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
  @JvmField @SerializedName("GeoArea") val geoArea: String,
  @JvmField @SerializedName("GeoLongitude") val geoLongitude: String,
  @JvmField @SerializedName("GeoLatitude") val geoLatitude: String,
  @JvmField @SerializedName("Time") val time: String,
  @JvmField @SerializedName("DeviceId") val deviceId: String,
  @JvmField @SerializedName("Accuracy") val accuracy: String,
  @JvmField @SerializedName("GeoPinCode") val geoPinCode: String,
  @JvmField @SerializedName("Id") val id: String

)