package com.mosl.mobile.domain.setappointment.lead

data class LookupRequestInput(
  @JvmField val businessUnitId: String,

  @JvmField val lookupTypeId: Int,

  @JvmField val parentId: String?,

  @JvmField val IgnoreBusinessUnit: Boolean?
)
