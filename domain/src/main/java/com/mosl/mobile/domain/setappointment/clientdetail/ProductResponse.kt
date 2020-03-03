package com.mosl.mobile.domain.setappointment.clientdetail

data class ProductResponse(
  @JvmField val groupName: Any,
  @JvmField val status: Int,
  @JvmField val productName: String,
  @JvmField val includeInGroup: Boolean,
  @JvmField val traderCode: String,
  @JvmField val productId: Int,
  @JvmField val backOfficeProductId: Int,
  @JvmField val clientProducts: Any,
  @JvmField val inApproval: Boolean,
  @JvmField val approvalFieldList: Any,
  @JvmField val isAdminMode: Boolean,
  @JvmField val effectiveFrom: Any,
  @JvmField val advisoryMappingTypes: List<AdvisoryMappingTypesItemResponse>
)