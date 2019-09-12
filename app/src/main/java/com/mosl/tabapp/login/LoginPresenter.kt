package com.mosl.tabapp.login

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.saathi.core.LoginNavigator
import com.mosl.mobile.user.login.LoginRepository
import io.reactivex.Observable
import javax.inject.Inject

class LoginPresenter @Inject constructor(
  view: LoginView,
  val navigator: LoginNavigator,
  val context: LoginActivityNew,
  val loginRepository: LoginRepository
) : BasePresenter<LoginView>(view) {

  override fun start() {
    disposable.add(Observable.merge(view.onLoginClicked(), view.onDoneClicked())
        .map<LoginFormInput> {
          LoginFormInput(
              view.getEmpCode(), view.getPassword()
          )
        }
        .switchMap { input ->
          LoginValidator.validate(input)
              .switchMap {
                loginRepository.login(it)
                    .compose(applyUiModel())
              }
        }.subscribe {
          view.renderLogin(it)

          if (it.isSuccess) {
            disposable.add(loginRepository.fetchReportTypes().compose(applyUiModel())
                .subscribe {
                  view.renderReportsData(it)
                }
            )
          }
//          it.onSuccess {
//            navigator.openDashboard()
//          }

          /*it.onFailure { error ->
            if (error is LoginException) {
              ekycNavigator.openNewEkycApp(true)
            }
          }*/
        }
    )
  }

}