package com.mosl.mobile.domain.attendance

data class AppInfo(
  @JvmField val name: String,
  @JvmField val os: String,
  @JvmField val versionName: String,
  @JvmField val versionCode: Int
) {
  override fun toString(): String {
    return "$name/$os/$versionName($versionCode)"
  }
}