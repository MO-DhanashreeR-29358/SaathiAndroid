package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class UpdateLeadRequest(

  @SerializedName("ProfileID")
  @JvmField val profileID: Int,

  @SerializedName("CreatedThrough")
  @JvmField val createdThrough: Int,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: String,

  @SerializedName("ProfessionID")
  @JvmField val professionID: Int,

  @SerializedName("ProspectRefereMobile")
  @JvmField val prospectRefereMobile: String,

  @SerializedName("BusinessUnitID")
  @JvmField val businessUnitID: Int,

  @SerializedName("SourceID")
  @JvmField val sourceID: Int,

  @SerializedName("LeadSourceID")
  @JvmField val leadSourceID: Int,

  @SerializedName("ProspectRefereEmail")
  @JvmField val prospectRefereEmail: String,

  @SerializedName("LeadSourceDetailsID")
  @JvmField val leadSourceDetailsID: Int,

  @SerializedName("IsPotentialPMSProductValue")
  @JvmField val isPotentialPMSProductValue: String,

  @SerializedName("LeadSourceDetails")
  @JvmField val leadSourceDetails: String,

  @SerializedName("ProspectRefereName")
  @JvmField val prospectRefereName: String,

  @SerializedName("contact")
  @JvmField val contact: Contact,

  @SerializedName("LeadID")
  @JvmField val leadID: Int,

  @SerializedName("leadFieldValueModels")
  @JvmField val leadFieldValueModels: List<Any>
)