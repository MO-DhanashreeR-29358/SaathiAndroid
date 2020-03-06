package com.mosl.mobile.domain.setappointment.clientdetail

data class AdvisorsItemResponse(
  @JvmField val status: Int,
  @JvmField val creationDate: String,
  @JvmField val lastModifiedBy: String?,
  @JvmField val advisorName: String?,
  @JvmField val createdBy: String,
  @JvmField val advisorCode: String,
  @JvmField val product: String?,
  @JvmField val clientAdvisoryMappingId: Int,
  @JvmField val productId: Int,
  @JvmField val uCID: Int,
  @JvmField val inApproval: Boolean,
  @JvmField val approvalFieldList: String?,
  @JvmField val lastModificationDate: String?,
  @JvmField val isAdminMode: Boolean,
  @JvmField val effectiveFrom: String,
  @JvmField val advisoryMappingTypeId: Int,
  @JvmField val branchCode: String
)