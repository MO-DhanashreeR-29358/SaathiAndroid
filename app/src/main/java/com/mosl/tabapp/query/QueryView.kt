package com.mosl.tabapp.query

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.tabapp.R
import com.mosl.tabapp.query.QueryUiEvent.OnSubmitConfirmClick
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import io.reactivex.rxjava3.core.Observable

interface QueryView : BaseView {
  fun getQueryMessgae(): String

  fun onSubmitBtnClick(): Observable<Unit>
  fun onEsignClick(): Observable<Unit>
  fun onSubmitDialogOpen(uiModel: UiModel<String>)
  fun onSubmitConfirmClicked(): Observable<OnSubmitConfirmClick>
  fun onResetBtnClick(): Observable<Unit>

  fun onTelePhoneNoOneClicked(): Observable<Unit>
  fun onTelePhoneNoTwoClicked(): Observable<Unit>
  fun showAlertForCall(phoneNumber: String)

  fun reset()

  fun renderSubmit(uiModel: UiModel<String>)
}

class QueryValidator : Validator<String, String>() {
  override fun execute(input: String): ValidationResult<String> {
    val errorMessage: Int? = when {
      input.isEmpty() -> R.string.error_message_empty_query
      else -> null
    }
    return if (errorMessage != null) {
      withError(errorMessage)
    } else {
      withData(
          input
      )
    }
  }
}
