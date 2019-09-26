package com.mosl.tabapp.service.location

import com.mosl.mobile.domain.location.LocationDataModel
import com.mosl.tabapp.database.LocationData

fun List<LocationData>.transformLocationData(): List<LocationDataModel> {
  return mapNotNull {
    try {
      LocationDataModel(
          employeeCode = it.empCode,
          geoArea = it.geoArea,
          geoLongitude = it.longitude,
          geoLatitude = it.latitude,
          time = it.time,
          deviceId = it.deviceId,
          accuracy = it.accuracy,
          geoPinCode = it.geoPincode,
          id = "0"

      )
    } catch (_: Exception) {
      null
    }

  }
}