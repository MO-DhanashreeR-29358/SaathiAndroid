package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class UpdateLeadRequest(
    @JvmField @SerializedName("ProfileID") val profileID: Int,
    @JvmField @SerializedName("CreatedThrough") val createdThrough: Int,
    @JvmField @SerializedName("CreationDate") val creationDate: String,
    @JvmField @SerializedName("CreatedBy") val createdBy: String,
    @JvmField @SerializedName("ProfessionID") val professionID: Int,
    @JvmField @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String,
    @JvmField @SerializedName("BusinessUnitID") val businessUnitID: Int,
    @JvmField @SerializedName("SourceID") val sourceID: Int,
    @JvmField @SerializedName("LeadSourceID") val leadSourceID: Int,
    @JvmField @SerializedName("ProspectRefereEmail") val prospectRefereEmail: String,
    @JvmField @SerializedName("LeadSourceDetailsID") val leadSourceDetailsID: Int,
    @JvmField @SerializedName("IsPotentialPMSProductValue") val isPotentialPMSProductValue: String,
    @JvmField @SerializedName("LeadSourceDetails") val leadSourceDetails: String,
    @JvmField @SerializedName("ProspectRefereName") val prospectRefereName: String,
    @JvmField @SerializedName("contact") val contact: Contact,
    @JvmField @SerializedName("LeadID") val leadID: Int,
    @JvmField @SerializedName("leadFieldValueModels") val leadFieldValueModels: List<Any>
)