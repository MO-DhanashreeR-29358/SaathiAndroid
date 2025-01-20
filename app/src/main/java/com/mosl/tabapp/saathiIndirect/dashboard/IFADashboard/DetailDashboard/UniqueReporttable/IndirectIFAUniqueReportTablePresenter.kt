package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable

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

class IndirectIFAUniqueReportTablePresenter @Inject constructor(
    view: IndirectIFAUniqueReportTableView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
): BasePresenter<IndirectIFAUniqueReportTableView>(view) {
    override fun start() {
        onFetchCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashRevenueBrokingHierarchy(it).compose(applyUiModel())
        } .subscribe {
            it.onSuccess {
                Log.d("table Response ",it)
            }
            it.onFailure {
                Log.d("table Response f",it.message.toString())
            }
        }

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }
    }
}