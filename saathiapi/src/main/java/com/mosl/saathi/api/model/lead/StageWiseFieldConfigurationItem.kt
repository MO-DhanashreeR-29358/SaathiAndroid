package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageWiseFieldConfigurationItem(

  @SerializedName("IsMandetory")
  @JvmField val isMandetory: Boolean,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: Any?,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: String,

  @SerializedName("FieldSystemName")
  @JvmField val fieldSystemName: String,

  @SerializedName("IsHide")
  @JvmField val isHide: Boolean,

  @SerializedName("FlagForLeadProductMapping")
  @JvmField val flagForLeadProductMapping: String,

  @SerializedName("ForDefaultProductOnly")
  @JvmField val forDefaultProductOnly: Boolean,

  @SerializedName("IdentifierPropertyName")
  @JvmField val identifierPropertyName: String,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("StageId")
  @JvmField val stageId: Int,

  @SerializedName("FlagForLead")
  @JvmField val flagForLead: String,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("FieldDisplayName")
  @JvmField val fieldDisplayName: String,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any?,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: String,

  @SerializedName("StatusId")
  @JvmField val statusId: Int,

  @SerializedName("EntityStatus")
  @JvmField val entityStatus: Int,

  @SerializedName("ID")
  @JvmField val iD: Int
)