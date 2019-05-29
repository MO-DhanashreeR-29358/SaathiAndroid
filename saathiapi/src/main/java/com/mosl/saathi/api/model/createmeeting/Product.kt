package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class Product(

  @JvmField @SerializedName("GroupName")
  val groupName: Any,

  @JvmField @SerializedName("Status")
  val status: Int,

  @JvmField @SerializedName("ProductName")
  val productName: String,

  @JvmField @SerializedName("IncludeInGroup")
  val includeInGroup: Boolean,

  @JvmField @SerializedName("TraderCode")
  val traderCode: String,

  @JvmField @SerializedName("ProductId")
  val productId: Int,

  @JvmField @SerializedName("BackOfficeProductId")
  val backOfficeProductId: Int,

  @JvmField @SerializedName("ClientProducts")
  val clientProducts: Any,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean,

  @JvmField @SerializedName("EffectiveFrom")
  val effectiveFrom: Any,

  @JvmField @SerializedName("AdvisoryMappingTypes")
  val advisoryMappingTypes: List<AdvisoryMappingTypesItem?>
)