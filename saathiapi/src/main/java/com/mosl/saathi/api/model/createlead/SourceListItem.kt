package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class SourceListItem(
  @JvmField @SerializedName("Status") val status: Int,
  @JvmField @SerializedName("CreationDate") val creationDate: String,
  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: Any,
  @JvmField @SerializedName("CreatedBy") val createdBy: String,
  @JvmField @SerializedName("IdentifierPropertyName") val identifierPropertyName: String,
  @JvmField @SerializedName("SourceID") val sourceID: Int,
  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,
  @JvmField @SerializedName("SourceName") val sourceName: String,
  @JvmField @SerializedName("Sequence") val sequence: Int,
  @JvmField @SerializedName("InApproval") val inApproval: Boolean,
  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,
  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: Any,
  @JvmField @SerializedName("EntityStatus") val entityStatus: Int
)