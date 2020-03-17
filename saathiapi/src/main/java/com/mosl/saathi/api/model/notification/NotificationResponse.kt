package com.mosl.saathi.api.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
  @JvmField @SerializedName("Message") val message: String?,
  @JvmField @SerializedName("CreationDate") val creationDate: String?,
  @JvmField @SerializedName("AdditionalData") val additionalData: String
)