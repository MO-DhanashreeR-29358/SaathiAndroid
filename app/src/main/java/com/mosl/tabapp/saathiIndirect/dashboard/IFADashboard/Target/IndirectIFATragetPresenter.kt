package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectIFATragetPresenter @Inject constructor(
    view: IndirectIFATargetView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFATargetView>(view) {
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)

    }
}