package com.mosl.tabapp.leadcreation

import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult

class LeadCreationValidator : Validator<LeadCreationUserInput, String>() {

  override fun execute(input: LeadCreationUserInput): ValidationResult<String> {
    val errorMessage = when {
      input.selectedCategory.value == SELECT -> "Select Category"
      input.etCategoryName.isBlank() -> "Select ${input.selectedCategory.value} name"
      input.etCategoryName.invalidAlphaNumbric() -> ERROR_ALPHABETS
      input.selectedCategory.value != "INDIVIDUAL" &&
          input.etContactPerson.isBlank() -> ERROR_CONTACT_PERSON
      input.etContactPerson.invalidAlphaNumbric() -> ERROR_ALPHABETS
      input.selectedInterestIn.value == SELECT -> "Select Interest In"
      input.selectedInterestIn.value == "PMS" &&
          input.selectedProductType.value == SELECT -> "Select ProductType"
      input.emailId.isBlank() -> MOSLConstants.ENTER_EMAIL_ID
      input.emailId.invalidEmail() -> MOSLConstants.INVALID_EMAIL_ID
      input.alternateEmailId.isNotBlank() &&
          input.alternateEmailId.invalidEmail() -> MOSLConstants.INVALID_EMAIL_ID
      input.alternateEmailId.isNotBlank() &&
          input.emailId == input.alternateEmailId -> MOSLConstants.SAME_EMAILMESSAGE
      input.mobileNo.isBlank() -> MOSLConstants.ENTER_MOBILE_NUMBER
      input.mobileNo.invalidMobileNo() -> MOSLConstants.INVALID_MOBILE_NO
      input.alternateMobileNo.isNotBlank() &&
          input.alternateMobileNo.invalidMobileNo() -> MOSLConstants.INVALID_MOBILE_NO
      input.alternateMobileNo.isNotBlank() &&
          input.mobileNo == input.alternateMobileNo -> MOSLConstants.SAME_MOBILEMESSAGE
      input.landlineNo.isNotBlank() &&
          input.landlineNo.invalidLandLine() -> MOSLConstants.INVALID_LAND_LINE
      input.ekycMobileNo.isNotBlank() &&
          input.ekycMobileNo.invalidMobileNo() -> MOSLConstants.INVALID_MOBILE_NO
      input.panNo.isNotBlank() &&
          input.panNo.invalidPan() -> MOSLConstants.INVALID_PAN
      input.aadharNo.isNotBlank() &&
          input.aadharNo.invalidAadhar() -> MOSLConstants.INVALID_AADHAR
      input.sourceName == MOSLConstants.WEBSITE_CLIENT_REFERRAL &&
          input.refereeCode.length < 4 -> ERROR_REFEREE_CODE
      input.refereeName.isNotBlank() &&
          input.refereeName.invalidAlphaNumbric() -> ERROR_ALPHABETS
      input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
          input.refereeName.isBlank() -> MOSLConstants.ENTER_PROSPECT_REFEREE_NAME
      input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
          input.refereeMobile.isBlank() -> MOSLConstants.ENTER_MOBILE_NUMBER
      input.refereeMobile.isNotBlank() &&
          input.refereeMobile.invalidMobileNo() -> MOSLConstants.INVALID_MOBILE_NO

      input.sourceName == MOSLConstants.WEBSITE_PROSPECT_REFERRAL &&
          input.refereeEmail.isBlank() -> MOSLConstants.ENTER_EMAIL_ID
      input.refereeEmail.isNotBlank() &&
          input.refereeEmail.invalidEmail() -> MOSLConstants.INVALID_EMAIL_ID
      input.pinCode.isNotBlank() &&
          input.pinCode.length < 6 -> ERROR_PINCODE
      input.officePinCode.isNotBlank() &&
          input.officePinCode.length < 6 -> ERROR_PINCODE


      else -> null
    }

    return if (errorMessage != null) {
      withMessage(errorMessage)
    } else {
      withData("Input is correct")
    }

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