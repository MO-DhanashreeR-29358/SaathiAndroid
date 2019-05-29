package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class Product(

  @SerializedName("GroupName")
  @JvmField val groupName: Any,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("ProductName")
  @JvmField val productName: String,

  @SerializedName("IncludeInGroup")
  @JvmField val includeInGroup: Boolean,

  @SerializedName("TraderCode")
  @JvmField val traderCode: String,

  @SerializedName("ProductId")
  @JvmField val productId: Int,

  @SerializedName("BackOfficeProductId")
  @JvmField val backOfficeProductId: Int,

  @SerializedName("ClientProducts")
  @JvmField val clientProducts: Any,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("EffectiveFrom")
  @JvmField val effectiveFrom: Any,

  @SerializedName("AdvisoryMappingTypes")
  @JvmField val advisoryMappingTypes: List<AdvisoryMappingTypesItem?>
)