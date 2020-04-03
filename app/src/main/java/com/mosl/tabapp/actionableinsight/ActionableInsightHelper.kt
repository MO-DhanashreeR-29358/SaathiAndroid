package com.mosl.tabapp.actionableinsight

import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel

fun getFilterList(
  list: ArrayList<WidgetModel>,
  systemName: String
): ArrayList<WidgetFilterModel> {
  for (widgetModel in list) {
    if (systemName.equals(widgetModel.systemName)) {
      return widgetModel.widgetFilterList as ArrayList<WidgetFilterModel>
    }
  }
  return ArrayList()
}

fun getSpinnerList(
  list: ArrayList<WidgetModel>,
  systemName: String
): ArrayList<TypeActionableFilterModel> {
  for (widgetModel in list) {
    if (systemName.equals(widgetModel.systemName)) {
      return widgetModel.spinnerFilterList as ArrayList<TypeActionableFilterModel>
    }
  }
  return ArrayList()
}