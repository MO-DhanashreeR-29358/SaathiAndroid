package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
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

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)


        view.fnonMeetingEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashMeetingHierarchy(it).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonIfaActivationEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashIFAActivationHierarchy(it).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonRevenuIpEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashRevenueIPHierarchy(it).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonRevenuBroEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashRevenueBrokingHierarchy(it).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }
    }
}