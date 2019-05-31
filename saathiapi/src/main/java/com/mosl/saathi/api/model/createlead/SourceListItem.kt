package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class SourceListItem(

  @SerializedName("Status") @JvmField val status: Int,

  @SerializedName("CreationDate") @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy") @JvmField val lastModifiedBy: Any,

  @SerializedName("CreatedBy") @JvmField val createdBy: String,

  @SerializedName("IdentifierPropertyName") @JvmField val identifierPropertyName: String,

  @SerializedName("SourceID") @JvmField val sourceID: Int,

  @SerializedName("BusinessUnitID") @JvmField val businessUnitID: Int,

  @SerializedName("SourceName") @JvmField val sourceName: String,

  @SerializedName("Sequence") @JvmField val sequence: Int,

  @SerializedName("InApproval") @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList") @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate") @JvmField val lastModificationDate: Any,

  @SerializedName("EntityStatus") @JvmField val entityStatus: Int
)