package com.mosl.tabapp.login

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import io.reactivex.rxjava3.core.Observable

interface LoginView : BaseView {
  fun onLoginClicked(): Observable<Unit>
  fun onDoneClicked(): Observable<Int>
  fun getEmpCode(): String
  fun getPassword(): String
  fun renderLogin(uiModel: UiModel<User>)
  fun setUserProperty(user: User?)
  fun renderReportsData(uiModel: UiModel<List<ReportType>>)
}