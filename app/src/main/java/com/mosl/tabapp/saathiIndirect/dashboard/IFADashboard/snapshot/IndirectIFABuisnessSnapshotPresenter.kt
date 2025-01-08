package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFABuisnessSnapshotPresenter @Inject constructor(
    view: IndirectIFABuisnessSnapshotView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFABuisnessSnapshotView>(view) {
    override fun start() {
        onFetchCalled()
            .map {
                view.fngetFilterValue()
            }.switchMap {
            indirectRepo.fnGetIFASalesDashboBuisnessSnapshot(it).compose(applyUiModel())
        }  .subscribe(view::renderData, Timber::e)
            .addTo(disposable)


        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }


    }
}