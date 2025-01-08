package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFAMyLeadPresenter @Inject constructor(
    view: IndirectIFAMyLeadView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFAMyLeadView>(view) {
    override fun start() {
        onFetchCalled().map {
            view.fngetFilterValue()
        }.switchMap {
                indirectRepo.fnGetIFaSalesDashMyLead(it).compose(applyUiModel())
        } .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }


    }
}