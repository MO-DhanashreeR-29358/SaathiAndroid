package com.mosl.mobile.user.model

data class CurrentBusinessProfileRequest(
  @JvmField val businessProfileId: String?,
  @JvmField val name: String?,
  @JvmField val businessUnitId: String?
)