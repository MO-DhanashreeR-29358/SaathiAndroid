package com.mosl.mobile.domain.setappointment

data class AutoCompleteAttendeeRequestInput(
  @JvmField val employeeCode: String,

  @JvmField val employeeName: String
)

data class LookupRequestInput(
  @JvmField val businessUnitId: String,

  @JvmField val lookupTypeId: Int,

  @JvmField val parentId: String?,

  @JvmField val IgnoreBusinessUnit: Boolean?
)
