package com.mosl.saathi.api.model.common

import com.google.gson.annotations.SerializedName

data class SaathiCommonResponse<T>(
  @JvmField @SerializedName("Response") val response: T?,
    @JvmField @SerializedName("Status") val status: Boolean,
    @JvmField @SerializedName("Type") val type: String,
    @JvmField @SerializedName("Message") val message: String,
  @JvmField @SerializedName("Erros") val errors: List<ApiError>?
) {
  val isSuccessful: Boolean
    get() = status

  fun getMessage(): String {
    return errors?.let { it[0].message } ?: "An Error Occurred"
  }
}

data class ApiError(
    @JvmField @SerializedName("Message") val message: String
)