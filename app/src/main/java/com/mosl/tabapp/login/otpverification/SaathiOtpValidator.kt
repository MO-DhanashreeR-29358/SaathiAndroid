package com.mosl.tabapp.login.otpverification

import android.content.Context
import com.mosl.tabapp.R
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

object SaathiOtpValidator  : Validator<String, String>() {
    override fun execute(input: String): ValidationResult<String> {
        val errorMessage: String? = when {
            input.length <4 -> "Please enter OTP"
            else -> null
        }

        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
            withData(input)
        }
    }
}