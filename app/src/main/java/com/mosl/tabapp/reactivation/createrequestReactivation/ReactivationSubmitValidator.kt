package com.mosl.tabapp.reactivation.createrequestReactivation

import com.mosl.mobile.domain.reactivation.ReactivationSubmitPostModel
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult

object ReactivationSubmitValidator:Validator<ReactivationSubmitPostModel,ReactivationSubmitPostModel> (){
    override fun execute(input: ReactivationSubmitPostModel): ValidationResult<ReactivationSubmitPostModel> {
        val errorMessage: String? = when {
            input.clientCode.isEmpty() -> "Please enter Client Code"
            input.actualActivationDate.isEmpty() ->"Please select activation date"
            input.remarks.isEmpty()-> "Please enter remark"
            else -> null
        }

        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
           withData(input)
        }
    }

}