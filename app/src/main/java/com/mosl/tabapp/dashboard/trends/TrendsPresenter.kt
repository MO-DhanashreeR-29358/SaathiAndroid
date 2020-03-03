package com.mosl.tabapp.dashboard.trends

import com.mosl.app.core.base.BasePresenter
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class TrendsPresenter @Inject constructor(
    view: MonthsOnMonthsView
) :
    BasePresenter<MonthsOnMonthsView>(view) {
    override fun start() {
        val widgetItemClickObsc = view.onWidgetItemClick().share()

        widgetItemClickObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)
    }
}