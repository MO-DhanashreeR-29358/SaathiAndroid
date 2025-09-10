package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import timber.log.Timber
import javax.inject.Inject

class IndirectCncfocoTragetPresenter @Inject constructor(
    view:IndirectCncTargetView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: CncFocoRepository
):BasePresenter<IndirectCncTargetView>(view) {
    override fun start() {

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)

        onFetchCalled().switchMap {
            indirectRepo.fngetCNCFocoDashRevenueWidgetData(getWidgetRequest()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)

       val widgetItemClickObsc = view.onWidgetItemClick().share()

        widgetItemClickObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectCncTargetView.strReve }
            .switchMap {
                indirectRepo.fngetCNCFocoDashRevenueWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectCncTargetView.strNoOfClient }
            .switchMap {
                indirectRepo.fngetCNCFocoDashNoOfClientMeetingWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)



    }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = view.fngetFilterValue(), calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0, fitHierarchy = "Team" )
    }
    companion object {
        const val REVENUE_GROWTH_DEGROWTH = "Revenue Growth/Degrowth"
    }
}