package com.mosl.tabapp.actionableinsight

import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel

data class ActionableInsightData(
  @JvmField val list: ArrayList<WidgetModel>,
  @JvmField val paramVal: String
)

data class ActionableInsightDetailData(
  @JvmField val data: WidgetModel,
  @JvmField val filterList: ArrayList<WidgetFilterModel>,
  @JvmField val spinnerList: ArrayList<TypeActionableFilterModel>,
  @JvmField val paramVal: String
)