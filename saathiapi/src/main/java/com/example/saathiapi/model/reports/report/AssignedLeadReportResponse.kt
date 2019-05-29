package com.example.saathiapi.model.reports.report

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class AssignedLeadReportResponse(

  @field:SerializedName("Response")
  val response: List<ResponseItem>,

  @field:SerializedName("Status")
  val status: Boolean,

  @field:SerializedName("Type")
  val type: String,

  @field:SerializedName("Message")
  val message: String,

  @field:SerializedName("Erros")
  val erros: List<Any>
) {
  fun isSuccess(): Boolean {
    return status.equals(true)
  }
}