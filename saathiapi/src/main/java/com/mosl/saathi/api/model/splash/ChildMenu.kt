package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class ChildMenu(
  @JvmField @SerializedName("Name") val name: String,
  @JvmField @SerializedName("SystemName") val systemName: String,
  @JvmField @SerializedName("ChildMenuList") val ChildMenuList: List<ChildMenu>
) {
  fun isBasicReports(): Boolean {
    return systemName.equals("BASIC_REPORTS")
  }
}