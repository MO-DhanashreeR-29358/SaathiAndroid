package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

open class HrmsErrorResponse {

  @JvmField @SerializedName("ErrorSource") val errorSource: Int? = null
  @JvmField @SerializedName("Message") val message: String? = null
  @JvmField @SerializedName("ErrorState") val errorState: Int? = null
  @JvmField @SerializedName("ErrorNumber") val errorNumber: Int? = null
  @JvmField @SerializedName("ErrorMessage") val errorMessage: String? = null

  fun hasError(): Boolean {
    if (errorNumber != null) {
      return errorNumber > 0
    }
    return false
  }
}