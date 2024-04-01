package com.mosl.mobile.domain.attendance.model

import com.mosl.mobile.domain.attendance.LatLng


data class MarkAttendanceData(
  @JvmField val officeLocation: LatLng,
  @JvmField val branchName: String,
  @JvmField val geoArea: String,
  @JvmField val mood: Int
)