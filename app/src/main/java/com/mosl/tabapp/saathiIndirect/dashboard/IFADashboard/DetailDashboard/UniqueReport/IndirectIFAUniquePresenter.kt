package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import timber.log.Timber
import javax.inject.Inject

class IndirectIFAUniquePresenter  @Inject constructor(
    view: IndirectIFAUniqueView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
): BasePresenter<IndirectIFAUniqueView>(view) {
    override fun start() {

        view.onFilterEventCalled().subscribe {
            view.fnRenderFilterEventCalled(it.strFilterText)
        }.addTo(disposable)


        view.fnonMeetingEventCalled().switchMap {
                indirectRepo.fnGetIFASalesDashMeetingTeamUniqueGraph(getWidgetRequest()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)


        val widgetItemClickObsc = view.onWidgetItemClick().share()

        widgetItemClickObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "1" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashMeetingTeamUniqueGraph(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "2" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashMeetingTargetMetNotMetGraph(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "3" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashMeetingMyUniqueGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        view.fnonIfaActivationEventCalled().switchMap {
            indirectRepo.fnGetIFASalesDashIFAActivationProductWiseGraph(getWidgetRequestReveIp()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)


        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "4" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashIFAActivationProductWiseGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "5" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashIFAActivationBranchWiseGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)
        view.fnonRevenuIpEventCalled().switchMap {
indirectRepo.fnGetIFASalesDashRevenueIPProductWiseSalesGraph(getWidgetRequestReveIp()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "6" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueIPProductWiseSalesGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "7"  }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueIPBranchWiseGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "8" }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueIPTargetMetNotMetGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)



        view.fnonRevenuBroEventCalled()
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueBrokingProductWiseSalesGraph(getWidgetRequestReveIp()).compose(applyUiModel())
            }.subscribe {
                view.renderData(it)
            }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "9"  }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueBrokingProductWiseSalesGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.id  == "10"  }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueBrokingBranchWiseGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter {it.widgetItemInfo.id  == "11"  }
            .switchMap {
                indirectRepo.fnGetIFASalesDashRevenueIPTargetMetNotMetGraph(getWidgetRequest()).compose(applyUiModel())
            }.subscribe(view::renderData, Timber::e)
            .addTo(disposable)




    }
    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0, fitHierarchy = "Team" , category = "Achieved")
    }

    fun getWidgetRequestReveIp() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0, fitHierarchy = "Team" )
    }
}