package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.UiModel
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import com.mosl.mobile.domain.target.AditionalParametersTarget
import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class FocoTargetPresenter @Inject constructor(
    view: FocoTargetView,
    val indirectRepo: FocoRepository,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) :
    BasePresenter<FocoTargetView>(view) {
    override fun start() {

        onFetchCalled().switchMap{
            indirectRepo.getFocoDashRevenueWidgetData(getWidgetRequest()).compose(applyUiModel())
        }.subscribe {
            view.renderData(it)
        }.addTo(disposable)

        val widgetItemClickObsc = view.onWidgetItemClick().share()

        widgetItemClickObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == REVENUE }
            .switchMap {
                indirectRepo.getFocoDashRevenueWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == REVENUE_GROWTH_DEGROWTH }
            .switchMap {
                indirectRepo.getFocoDashRevenueGrowthWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderBarChartData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == NO_OF_CLIENT_MEETINGS }
            .switchMap {
                indirectRepo.getFocoDashNoOfClientsWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == ONLINE_REVENUE }
            .switchMap {
                indirectRepo.getFocoDashOnlineRevenueWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)
        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == CLIENT_REACH }
            .switchMap {
                indirectRepo.getFocoDashClientReachWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)
        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == IP_SALES }
            .switchMap {
                indirectRepo.getFocoDashIPSalesWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        widgetItemClickObsc
            .filter { it.widgetItemInfo.name  == TOTAL_MOB_APP }
            .switchMap {
                indirectRepo.getFocoDashTotalMobAppWidgetData(getWidgetRequest()).compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)
    }
    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", category = "Existing", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }
    companion object {
        const val REVENUE = "Revenue"
        const val REVENUE_GROWTH_DEGROWTH = "Revenue Growth/Degrowth"
        const val NO_OF_CLIENT_MEETINGS = "No. of Client Meetings"
        const val ONLINE_REVENUE = "Online Revenue"
        const val CLIENT_REACH = "Client Reach"
        const val IP_SALES = "IP Sales"
        const val TOTAL_MOB_APP = "Total mob app Login and 1st trade done"

    }

}