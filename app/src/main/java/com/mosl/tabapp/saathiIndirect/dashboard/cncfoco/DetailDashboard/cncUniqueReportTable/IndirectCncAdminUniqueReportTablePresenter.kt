package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl

import javax.inject.Inject

class IndirectCncAdminUniqueReportTablePresenter @Inject constructor(
    view: IndirectCncAdminUniqueReportTableView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: CncFocoRepository
) : BasePresenter<IndirectCncAdminUniqueReportTableView>(view){
    override fun start() {

        view.fnonMeetingEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetCNCFocoDashMeetingDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
        } .subscribe {
            view.renderData(it)
        }

        view.fnonRevenuBroEventCalled().map {
            view.fngetFilterValue()
        }.switchMap {
            indirectRepo.fnGetCNCFocoDashRevenueDetail(it,view.fngetFilterProductValue()).compose(applyUiModel())
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