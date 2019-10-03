package com.mosl.mobile.domain.location

data class LocationDataModel(
  val employeeCode: String,
  val geoArea: String,
  val geoLongitude: String,
  val geoLatitude: String,
  val time: String,
  val deviceId: String,
  val accuracy: String,
  val geoPinCode: String,
  val id: String
)