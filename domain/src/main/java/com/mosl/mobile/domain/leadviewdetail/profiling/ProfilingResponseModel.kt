package com.mosl.mobile.domain.leadviewdetail.profiling

data class ProfilingResponseModel(
  @JvmField val response: String?,
  @JvmField val status: Boolean,
  @JvmField val message: String
) {
  val isSuccessful: Boolean
    get() = status == true

}