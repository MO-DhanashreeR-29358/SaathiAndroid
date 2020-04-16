package com.mosl.app.core.data.location

import com.mosl.mobile.domain.location.LocationDataModel
import com.mosl.mobile.domain.location.LocationResponse
import com.mosl.saathi.api.model.location.LocationDataItemModel
import com.mosl.saathi.api.model.location.LocationDataResponse

fun List<LocationDataModel>.transformLocationData(): List<LocationDataItemModel> {
  return mapNotNull {
    try {
      com.mosl.saathi.api.model.location.LocationDataItemModel(
        employeeCode = it.employeeCode,
        geoArea = it.geoArea,
        geoLongitude = it.geoLongitude,
        geoLatitude = it.geoLatitude,
        time = it.time,
        deviceId = it.deviceId,
        accuracy = it.accuracy,
        geoPinCode = it.geoPinCode,
        id = "0"

      )
    } catch (_: Exception) {
      null
    }

  }
}

fun LocationDataResponse.transform(): LocationResponse {
  return LocationResponse(
      response = response,
      status = status,
      type = type,
      message = message,
      errors = errors

  )
}





