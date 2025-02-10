package com.mosl.tabapp.advancesearch.advanceSearch

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class AdvanceSearchTabPresenter @Inject constructor(
    view: AdvanceSearchTabView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
): BasePresenter<AdvanceSearchTabView>(view) {
    override fun start() {

    }
}