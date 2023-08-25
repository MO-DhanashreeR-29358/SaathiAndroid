package com.mosl.tabapp.reactivation

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.mobile.user.login.UserManager
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class ReactivationSummaryPresenter @Inject constructor(
    view: ReactivationSummaryView,
    val userManager: UserManager
) : BasePresenter<ReactivationSummaryView>(view) {

    override fun start() {



    }
}