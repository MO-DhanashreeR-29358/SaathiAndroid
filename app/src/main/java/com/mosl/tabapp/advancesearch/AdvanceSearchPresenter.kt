package com.mosl.tabapp.advancesearch

import com.mosl.app.core.base.BasePresenter
import javax.inject.Inject
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl

class AdvanceSearchPresenter @Inject constructor(
    view: AdvanceSearchView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
): BasePresenter<AdvanceSearchView>(view) {
    override fun start() {

    }
}