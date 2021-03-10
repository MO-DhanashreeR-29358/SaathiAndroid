package com.mosl.tabapp.dashboard.tradeyesterday

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.clients.APClientsInput
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayInput
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import javax.inject.Inject

class ClientsTradedYesterdayPresenter @Inject constructor(
  view: ClientsTradedYesterdayView,
  val dashboardModel: DashboardModel,
  val navigator: ScreenNavigatorImpl,
  val repository: ClientTradeYesterdayRepository
) : BasePresenter<ClientsTradedYesterdayView>(view) {
  override fun start() {
    onFetchCalled().switchMap {
      repository.getClientsTradedYesterday(getWidgetRequest())
        .compose(applyUiModel())
    }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onDataEvent().filter {
      view.getClientList().size > 0
    }.subscribe {
      view.renderUI()
    }.addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it)
    }.addTo(disposable)
  }

  private fun navigationFlow(data: MutableMap<String, String>) {
    val args = Bundle()
    args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
    args.putString(MOSLConstants.LABEL, view.getGroupName())
    args.putSerializable(MOSLConstants.MAP, data as Serializable?)
    navigator.openPage(
        NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD.toString(),
        args,
        addToStack = true,
        forceReload = false
    )
  }

  private fun getWidgetRequest(): ClientTradeYesterdayInput {
    return ClientTradeYesterdayInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetSystemsName(),
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      additionalParameters = APClientsInput(
        fltPeriod = MOSLConstants.SELF_KEY,
        fltHierarchy = dashboardModel.dashboardFor
      )
    )
  }
}