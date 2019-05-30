package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class InsertLeadProductRequest(

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

  @SerializedName("IsPotentialPMSProduct")
  @JvmField val isPotentialPMSProduct: String,

  @SerializedName("ProductSpecialist")
  @JvmField val productSpecialist: String,

  @SerializedName("AssignedBy")
  @JvmField val assignedBy: String,

  @SerializedName("UCID")
  @JvmField val uCID: String,

  @SerializedName("PrimaryRM")
  @JvmField val primaryRM: String
)