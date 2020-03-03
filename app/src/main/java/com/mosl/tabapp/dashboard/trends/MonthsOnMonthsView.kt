package com.mosl.tabapp.dashboard.trends

import com.mosl.app.core.base.BaseView
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import io.reactivex.Observable

interface MonthsOnMonthsView : BaseView {

    fun onWidgetItemClick(): Observable<TrendsWidgetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: DashboardGroupModel, currentPosition: Int)
}