package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class LeadStageDetailResponse(

    @SerializedName("LeadID"          ) var LeadID          : String?  = null,
    @SerializedName("InterestedIn"     ) var InterestedIn    : String?  = null,
    @SerializedName("LeadProduct"     ) var LeadProduct       : String?  = null,
    @SerializedName("LeadStageID"     ) var LeadStageID        : String?  = null,
    @SerializedName("LeadStageCode"   ) var LeadStageCode      : String?  = null,
    @SerializedName("LeadStageName"   ) var LeadStageName      : String?  = null,
    @SerializedName("LeadStatusID"    ) var LeadStatusID      : String?  = null,
    @SerializedName("LeadStatus"     ) var LeadStatus         : String?  = null,
    @SerializedName("Status"         ) var Status             : String?  = null

    )