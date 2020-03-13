package com.mosl.tabapp.dashboard.trends

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.trends.TrendsChartModel
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import io.reactivex.Observable

interface MonthsOnMonthsView : BaseView {

    fun onWidgetItemClick(): Observable<TrendsWidgetUiEvent.ItemClick>
    fun renderWidgetData(dashboardGroupModel: DashboardGroupModel, currentPosition: Int)
    fun getDashboardGroupModel(): DashboardGroupModel?
  fun renderData(uiModel: UiModel<List<TrendsChartModel>>)
}