package com.mosl.mobile.user.model

import com.mosl.saathi.api.model.splash.ChildMenu

data class ChildMenu(
  @JvmField val name: String,
  @JvmField val systemName: String,
  @JvmField val ChildMenuList: List<ChildMenu>
) {
  fun isBasicReports(): Boolean {
    return systemName.equals("BASIC_REPORTS")
  }
}