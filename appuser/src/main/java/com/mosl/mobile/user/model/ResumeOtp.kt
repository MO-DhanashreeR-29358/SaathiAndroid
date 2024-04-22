package com.mosl.mobile.user.model

data class ResumeOtp (
    val retryAfter: Int,
    val resendCounter: Int,
    val status: Status,
    val message: String
)
enum class Status(val id: String) {
    SUCCESS(id = "1"),
    FAIL(id = "0"),
    PARTIAL_SUCCESS(id = "2"),
    REGISTRATION_SUCCESS(id = "4")
}