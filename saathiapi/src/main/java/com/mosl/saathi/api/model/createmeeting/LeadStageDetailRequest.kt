package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class LeadStageDetailRequest(

  @JvmField @SerializedName("LeadID") val leadId: String,

  @JvmField @SerializedName("MeetingInterestedInList") val productIdList: List<String>
)