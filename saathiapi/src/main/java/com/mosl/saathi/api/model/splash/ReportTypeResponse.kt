package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class ReportTypeResponse(
  @JvmField @SerializedName("Name") val name: String,
  @JvmField @SerializedName("SystemName") val systemName: String,
  @JvmField @SerializedName("ChildMenuList") val childMenuList: List<ChildMenu>
) {
  fun isPreDefinedReports(): Boolean {
    return systemName.equals("PRE-DEFINED_REPORTS")
  }
}