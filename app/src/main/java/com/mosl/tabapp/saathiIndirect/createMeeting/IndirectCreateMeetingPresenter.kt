package com.mosl.tabapp.saathiIndirect.createMeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class IndirectCreateMeetingPresenter @Inject constructor(
    view:IndirectCreateMeetingView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) :BasePresenter<IndirectCreateMeetingView>(view){
    override fun start() {

    }
}