package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class IndirectIFAUniqueReportHierchyTablePresenter@Inject constructor(
    view: IndirectIFAUniqueReportHierchyTableView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
): BasePresenter<IndirectIFAUniqueReportHierchyTableView>(view) {
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