package com.mosl.tabapp.login

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.user.login.LoginRepository
import com.mosl.mobile.user.model.GenerateOtpPostModel
import com.mosl.tabapp.core.LoginNavigator
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    view: LoginView,
    val navigator: LoginNavigator,
    val context: LoginActivity,
    val loginRepository: LoginRepository
) : BasePresenter<LoginView>(view) {

  override fun start() {
      view.fnCallLogin()
          .map<LoginFormInput> {
          LoginFormInput(
              view.getEmpCode(), view.getPassword(), view.getIsAllowedAllPermission(),view.getAppVersion()
          )
      }
          .switchMap { input ->
              LoginValidator.validate(input)
                  .flatMap {
                      loginRepository.login(it)
                  }.compose(applyUiModel())
          }.subscribe {
              view.renderLogin(it)

              if (it.isSuccess) {
                  view.setUserProperty(it.data)
                  disposable.add(loginRepository.fetchReportTypes().compose(applyUiModel())
                      .subscribe {
                          view.renderReportsData(it)
                      }
                  )
              }
          }.addTo(disposable)

     view.fnCallOtpLogin()
         .map<LoginFormInput> {
             LoginFormInput(
                 view.getEmpCode(),
                 view.getPassword(),
                 view.getIsAllowedAllPermission(),
                 view.getAppVersion()
             )
         }
          .switchMap {
                  input ->
              LoginValidator.validate(input)
                  .flatMap {
                      loginRepository.fnValidateLogin(it)
                  }.compose(applyUiModel())
          }.subscribe{
            view.fnRenderNewLogin(it)
         }.addTo(disposable)


      view.fnOnOtpSucess()
          .switchMap {
              view.setUserProperty(it.user)
              loginRepository.fetchReportTypes().compose(applyUiModel())
          } .subscribe {
              view.renderReportsData(it)
          }.addTo(disposable)


    /*disposable.add(
        Observable.merge(view.onLoginClicked(), view.onDoneClicked())
        .map<LoginFormInput> {
          LoginFormInput(
              view.getEmpCode(), view.getPassword(), view.getIsAllowedAllPermission(),view.getAppVersion()
          )
        }
        .switchMap { input ->
          LoginValidator.validate(input)
            .flatMap {
                loginRepository.login(it)
            }.compose(applyUiModel())
        }.subscribe {
          view.renderLogin(it)

          if (it.isSuccess) {
            view.setUserProperty(it.data)
            disposable.add(loginRepository.fetchReportTypes().compose(applyUiModel())
                .subscribe {
                  view.renderReportsData(it)
                }
            )
          }
      }
    )*/
  }

}