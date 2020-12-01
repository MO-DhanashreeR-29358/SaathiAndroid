package com.mosl.saathi.api.model.webview

import com.google.gson.annotations.SerializedName

data class CommonURLResponse(
    @SerializedName("Key") @JvmField val key: String,
    @SerializedName("OAOUserid") @JvmField val OAOUserid: String?,
    @SerializedName("URL") @JvmField val url: String?

)