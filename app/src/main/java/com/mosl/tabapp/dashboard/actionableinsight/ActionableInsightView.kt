package com.mosl.tabapp.dashboard.actionableinsight

import com.mosl.app.core.base.BaseView
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightEvent.OnItemClicked
import com.mosl.tabapp.model.dashboard.WidgetModel

interface ActionableInsightView : BaseView {
  fun render(data: List<WidgetModel>)
    fun onCloseClicked(): io.reactivex.rxjava3.core.Observable<Unit>
    fun dismissDialog()
    fun onActionableInsightItemClick(): io.reactivex.rxjava3.core.Observable<OnItemClicked>
}