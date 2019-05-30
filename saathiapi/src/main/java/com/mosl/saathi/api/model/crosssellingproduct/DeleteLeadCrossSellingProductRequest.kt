package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class DeleteLeadCrossSellingProductRequest(

  @SerializedName("InterestedIn")
  @JvmField val interestedIn: String,

  @SerializedName("StageID")
  @JvmField val stageID: String,

  @SerializedName("AssignedByDate")
  @JvmField val assignedByDate: String,

  @SerializedName("StatusID")
  @JvmField val statusID: String,

  @SerializedName("LeadID")
  @JvmField val leadID: String,

  @SerializedName("ProductID")
  @JvmField val productID: String,

  @SerializedName("LeadProductMappingID")
  @JvmField val leadProductMappingID: String,

  @SerializedName("AssignedBy")
  @JvmField val assignedBy: String,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: String
)