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
        view.fnonMeetingEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashMeetingDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
        } .subscribe {
           view.renderData(it)
        }

        view.fnonIfaActivationEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashIFAActivationDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonRevenuIpEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashRevenueIPDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonRevenuBroEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetIFASalesDashRevenueBrokingDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }



        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)

        view.onFilterCategoryEventCalled().subscribe {
            view.fnRenderFilterCategoryEventCalled(it.strFilterCatText)
        }.addTo(disposable)
    }
}