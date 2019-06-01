package com.mosl.saathi.api.model.common

import com.google.gson.annotations.SerializedName

data class ExtraDataCommonResponse<T>(
    @JvmField @SerializedName("Response") val response: ExtraDataInner<T>?,
    @JvmField @SerializedName("Status") val status: Boolean,
    @JvmField @SerializedName("Type") val type: String,
    @JvmField @SerializedName("Message") val message: String,
    @JvmField @SerializedName("Erros") val errors: List<ApiError>

) {
  val data: T?
    get() = response?.Data
}

data class ExtraDataInner<T>(
    @JvmField @SerializedName("ExtraData") val extraData: Any?,
    @JvmField @SerializedName("Data") val Data: T?,
    @JvmField @SerializedName("Errors") val errors: String?,
    @JvmField @SerializedName("Total") val total: String?,
    @JvmField @SerializedName("Permissions") val permission: PermissionModel?
)