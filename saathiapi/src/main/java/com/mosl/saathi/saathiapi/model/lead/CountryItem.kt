package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class CountryItem(

  @SerializedName("CountryId")
  @JvmField val countryId: Int,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("LastModifiedBy")
  @JvmField val lastModifiedBy: String,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: String,

  @SerializedName("CountryName")
  @JvmField val countryName: String,

  @SerializedName("IdentifierPropertyName")
  @JvmField val identifierPropertyName: String,

  @SerializedName("ShortName")
  @JvmField val shortName: String,

  @SerializedName("States")
  @JvmField val states: List<Any?>,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("LastModificationDate")
  @JvmField val lastModificationDate: String,

  @SerializedName("EntityStatus")
  @JvmField val entityStatus: Int
)