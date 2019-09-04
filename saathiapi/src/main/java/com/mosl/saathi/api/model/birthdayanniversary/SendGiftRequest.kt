package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class SendGiftRequest(
  @JvmField @SerializedName("SelectedUcids") val selectedUcids: String,
  @JvmField @SerializedName("EventType") val eventType: String
)