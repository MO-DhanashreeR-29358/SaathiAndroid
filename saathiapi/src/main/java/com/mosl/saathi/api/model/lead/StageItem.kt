package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageItem(
  @JvmField @SerializedName("Status") val status: Int,
  @JvmField @SerializedName("CreationDate") val creationDate: String,
  @JvmField @SerializedName("LastModifiedBy") val lastModifiedBy: Any,
  @JvmField @SerializedName("CreatedBy") val createdBy: Any,
  @JvmField @SerializedName("StageName") val stageName: String,
  @JvmField @SerializedName("StatusName") val statusName: String,
  @JvmField @SerializedName("IdentifierPropertyName") val identifierPropertyName: String,
  @JvmField @SerializedName("StageID") val stageID: Int,
  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,
  @JvmField @SerializedName("Sequence") val sequence: Int,
  @JvmField @SerializedName("StageCode") val stageCode: String,
  @JvmField @SerializedName("InApproval") val inApproval: Boolean,
  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any,
  @JvmField @SerializedName("LastModificationDate") val lastModificationDate: Any,
  @JvmField @SerializedName("EntityStatus") val entityStatus: Int
)