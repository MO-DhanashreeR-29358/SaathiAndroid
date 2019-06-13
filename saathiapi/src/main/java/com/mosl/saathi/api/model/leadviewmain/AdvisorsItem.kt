package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class AdvisorsItem(

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("CreationDate") val creationDate: String,

  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: Any,

  @JvmField @SerializedName("AdvisorName") val advisorName: Any,

  @JvmField @SerializedName("CreatedBy") val createdBy: String,

  @JvmField @SerializedName("AdvisorCode") val advisorCode: String,

  @JvmField @SerializedName("Product") val product: Any,

  @JvmField @SerializedName("ClientAdvisoryMappingId") val clientAdvisoryMappingId: Int,

  @JvmField @SerializedName("ProductId") val productId: Int,

  @JvmField @SerializedName("UCID") val uCID: Int,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,

  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: Any,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("EffectiveFrom") val effectiveFrom: String,

  @JvmField @SerializedName("AdvisoryMappingTypeId") val advisoryMappingTypeId: Int,

  @JvmField @SerializedName("BranchCode") val branchCode: String
)