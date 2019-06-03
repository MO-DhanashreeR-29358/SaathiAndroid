package com.mosl.saathi.api.model.notification

import com.google.gson.annotations.SerializedName

class NotificationRequest(
  @JvmField @SerializedName("Category") val category: String
)