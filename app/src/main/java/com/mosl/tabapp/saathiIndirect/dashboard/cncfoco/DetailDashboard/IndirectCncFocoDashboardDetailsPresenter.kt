package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class IndirectCncFocoDashboardDetailsPresenter @Inject constructor(
    view: IndirectCncFocoDashboardDetailsView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: CncFocoRepository
) : BasePresenter<IndirectCncFocoDashboardDetailsView>(view){
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetCncDashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)

    }
}