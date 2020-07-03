package com.mosl.saathi.api.model.webview

import com.google.gson.annotations.SerializedName

data class CommonURLRequest(
    @JvmField @SerializedName("Key")
    val key: String,
    @JvmField @SerializedName("OAOUserid")
    val OAOUserid: String?,
    @JvmField @SerializedName("Employeecode")
    val employeeCode: String?

)

