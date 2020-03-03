package com.mosl.mobile.domain.setappointment.clientdetail

data class AdvisoryMappingTypesItemResponse(

  @JvmField val parentId: Int,

  @JvmField val status: Int,

  @JvmField val active: Boolean,

  @JvmField val lookupType: Int,

  @JvmField val employeeCode: Any,

  @JvmField val lookUpid: Int,

  @JvmField val lookUpName: String,

  @JvmField val businessUnitId: Int,

  @JvmField val sequence: Int,

  @JvmField val lookupTypeId: Int
)