package com.mosl.saathi.api.model.crosssellingproduct

import com.google.gson.annotations.SerializedName

data class DeleteLeadCrossSellingProductRequest(
    @JvmField @SerializedName("LeadID") val leadID: String,
    @JvmField @SerializedName("InterestedIn") val interestedIn: String,
    @JvmField @SerializedName("LeadProductMappingID") val leadProductMappingID: String,
    @JvmField @SerializedName("ProductID") val productID: String,
    @JvmField @SerializedName("PrimaryRM") val primaryRM: String,
    @JvmField @SerializedName("AssignedBy") val assignedBy: String,
    @JvmField @SerializedName("AssignedByDate") val assignedByDate: String,
    @JvmField @SerializedName("StageID") val stageID: String,
    @JvmField @SerializedName("StatusID") val statusID: String
)