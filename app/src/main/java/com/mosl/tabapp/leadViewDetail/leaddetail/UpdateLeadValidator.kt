package com.mosl.tabapp.leadViewDetail.leaddetail

import androidx.annotation.StringRes
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult

class UpdateLeadValidator : Validator<UpdateLeadRequest, String>() {
    override fun execute(input: UpdateLeadRequest): ValidationResult<String> {
        val contact = input.contact
        val identifier: Int
        val errorMessageRes = when {

            contact.name.isBlank() -> {
                identifier = 1
                R.string.error_message_cat_name
            }

            contact.name.invalidAlphaNumbric() -> {
                identifier = 2
                R.string.error_message_alphabets
            }

            contact.emailId.run { isNotBlank() && invalidEmail() } -> {
                identifier = 3
                R.string.error_message_invalid_email
            } // MOSLConstants.INVALID_EMAIL_ID


            contact.alternateEmailId.run { isNotBlank() && invalidEmail() } -> {
                identifier = 4
                R.string.error_message_invalid_email
            }

            contact.alternateEmailId.run { isNotBlank() && contact.emailId == this } -> {
                identifier = 5
                R.string.error_message_same_email
            }

            contact.alternateMobileNo.run {
                isNotBlank() &&
                        length <= 10 && !matches(
                        "^[6789]\\d{9}$".toRegex()
                )
            } -> {
                identifier = 6
                R.string.error_message_invalid_mobile
            }

            contact.alternateMobileNo.run {
                isNotBlank() &&
                        contact.mobile == this
            } -> {
                identifier = 7
                R.string.error_message_same_mobile
            }

            contact.landlineNo.run { isNotBlank() && invalidLandLine() } -> {
                identifier = 8
                R.string.error_message_invalid_landline
            }

            contact.panNumber.run { isNotBlank() && invalidPan() } -> {
                identifier = 9
                R.string.error_message_invalid_pan
            }

            contact.aadharNumber.run { isNotBlank() && invalidAadhar() } -> {
                identifier = 10
                R.string.error_message_invalid_aadhar
            }

            input.refereeName.run { isNotBlank() && invalidAlphaNumbric() } -> {
                identifier = 11
                R.string.error_message_alphabets
            }

            input.prospectRefereMobile.run {
                isNotBlank() && length <= 10 && !matches(
                        "^[6789]\\d{9}$".toRegex()
                )
            } -> {
                identifier = 12
                R.string.error_message_invalid_mobile
            }

            input.prospectRefereEmail.run { isNotBlank() && invalidEmail() } -> {
                identifier = 13
                R.string.error_message_invalid_email
            }

            else -> {
                identifier = 0
                null
            }
        }

        return returnValidationResult(
                errorMessageRes,
                actionOnError = {
                    withError(error = requireNotNull(errorMessageRes), identifier = identifier)
                },
                actionOnSuccess = { withData("Input is correct") }
        )

    }

    private inline fun returnValidationResult(
            @StringRes errorMessageRes: Int?,
            actionOnError: (Int) -> ValidationResult<String>,
            actionOnSuccess: () -> ValidationResult<String>
    ): ValidationResult<String> {
        return if (errorMessageRes != null)
            actionOnError(errorMessageRes)
        else
            actionOnSuccess()
    }

    private fun String.invalidAlphaNumbric(): Boolean {
        return !CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(this)
    }

    private fun String.invalidEmail(): Boolean {
        return !CRMValidationRule.isValidEmail(this)
    }

    private fun String.invalidMobileNo(): Boolean {
        return !CRMValidationRule.validateMobile(this)
    }

    private fun String.invalidLandLine(): Boolean {
        return !CRMValidationRule.validateTel(this)
    }

    private fun String.invalidPan(): Boolean {
        return !CRMValidationRule.isValidPannumber(this)
    }

    private fun String.invalidAadhar(): Boolean {
        return !CRMValidationRule.validateAadhar(this)
    }

    companion object {
        const val ERROR_ALPHABETS = "Only alphabets are allowed"
        const val ERROR_CONTACT_PERSON = "Enter contact person"
        const val ERROR_REFEREE_CODE = "Enter referee code"
        const val SELECT = "Select"
        const val ERROR_PINCODE = "Invalid Pincode"
    }
}