package com.mosl.tabapp.dashboard.leads.report

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.lead.report.LeadsReportInput
import com.mosl.mobile.domain.lead.report.LeadsReportRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
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
      }.subscribe(view::renderData)
      .addTo(disposable)

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