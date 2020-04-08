package com.mosl.tabapp.dashboard.actionableinsight

import com.mosl.app.core.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo

import javax.inject.Inject

class ActionableInsightPresenter @Inject constructor(
  val insightData: ActionableInsightData,
  val navigator: ActionableInsightNavigator,
  view: ActionableInsightView
) : BasePresenter<ActionableInsightView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .map { insightData.list }
        .subscribe(view::render)
        .addTo(disposable)

    view.onCloseClicked()
        .subscribe {
          view.dismissDialog()
        }
        .addTo(disposable)

    view.onActionableInsightItemClick()
      .filter { !it.widgetModel.actionableCount.equals("0") }
        .subscribe {
          view.dismissDialog()
          navigator.navigateToSecondLevelDashboard(
              ActionableInsightDetailData(
                  it.widgetModel, getFilterList(insightData.list, it.widgetModel.systemName),
                  getSpinnerList(insightData.list, it.widgetModel.systemName), insightData.paramVal
              )
          )
        }
        .addTo(disposable)
  }
}