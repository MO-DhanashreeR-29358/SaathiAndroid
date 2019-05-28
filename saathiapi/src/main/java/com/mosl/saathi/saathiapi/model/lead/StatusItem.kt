package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class StatusItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: Any,

  @SerializedName("IdentifierPropertyName")
  @JvmField val identifierPropertyName: String,

  @SerializedName("StageID")
  @JvmField val stageID: Int,

  @SerializedName("StatusName")
  @JvmField val statusName: String,

  @SerializedName("Sequence")
  @JvmField val sequence: Int,

  @SerializedName("StatusCode")
  @JvmField val statusCode: Any,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: Any,

  @SerializedName("StatusID")
  @JvmField val statusID: Int,

  @SerializedName("EntityStatus")
  @JvmField val entityStatus: Int
)