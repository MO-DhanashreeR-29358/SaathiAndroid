package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class AddUpdateLeadRequest(

  @JvmField @SerializedName("CreatedThrough") val createdThrough: String,

  @JvmField @SerializedName("CreationDate") val creationDate: String,

  @JvmField @SerializedName("CreatedBy") val createdBy: String,

  @JvmField @SerializedName("ProfessionID") val professionID: Int,

  @JvmField @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String,

  @JvmField @SerializedName("Comments") val comments: String,

  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,

  @JvmField @SerializedName("SourceID") val sourceID: Int,

  @JvmField @SerializedName("LeadSourceID") val leadSourceID: Int,

  @JvmField @SerializedName("ProspectRefereEmail") val prospectRefereEmail: String,

  @JvmField @SerializedName("LeadSourceDetailsID") val leadSourceDetailsID: Int,

  @JvmField @SerializedName("LeadSourceDetails") val leadSourceDetails: String,

  @JvmField @SerializedName("ProspectRefereName") val prospectRefereName: String,

  @JvmField @SerializedName("contact") val contact: Contact,

  @JvmField @SerializedName(
      "leadProductMapping"
  ) val leadProductMapping: List<LeadProductMappingItem?>,

  @JvmField @SerializedName("LeadCategoryID") val leadCategoryID: Int
)