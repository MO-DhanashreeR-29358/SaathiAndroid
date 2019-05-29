package com.mosl.saathi.saathiapi.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class SaveLeadCrossSellingProductRequest(

  @SerializedName("InterestedInId")
  @JvmField val interestedInId: String,

  @SerializedName("CrossSellingProductId")
  @JvmField val crossSellingProductId: String,

  @SerializedName("LeadId")
  @JvmField val leadId: String,

  @SerializedName("CrossSellingStatusId")
  @JvmField val crossSellingStatusId: String
)