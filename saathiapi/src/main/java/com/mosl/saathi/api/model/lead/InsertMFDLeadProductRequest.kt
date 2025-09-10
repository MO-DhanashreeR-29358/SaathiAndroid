package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class InsertMFDLeadProductRequest(

  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("NewInterestedIn") val newInterestedIn: String,
@JvmField @SerializedName("NewProductname") val newProductName: String,
  @JvmField @SerializedName("PrimaryRM") val primaryRM: String,
@JvmField @SerializedName("OAOUserId") val oaoUserId: String? = null,
  @JvmField @SerializedName("OldInterestedIn") val oldInterestedIn: String? = null,
  @JvmField @SerializedName("OldProductname") val oldProductName: String? = null,
@JvmField @SerializedName("StageId") val stageID: String? = null,
@JvmField @SerializedName("statusId") val statusID: String? = null,
@JvmField @SerializedName("Channel") val profileName: String? = null,
  @JvmField @SerializedName("Param_extra1") val extra1: String? = null,
  @JvmField @SerializedName("Param_extra2") val extra2: String? = null,
  @JvmField @SerializedName("Param_extra3") val extra3: String? = null,
  @JvmField @SerializedName("CreatedBy") val createdBy: String? = null,
@JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean =true,
@JvmField @SerializedName("Status") val status: Int = 1
)