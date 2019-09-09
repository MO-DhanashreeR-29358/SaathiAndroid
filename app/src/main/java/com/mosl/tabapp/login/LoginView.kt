package com.mosl.tabapp.login

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.user.model.User
import io.reactivex.Observable

interface LoginView : BaseView {
  fun onLoginClicked(): Observable<Unit>
  fun onDoneClicked(): Observable<Int>
  fun getEmpCode(): String
  fun getPassword(): String
  fun renderLogin(uiModel: UiModel<User>)
}