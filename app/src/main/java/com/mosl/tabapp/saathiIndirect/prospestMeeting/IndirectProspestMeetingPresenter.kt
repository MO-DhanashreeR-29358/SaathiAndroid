package com.mosl.tabapp.saathiIndirect.prospestMeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class IndirectProspestMeetingPresenter @Inject constructor(
    view:IndirectProspestMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
):BasePresenter<IndirectProspestMeetingView>(view) {

    override fun start() {

    }
}