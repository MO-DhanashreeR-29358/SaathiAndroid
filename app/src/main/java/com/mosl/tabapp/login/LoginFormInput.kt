package com.mosl.tabapp.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.tabapp.R
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult

data class LoginFormInput(
  val empCode: String,
  val password: String,
  val isAllPermissionAllowed : Boolean
)

object LoginValidator : Validator<LoginFormInput, LoginInfo>() {
  override fun execute(input: LoginFormInput): ValidationResult<LoginInfo> {
    val errorMessage: Int = when {
      input.empCode.isEmpty() -> R.string.msg_username_empty
      input.password.isEmpty() -> R.string.msg_password_empty
      !input.isAllPermissionAllowed -> R.string.msg_permission
      else -> -1
    }

    return if (errorMessage != -1) {
      withError(errorMessage)
    } else {
      withData(
          LoginInfo(
              empCode = input.empCode,
              password = input.password
          )
      )
    }
  }
}