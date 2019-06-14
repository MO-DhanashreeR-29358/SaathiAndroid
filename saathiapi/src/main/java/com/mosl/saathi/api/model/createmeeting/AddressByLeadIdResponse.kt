package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class AddressByLeadIdResponse(

  @JvmField @SerializedName("ISComplete") val iSComplete: Int,

  @JvmField @SerializedName("MeetingAddress") val meetingAddress: String,

  @JvmField @SerializedName("LeadId") val leadId: Int,

  @JvmField @SerializedName("ErrorMessage") val errorMessage: String,

  @JvmField @SerializedName("MeetingAddressTypeId") val meetingAddressTypeId: Int,

  @JvmField @SerializedName("BranchCode") val branchCode: Any,

  @JvmField @SerializedName("Pincode") val pincode: String,

  @JvmField @SerializedName("IsDisable") val isDisable: Boolean
)