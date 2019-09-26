package com.mosl.mobile.domain.location

data class LocationResponse(
  @JvmField val response: String?,
  @JvmField val status: Boolean,
  @JvmField val type: String,
  @JvmField val message: String,
  @JvmField val errors: List<Any>?
) {
  val isSuccessful: Boolean
    get() = status == true

}

data class ApiError(
  @JvmField val message: String
)