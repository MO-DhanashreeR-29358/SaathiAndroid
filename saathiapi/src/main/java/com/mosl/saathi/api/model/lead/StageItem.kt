package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: Any,

  @SerializedName("StageName")
  @JvmField val stageName: String,

  @SerializedName("IdentifierPropertyName")
  @JvmField val identifierPropertyName: String,

  @SerializedName("StageID")
  @JvmField val stageID: Int,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("Sequence")
  @JvmField val sequence: Int,

  @SerializedName("StageCode")
  @JvmField val stageCode: String,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("EntityStatus")
  @JvmField val entityStatus: Int
)