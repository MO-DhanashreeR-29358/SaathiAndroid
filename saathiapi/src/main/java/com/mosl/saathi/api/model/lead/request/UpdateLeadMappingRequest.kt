package com.mosl.saathi.api.model.lead.request

import com.google.gson.annotations.SerializedName

class UpdateLeadMappingRequest(
  @JvmField @SerializedName("LeadID") val leadId: String,
  @JvmField @SerializedName("InterestedIn") val interestedIn: String,
  @JvmField @SerializedName("LeadProductMappingID") val leadProductMappingID: String,
  @JvmField @SerializedName("ProductID") val productID: String,
  @JvmField @SerializedName("PrimaryRM") val primaryRM: String,
  @JvmField @SerializedName("ProductSpecialist") val productSpecialist: String,
  @JvmField @SerializedName("AssignedBy") val assignedBy: String,
  @JvmField @SerializedName("AssignedByDate") val assignedByDate: String,
  @JvmField @SerializedName("StageID") val stageID: String,
  @JvmField @SerializedName("StageName") val stageName: String,
  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: String,
  @JvmField @SerializedName("StatusID") val statusID: String,
  @JvmField @SerializedName("StatusValue") val statusValue: String,
  @JvmField @SerializedName("ActAssignTo") val actAssignTo: String,
  @JvmField @SerializedName("Map") val map: Map<String, Any?>
)