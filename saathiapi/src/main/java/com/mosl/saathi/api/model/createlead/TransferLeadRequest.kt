package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class TransferLeadRequest(
    @JvmField @SerializedName("Mobile") val mobile: String,
    @JvmField @SerializedName("CurrentUser") val currentUser: String
)

data class TransferLeadResponse(
    @JvmField @SerializedName("IsPaymentDone") val isPaymentDone: Boolean
)