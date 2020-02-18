package com.mosl.mobile.domain.leadviewdetail.profiling

data class ProfileRequestData(
  @JvmField val profileParameterID: String,
  @JvmField val profileSubParameterID: String,
  @JvmField val leadID: String,
  @JvmField val value: String
)