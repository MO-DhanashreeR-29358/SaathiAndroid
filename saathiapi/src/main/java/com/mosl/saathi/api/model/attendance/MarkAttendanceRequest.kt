package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class MarkAttendanceRequest(
  @JvmField @SerializedName("Latitude") val latitude: Double,
  @JvmField @SerializedName("Longitude") val longitude: Double,
  @JvmField @SerializedName("GeoArea") val geoArea: String,
  @JvmField @SerializedName("MoodId") val moodId: Int
)