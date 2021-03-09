package com.mosl.tabapp.query

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.query.QueryRepository
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.query.QueryFragment.Companion.TELE_PHONE_1
import com.mosl.tabapp.query.QueryFragment.Companion.TELE_PHONE_2
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class QueryPresenter @Inject constructor(
  view: QueryView,
  val repository: QueryRepository,
  val navigator: EkycNavigator
) : BasePresenter<QueryView>(view) {
  protected val validator: QueryValidator = QueryValidator()
  override fun start() {

    view.onSubmitBtnClick()
        .map {
          view.getFormInput()
        }
        .switchMap { input ->
          validator.validate(input)
              .compose(applyUiModel())
        }
        .subscribe { view.onSubmitDialogOpen(it) }
        .addTo(disposable)


    Observable.merge(view.onSubmitConfirmClicked(), onRetryCalled())
        .map { view.getQueryMessgae() }
        .switchMap {
          repository.getUserInsertQuery(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderSubmit)
        .addTo(disposable)




    view.onResetBtnClick()
        .subscribe { view.reset() }
        .addTo(disposable)

    view.onTelePhoneNoOneClicked()
        .subscribe {
          view.showAlertForCall(TELE_PHONE_1)
        }
        .addTo(disposable)

    view.onTelePhoneNoTwoClicked()
        .subscribe {
          view.showAlertForCall(TELE_PHONE_2)
        }
        .addTo(disposable)

    view.onEsignClick()
        .subscribe { navigator.openEkyc() }
        .addTo(disposable)
  }
}

fun QueryView.getFormInput(): String {
  return getQueryMessgae()
}

sealed class QueryUiEvent {
  object OnSubmitConfirmClick : QueryUiEvent()
}