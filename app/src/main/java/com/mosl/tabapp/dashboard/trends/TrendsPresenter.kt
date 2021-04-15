package com.mosl.tabapp.dashboard.trends

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.trends.TrendsInput
import com.mosl.mobile.domain.trends.TrendsRepository
import com.mosl.tabapp.model.dashboard.DashboardModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class TrendsPresenter @Inject constructor(
  view: MonthsOnMonthsView,
  val dashboardModel: DashboardModel,
  val repository: TrendsRepository
) :
  BasePresenter<MonthsOnMonthsView>(view) {
  override fun start() {
    val widgetItemClickObsc = view.onWidgetItemClick().share()

    widgetItemClickObsc
      .subscribe {
        view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
      }.addTo(disposable)

    widgetItemClickObsc
      .filter { view.getDashboardGroupModel() != null }
      .map { getWidgetRequest() }
      .switchMap {
        repository.getTrendsWidget(it)
          .compose(applyUiModel())
      }.subscribe(view::renderData)
      .addTo(disposable)
  }

  private fun getWidgetRequest(): TrendsInput {
    var systemsName: String
    view.getDashboardGroupModel().apply {
      systemsName = this?.widgetList?.get(0)?.systemName ?: ""
    }
    return TrendsInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = systemsName,
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode
    )
  }
}