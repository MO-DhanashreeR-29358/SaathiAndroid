package com.mosl.tabapp.dashboard.snapshot

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.snapshot.BusinessSnapInput
import com.mosl.mobile.domain.snapshot.BusinessSnapshotRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import javax.inject.Inject

class BusinessSnapshotPresenter @Inject constructor(
  view: BusinessSnapshotView,
  val dashboardModel: DashboardModel,
  private val repository: BusinessSnapshotRepository,
  val navigator: ScreenNavigatorImpl
) : BasePresenter<BusinessSnapshotView>(view) {
  override fun start() {
    Observable.merge(
      view.onFilter1Change(),
      view.onFilter2Change(), view.onFilter3Change()
    )
      .filter(InvalidSelectionPredicate)
      .skip(1)
      .switchMap {
        repository.getDashboardData(getWidgetRequest())
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it)
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): BusinessSnapInput {
    return BusinessSnapInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetSystemsName(),
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      additionalParameters = view.getAdditionalParameterMap(null, null)
    )
  }

  private fun navigationFlow(data: MutableMap<String, String>) {
    val args = Bundle()
    args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
    args.putString(MOSLConstants.LABEL, MOSLConstants.BUSINESS_SNAPSHOT_TITLE)
    args.putSerializable(MOSLConstants.MAP, data as Serializable?)
    navigator.openPage(
        NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD.toString(),
        args,
        addToStack = true,
        forceReload = false
    )
  }
}