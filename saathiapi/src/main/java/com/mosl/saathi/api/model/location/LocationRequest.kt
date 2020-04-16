package com.mosl.saathi.api.model.location

import com.google.gson.annotations.SerializedName

data class LocationRequest(
  @JvmField @SerializedName("GeoList")
  val locationList: List<LocationDataItemModel>
)