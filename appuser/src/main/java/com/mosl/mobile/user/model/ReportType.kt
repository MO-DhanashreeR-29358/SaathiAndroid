package com.mosl.mobile.user.model

data class ReportType(
  @JvmField val name: String,
  @JvmField val systemName: String,
  @JvmField val childMenuList: List<ChildMenu>
)