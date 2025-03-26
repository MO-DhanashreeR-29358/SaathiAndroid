package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class CncFocoDashboardPresenter @Inject constructor(
    view:CncFocoDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo:CncFocoRepository
) :BasePresenter<CncFocoDashboardView>(view){
    override fun start() {

    }
}