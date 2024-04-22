package com.mosl.user.data.login

import java.io.Serializable

data class VerifyAction(
  @JvmField val requestId: String,
  @JvmField val mobileNumber: String,
  @JvmField val otp: String
) : Serializable