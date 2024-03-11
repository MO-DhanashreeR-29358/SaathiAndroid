package com.mosl.tabapp.login

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.user.login.LoginRepository
import com.mosl.mobile.user.model.GenerateOtpPostModel
import com.mosl.mobile.user.model.ValidateLoginPostModel
import com.mosl.tabapp.core.LoginNavigator
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    view: LoginView,
    val navigator: LoginNavigator,
    val context: LoginActivity,
    val loginRepository: LoginRepository
) : BasePresenter<LoginView>(view) {

  override fun start() {
     view.onLoginClicked()
          .switchMap {
              loginRepository.fnValidateLogin(ValidateLoginPostModel(
                  ipaddress = "192.168.62.99",  isPersistance = false, employeeCode = "10640", macaddress = "123456789",
                 appId = 2,clientinfo = "IE 1234567",password = "mosl_123"
              )).compose(applyUiModel())
          }.subscribe{
            view.fnRenderNewLogin(it)
         }

      view.fnGenerateOtp().
              switchMap {
                  loginRepository.fnCallOtp(GenerateOtpPostModel(
                      ipaddress = "192.168.62.99",  isPersistance = false, employeeCode = "10640", macaddress = "123456789",
                      appId = 2,clientinfo = "IE 1234567",password = "mosl_123"
                  )).compose(applyUiModel())
              }.subscribe {
                  view.renderGenerateOtp(it)
      }

    disposable.add(Observable.merge(view.onLoginClicked(), view.onDoneClicked())
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
    )
  }

}