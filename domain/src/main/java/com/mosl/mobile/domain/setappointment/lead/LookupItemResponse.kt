package com.mosl.mobile.domain.setappointment.lead

data class LookupItemResponse(
  @JvmField val parentId: Int,
  @JvmField val status: Int,
  @JvmField val sysName: String?,
  @JvmField val active: Boolean,
  @JvmField val lookupType: Int,
  @JvmField val lookUpid: Int,
  @JvmField val lookUpName: String?,
  @JvmField val businessUnitId: Int,
  @JvmField val sequence: Int,
  @JvmField val ignoreBusinessUnit: Boolean,
  @JvmField val lookupTypeId: Int
)