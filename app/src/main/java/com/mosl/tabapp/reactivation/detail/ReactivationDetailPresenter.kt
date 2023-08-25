package com.mosl.tabapp.reactivation.detail

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import timber.log.Timber
import javax.inject.Inject

class ReactivationDetailPresenter @Inject constructor(
    view: ReactivationDetailView,
    val repository: ReactivationRepository,
    val userManager: UserManager
) : BasePresenter<ReactivationDetailView>(view) {

    override fun start() {


    }
}