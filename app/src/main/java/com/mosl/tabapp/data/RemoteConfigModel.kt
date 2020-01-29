package com.mosl.tabapp.data

import com.google.gson.annotations.SerializedName

data class RemoteConfigModel(

    @JvmField @SerializedName("isConfigured") var isConfigured: Boolean,
    @JvmField @SerializedName("image_url") var imageUrl: String,
    @JvmField @SerializedName("action_url") var actionUrl: String,
    @JvmField @SerializedName("target") var target: Int
)

