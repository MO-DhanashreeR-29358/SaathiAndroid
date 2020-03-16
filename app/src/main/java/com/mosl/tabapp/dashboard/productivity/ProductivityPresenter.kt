package com.mosl.tabapp.dashboard.productivity

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.productivity.ProductivityAdditionalParametersInput
import com.mosl.mobile.domain.productivity.ProductivityInput
import com.mosl.mobile.domain.productivity.ProductivityRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment.Companion.PRODUCTIVITY
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import javax.inject.Inject

class ProductivityPresenter @Inject constructor(
  view: productivityView,
  val dashboardModel: DashboardModel,
  private val repository: ProductivityRepository,
  val navigator: ScreenNavigatorImpl
) : BasePresenter<productivityView>(view) {
  override fun start() {

    onFetchCalled().switchMap {
        repository.getDashboardData(getWidgetRequest())
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onShowMoreClick().subscribe {
      navigationFlow()
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): ProductivityInput {
    return ProductivityInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetSystemsName(),
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      additionalParameters = ProductivityAdditionalParametersInput(
        fltHierarchy = dashboardModel.dashboardFor
      )
    )
  }

  private fun navigationFlow() {
    val data: MutableMap<String, String> = mutableMapOf()
    data.putAll(view.getAdditionalParameterMap())
    val args = Bundle()
    args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
    args.putString(MOSLConstants.LABEL, PRODUCTIVITY)
    args.putSerializable(MOSLConstants.MAP, data as Serializable?)
    navigator.openPage(
      NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
      args,
      addToStack = true,
      forceReload = false
    )
  }
}