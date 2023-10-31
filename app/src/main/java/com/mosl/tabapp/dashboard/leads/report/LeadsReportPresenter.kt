package com.mosl.tabapp.dashboard.leads.report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.lead.report.LeadsReportInput
import com.mosl.mobile.domain.lead.report.LeadsReportRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import java.io.Serializable
import java.util.HashMap
import javax.inject.Inject

class LeadsReportPresenter @Inject constructor(
  view: LeadsReportView,
  val dashboardModel: DashboardModel,
  val navigator: ScreenNavigatorImpl,
  val userManager: UserManager,
  val repository: LeadsReportRepository

) : BasePresenter<LeadsReportView>(view) {
  override fun start() {

    onFetchCalled().switchMap {
        repository.getLeadWidget(getWidgetRequest()).compose(
          applyUiModel()
        )
      }.subscribe{
      Timber.d("LeadsReportPresenter onFetchCalled saathiApi.getLeadReportWidgetData")
      view.renderData(it)}
      .addTo(disposable)

    view.cellEvent().subscribe {
      toNavigate(it.additionalparametersmap)
    }.addTo(disposable)

  }

  fun toNavigate(map: Map<String, String>) {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()
    addtionalParameterMap.putAll(map)
    addtionalParameterMap.putAll(view.getAddtionalParameterMap())
    val args = Bundle()
    args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
    args.putString(MOSLConstants.LABEL, MOSLConstants.LEAD)
    args.putSerializable(MOSLConstants.MAP, addtionalParameterMap as Serializable)
    navigator.openPage(
        NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD.toString(), args,
        true, false
    )
  }

  private fun getWidgetRequest(): LeadsReportInput {
    return LeadsReportInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetSystemsName(),
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      addtionalParameters = view.getAddtionalParameterMap()
    )
  }
}