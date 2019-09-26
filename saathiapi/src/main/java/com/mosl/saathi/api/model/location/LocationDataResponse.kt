package com.mosl.saathi.api.model.location

import com.google.gson.annotations.SerializedName

data class LocationDataResponse(
  @JvmField @SerializedName("Response") val response: String?,
  @JvmField @SerializedName("Status") val status: Boolean,
  @JvmField @SerializedName("Type") val type: String,
  @JvmField @SerializedName("Message") val message: String,
  @JvmField @SerializedName("Erros") val errors: List<ApiError>?
) {
  val isSuccessful: Boolean
    get() = status == true

  fun getMessage(): String {
    return errors!!.get(0).message ?: "An Error Occurred"
  }
}

data class ApiError(
  @JvmField @SerializedName("Message") val message: String
)