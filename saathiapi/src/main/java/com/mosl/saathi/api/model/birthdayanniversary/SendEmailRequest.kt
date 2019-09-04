package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class SendEmailRequest(

  @JvmField @SerializedName("SelectedUcids")
  val selectedUcids: String?,

  @JvmField @SerializedName("checkAll")
  val checkAll: Boolean,

  @JvmField @SerializedName("EventType")
  val eventType: String,

  @JvmField @SerializedName("MessageType")
  val messageType: String,

  @JvmField @SerializedName("EmployeeCode")
  val employeeCode: String?,

  @JvmField @SerializedName("FilterType")
  val filterType: String?

)