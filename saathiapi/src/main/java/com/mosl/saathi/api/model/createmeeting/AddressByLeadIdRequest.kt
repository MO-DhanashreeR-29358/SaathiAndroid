package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class AddressByLeadIdRequest(

  @JvmField @SerializedName("LeadId") val leadId: String,

  @JvmField @SerializedName("MeetingAddressTypeId") val meetingAddressTypeId: String
)