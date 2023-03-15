package com.mosl.tabapp.leadcreation.ui

import androidx.annotation.StringRes
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

class LeadCreationValidator : Validator<LeadCreationUserInput, String>() {

    override fun execute(input: LeadCreationUserInput): ValidationResult<String> {
        val identifier: Int
        val errorMessageRes = when {

            input.selectedCategory.value == SELECT -> {
                identifier = 1
                R.string.error_message_category
            }

            input.isBank && input.selectedBank.bankId == -1 -> {
                identifier = 27
                R.string.error_message_bank
            }

            input.isBank && input.selectedBranch.branchId == -1 -> {
                identifier = 28
                R.string.error_message_branch
            }

            input.etCategoryName.isBlank() -> {
                identifier = 2
                R.string.error_message_cat_name
            }

            input.etCategoryName.invalidAlphaNumbric() -> {
                identifier = 3
                R.string.error_message_alphabets
            }

            !input.selectedCategory.value.equals("INDIVIDUAL", true) &&
                    input.etContactPerson.isBlank() -> {
                identifier = 4
                R.string.error_message_contact
            }

            !input.selectedCategory.value.equals("INDIVIDUAL", true) &&
                    input.etContactPerson.invalidAlphaNumbric() -> {
                identifier = 5
                R.string.error_message_alphabets
            }

            input.selectedInterestIn.value == SELECT -> {
                identifier = 6
                R.string.error_message_interest
            }

            input.selectedInterestIn.value.equals("PMS", true) &&
                    input.selectedProductType.value == SELECT -> {
                identifier = 7
                R.string.error_message_product_type
            }

            input.emailId.isNotBlank() &&
                    input.emailId.invalidEmail() -> {
                identifier = 8
                R.string.error_message_invalid_email
            } // MOSLConstants.INVALID_EMAIL_ID

            input.alternateEmailId.isNotBlank() &&
                    input.alternateEmailId.invalidEmail() -> {
                identifier = 9
                R.string.error_message_invalid_email
            }

            input.alternateEmailId.isNotBlank() &&
                    input.emailId == input.alternateEmailId -> {
                identifier = 10
                R.string.error_message_same_email
            }

            input.mobileNo.isBlank() -> {
                identifier = 11
                R.string.error_message_enter_mobile
            }

            input.mobileNo.length <= 10 && !input.mobileNo.matches(
                "^[6789]\\d{9}$".toRegex()
            ) -> {
                identifier = 12
                R.string.error_message_invalid_mobile
            }

            input.alternateMobileNo.isNotBlank() &&
                    input.alternateMobileNo.length <= 10 && !input.alternateMobileNo.matches(
                "^[6789]\\d{9}$".toRegex()
            ) -> {
                identifier = 13
                R.string.error_message_invalid_mobile
            }
            input.alternateMobileNo.isNotBlank() &&
                    input.mobileNo == input.alternateMobileNo -> {
                identifier = 14
                R.string.error_message_same_mobile
            }

            input.landlineNo.isNotBlank() &&
                    input.landlineNo.invalidLandLine() -> {
                identifier = 15
                R.string.error_message_invalid_landline
            }

            input.ekycMobileNo.isNotBlank() &&
                    input.ekycMobileNo.length <= 10 && !input.ekycMobileNo.matches(
                "^[6789]\\d{9}$".toRegex()
            ) -> {
                identifier = 16
                R.string.error_message_invalid_mobile
            }

            input.panNo.isNotBlank() &&
                    input.panNo.invalidPan() -> {
                identifier = 17
                R.string.error_message_invalid_pan
            }

            input.aadharNo.isNotBlank() &&
                    input.aadharNo.invalidAadhar() -> {
                identifier = 18
                R.string.error_message_invalid_aadhar
            }

            input.sourceName == MOSLConstants.WEBSITE_CLIENT_REFERRAL &&
                    input.referCode.length < 4 -> {
                identifier = 19
                R.string.error_message_referee_code
            }

            input.referName.isNotBlank() &&
                    input.referName.invalidAlphaNumbric() -> {
                identifier = 20
                R.string.error_message_alphabets
            }

            input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
                    input.referName.isBlank() -> {
                identifier = 21
                R.string.error_message_prospect_referee
            }

            input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
                    input.referMobile.isBlank() -> {
                identifier = 22
                R.string.error_message_enter_mobile
            }

            input.referMobile.isNotBlank() &&
                    input.referMobile.invalidMobileNo() -> {
                identifier = 23
                R.string.error_message_invalid_mobile
            }

            input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
                    input.referEmail.isBlank() -> {
                identifier = 24
                R.string.error_message_enter_email
            }

            input.pinCode.isNotBlank() &&
                    input.pinCode.length < 6 -> {
                identifier = 25
                R.string.error_message_pincode
            }

            input.officePinCode.isNotBlank() &&
                    input.officePinCode.length < 6 -> {
                identifier = 26
                R.string.error_message_pincode
            }
            input.branchCode == MOSLConstants.BRANCH_CODE_FSFBMAIN &&
                    input.referaalCode.isNotBlank() && input.referaalCode.length < 6 -> {
                identifier = 29
                R.string.error_message_invalid_referaal_code
            }

            input.panNo.isBlank() &&
                    input.branchCode == MOSLConstants.BRANCH_CODE_CBIMAIN -> {
                identifier = 30
                R.string.error_message_enter_pan
            }
            input.panNo.isBlank() &&
                    input.branchCode == MOSLConstants.BRANCH_CODE_FSFBMAIN  -> {
                identifier = 31
                R.string.error_message_enter_pan
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