package com.mosl.user.domain.model

data class GenerateOTP(
  @JvmField val requestId: String,
  @JvmField val oTP: String
)