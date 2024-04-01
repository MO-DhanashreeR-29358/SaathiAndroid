package com.mosl.user.domain.model

data class TokenInfo(
  @JvmField val accessToken: String,
  @JvmField val refreshToken: String,
  @JvmField val expiresIn: Int
)

data class JwtTokens(
  @JvmField val accessToken: String,
  @JvmField val refreshToken: String
)