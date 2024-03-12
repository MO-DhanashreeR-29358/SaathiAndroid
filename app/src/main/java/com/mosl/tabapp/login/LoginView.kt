package com.mosl.tabapp.login

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.ValidateLoingResponseModel
import io.reactivex.rxjava3.core.Observable

interface LoginView : BaseView {
  /*fun onLoginClicked(): Observable<Unit>
  fun onDoneClicked(): Observable<Int>*/
  fun getEmpCode(): String
  fun getPassword(): String
  fun getAppVersion() : String
  fun getIsAllowedAllPermission(): Boolean
  fun renderLogin(uiModel: UiModel<User>)
  fun setUserProperty(user: User?)
  fun renderReportsData(uiModel: UiModel<List<ReportType>>)


  fun fnRenderNewLogin(uiModdel:UiModel<ValidateLoingResponseModel>)

  fun fnGenerateOtp():Observable<LoginActivityUiEvent.OnCallGenerateOtp>

  fun renderGenerateOtp(uiModel:UiModel<String>)

  fun fnCallLogin():Observable<LoginActivityUiEvent.OnCallLogin>

  fun fnCallOtpLogin():Observable<LoginActivityUiEvent.OnCallOtpLogin>

  fun fnGetLoginData(): LoginInfo

  fun fnOnOtpSucess():Observable<LoginActivityUiEvent.OnOtpSucess>
}