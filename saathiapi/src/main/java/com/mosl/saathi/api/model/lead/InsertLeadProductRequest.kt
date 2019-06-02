package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class InsertLeadProductRequest(
    @JvmField @SerializedName("LeadID") val leadID: String,
    @JvmField @SerializedName("UCID") val uCID: String,
    @JvmField @SerializedName("InterestedIn") val interestedIn: String,
    @JvmField @SerializedName("ProductID") val productID: String,
    @JvmField @SerializedName("PrimaryRM") val primaryRM: String,
    @JvmField @SerializedName("ProductSpecialist") val productSpecialist: String,
    @JvmField @SerializedName("AssignedBy") val assignedBy: String,
    @JvmField @SerializedName("AssignedByDate") val assignedByDate: String,
    @JvmField @SerializedName("StageID") val stageID: String,
    @JvmField @SerializedName("StatusID") val statusID: String,
    @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: String
)