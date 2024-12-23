package com.mosl.saathi.api.model.saathiIndirect.SetAppointment

import com.google.gson.annotations.SerializedName
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem

data class AttendeeResponse(
    @field:SerializedName("Response")
    val response: List<AutoCompleteAttendeeItem>? = null,

    @field:SerializedName("Status")
    val status: Boolean? = null,

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Erros")
    val erros: List<Any?>? = null
)
