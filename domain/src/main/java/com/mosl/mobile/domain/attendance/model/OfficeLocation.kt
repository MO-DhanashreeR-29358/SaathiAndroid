package com.mosl.mobile.domain.attendance.model

import com.mosl.mobile.domain.attendance.LatLng


data class OfficeLocation(
  @JvmField val latitude: Double,
  @JvmField val longitude: Double,
  @JvmField val id: Int,
  @JvmField val locationName: String
)

data class UserLocationInfo(
  @JvmField val distance: Double,
  @JvmField val officeLocation: OfficeLocation,
  @JvmField val userLocation: LatLng
)