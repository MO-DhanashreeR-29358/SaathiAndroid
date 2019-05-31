package com.mosl.saathi.api.model.common

import com.google.gson.annotations.SerializedName

data class SaathiCommonResponse<T>(
    @JvmField @SerializedName("Response") val response: T?,
    @JvmField @SerializedName("Status") val status: Boolean,
    @JvmField @SerializedName("Type") val type: String,
    @JvmField @SerializedName("Message") val message: String,
    @JvmField @SerializedName("Erros") val errors: List<ApiError>
)

data class ApiError(
    @JvmField @SerializedName("Message") val message: String
)