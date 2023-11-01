package com.mosl.tabapp.dashboard.snapshot.advance

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.snapshot.BusinessSnapInput
import com.mosl.mobile.domain.snapshot.BusinessSnapshotRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

class BusinessSnapshotAdvPresenter @Inject constructor(
  view: BusinessSnapshotAdvView,
  val dashboardModel: DashboardModel,
  val navigator: ScreenNavigatorImpl,
  val repository: BusinessSnapshotRepository
) : BasePresenter<BusinessSnapshotAdvView>(view) {
  override fun start() {
    onFetchCalled().switchMap {
      repository.getSnapshot(getWidgetRequest())
        .compose(applyUiModel())
    }
      .subscribe{
        Timber.d("BusinessSnapshotAdvPresenter onFetchCalled saathiApi.getBusinessSnapshot")
        view.renderData(it)}
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

  private fun getWidgetRequest(): BusinessSnapInput {
    return BusinessSnapInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetSystemsName(),
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      additionalParameters = view.getAdditionalParameterMap()
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