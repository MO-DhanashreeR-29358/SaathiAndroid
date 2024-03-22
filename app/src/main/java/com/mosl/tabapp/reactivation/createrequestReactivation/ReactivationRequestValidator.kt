package com.mosl.tabapp.reactivation.createrequestReactivation

import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult

object ReactivationRequestValidator :Validator<ReactivationClientSearchPostModel, ReactivationClientSearchPostModel>() {
    override fun execute(input: ReactivationClientSearchPostModel): ValidationResult<ReactivationClientSearchPostModel> {
        val errorMessage: String? = when {
            input.clientCode.isEmpty() -> "Please enter Client Code"
            else -> null
        }

        return if (errorMessage != null) {
          withMessage(errorMessage)
        } else {
            withData(input)
        }
    }
}