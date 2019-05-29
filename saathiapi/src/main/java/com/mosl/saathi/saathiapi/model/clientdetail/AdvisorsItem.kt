package com.mosl.saathi.saathiapi.model.clientdetail

import com.google.gson.annotations.SerializedName

data class AdvisorsItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("AdvisorName")
  @JvmField val advisorName: Any,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: String,

  @SerializedName("AdvisorCode")
  @JvmField val advisorCode: String,

  @SerializedName("Product")
  @JvmField val product: Any,

  @SerializedName("ClientAdvisoryMappingId")
  @JvmField val clientAdvisoryMappingId: Int,

  @SerializedName("ProductId")
  @JvmField val productId: Int,

  @SerializedName("UCID")
  @JvmField val uCID: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("EffectiveFrom")
  @JvmField val effectiveFrom: String,

  @SerializedName("AdvisoryMappingTypeId")
  @JvmField val advisoryMappingTypeId: Int,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String
)