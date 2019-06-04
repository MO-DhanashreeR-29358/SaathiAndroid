package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class CreateLeadResponse(
    @JvmField @SerializedName("LeadID") val leadID: String?,
    @JvmField @SerializedName("DedupeModel") val dedupeModel: DedupeModelResponse?
)

data class DedupeModelResponse(
    @JvmField @SerializedName("LeadID") val leadID: String?,
    @JvmField @SerializedName("CheckRemark") val checkRemark: String?,
    @JvmField @SerializedName("Stage") val stage: String?,
    @JvmField @SerializedName("BranchCode") val branchCode: String?,
    @JvmField @SerializedName("DedupeWith") val dedupeWith: String?,
    @JvmField @SerializedName("ClientName") val clientName: String?,
    @JvmField @SerializedName("ClientCode") val clientCode: String?,
    @JvmField @SerializedName("ProductName") val productName: String?,
    @JvmField @SerializedName("LeadName") val leadName: String?,
    @JvmField @SerializedName("IsTransfer") val isTransfer: String?
)