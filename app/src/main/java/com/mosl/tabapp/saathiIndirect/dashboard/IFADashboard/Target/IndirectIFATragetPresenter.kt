package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import timber.log.Timber
import javax.inject.Inject

class IndirectIFATragetPresenter @Inject constructor(
    view: IndirectIFATargetView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFATargetView>(view) {
    override fun start() {

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashMeeting(getWidgetRequest()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)

        val widgetItemClickObsc = view.onWidgetItemClick().share()

        widgetItemClickObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectIFATargetView.strMeeting }
            .switchMap {
                indirectRepo.fnGetIFADashMeeting(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectIFATargetView.strIFAActivation }
            .switchMap {
                indirectRepo.fnGetIFADashIFAACtivation(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectIFATargetView.strReveIP }
            .switchMap {
                indirectRepo.fnGetIFADashREvenueIP(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IndirectIFATargetView.strReveBro }
            .switchMap {
                indirectRepo.fnGetIFADashRevenueBroking(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

    }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = view.fngetFilterValue(), calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0, fitHierarchy = "Team" )
    }
}