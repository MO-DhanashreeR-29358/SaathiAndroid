package com.mosl.tabapp.core

import android.widget.Toast
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.mobile.user.login.UserManager
import javax.inject.Inject

open class CommonFragment : BaseFragment() {
  @Inject
  internal lateinit var userManager: UserManager

  protected fun showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
  }
}