package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.DashboardIndirctRepository
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.CCDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCDashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class CCDashboardPresenter @Inject constructor(
    view: CCDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectCCDashboardRepository
) : BasePresenter<CCDashboardView>(view) {
    override fun start() {
            onFetchCalled().switchMap{
                indirectRepo.fnGetCCDashboardFilter().compose(applyUiModel())
            }.subscribe {
                view.fnRenderFilterList(it)
            }.addTo(disposable)

        val periodChangedObs = view.onPeriodChange()
            .filter(InvalidSelectionPredicate)
            .share()

        val advisorChangedObs = view.onAdvisorChange()
            .filter(InvalidSelectionPredicate)
            .share()

        val screenLoadObs = Observable.merge(
            periodChangedObs,
            advisorChangedObs
        )
        screenLoadObs
            .switchMap {
                indirectRepo.getMeetingSummary(getWidgetRequest())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderMeetingSummaryData, Timber::e)
            .addTo(disposable)

        view.onMRSLoadEvent()
            .switchMap {
                indirectRepo.getMeetingReachSummary(getWidgetRequest())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderMeetingReachSummaryData, Timber::e)
            .addTo(disposable)

        view.onLeadCreationClicked()
            .subscribe {
                navigator.openPage(NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_CREATION.toString(), false)
            }
            .addTo(disposable)
    }

    fun getWidgetRequest() : CCDashboardInput {
        return  CCDashboardInput(employeeCode = userManager.getUser()?.employeeCode, fltPeriod = view.getPeriod(), fltAdvisorEmpCode = view.getAdvisor(),  filter = "", calculatedPageP = 0,
            pageSizeP = 20,  pageP = 1, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }

}
