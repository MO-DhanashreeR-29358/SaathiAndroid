package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFABuisnessSnapshotIpPresenter @Inject constructor(
    view: IndirectIFABuisnessSnapshotIpView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
) :BasePresenter<IndirectIFABuisnessSnapshotIpView>(view){
    override fun start() {

        onFetchCalled()
            .map {
                view.fngetFilterValue()
            }.switchMap {
                indirectRepo.fnGetIFASalesDashMyBuisnessSnapIP(it).compose(applyUiModel())
            }  .subscribe(view::renderData, Timber::e)
            .addTo(disposable)


        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)

    }
}