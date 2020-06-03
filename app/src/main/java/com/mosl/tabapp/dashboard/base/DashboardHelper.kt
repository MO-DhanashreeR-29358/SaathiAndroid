package com.mosl.tabapp.dashboard.base

import com.mosl.mobile.domain.dashboard.DashboardGroupsData
import com.mosl.mobile.domain.dashboard.FilterData
import com.mosl.mobile.domain.dashboard.TableData
import com.mosl.mobile.domain.dashboard.WidgetData
import com.mosl.mobile.domain.dashboard.WidgetType
import com.mosl.shared.util.Utility.stringOrEmpty
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel

fun getGroupModelFromJsonArray(groupsItems: List<DashboardGroupsData>): List<DashboardGroupModel> {
  val groupList = ArrayList<DashboardGroupModel>()


  for (liX in groupsItems.indices) {
    val item = groupsItems.get(liX)
    if (item != null) {
      val groupModel = DashboardGroupModel()
      groupList.add(groupModel)
      val dashBordGroupId = item.dashboardGroupId.toString()
      val groupName = item.groupName
      val groupSystemName = item.systemName
      val imageUrl1 = item.imageUrl
      val imageUrl2 = item.imageUrl2
      groupModel.id = dashBordGroupId
      groupModel.displayName = groupName
      groupModel.systemName = groupSystemName
      groupModel.iconUrl1 = imageUrl1
      groupModel.iconUrl2 = imageUrl2
      val selected = liX == 0
      groupModel.isSelected = selected
      val childrenGroupItems = item.children
      if (childrenGroupItems != null && childrenGroupItems.size > 0) {
        val childrenGroup =
          getGroupModelFromJsonArray(childrenGroupItems)
        groupModel.subGroupList
            .addAll(childrenGroup)
      }
      val availableWidgets = item.availableWidgets
      val widgetModelList: List<WidgetModel> =
        getWidgetModelFromAvailableWidgets(availableWidgets)
      groupModel.widgetList
          .addAll(widgetModelList)
      //filter list
      groupModel.groupFilterList = getFilterList(item.availableFilters)
    }
  }
  return groupList
}

fun getWidgetModelFromAvailableWidgets(
  widgetsItems: List<WidgetData>
): List<WidgetModel> {
  val widgetModelList: MutableList<WidgetModel> =
    java.util.ArrayList()
  for (ctr in widgetsItems.indices) {
    val item = widgetsItems[ctr]
    if (item != null) {
      val widgetModel = WidgetModel()
      widgetModelList.add(widgetModel)
      val id: String = stringOrEmpty(item.ID.toString())
      val displayName: String = stringOrEmpty(item.name)
      val systemName: String = stringOrEmpty(item.sysName)
      val iconUrl1: String = stringOrEmpty(item.imageUrl)
      val iconUr2: String = stringOrEmpty(item.imageUrl2)
      val widgetTypeItems = item.widgeType
      if (widgetTypeItems != null) {
        val childrenGroup =
          getWidgetTypeFromAvailableWidgets(widgetTypeItems)
        widgetModel.widgetTypeList = childrenGroup
      }
      val selected = ctr == 0
      widgetModel.isSelected = selected
      widgetModel.id = id
      widgetModel.name = displayName
      widgetModel.systemName = systemName
      widgetModel.iconUrl1 = iconUrl1
      widgetModel.iconUrl2 = iconUr2
      widgetModel.widgetFilterList = getFilterList(item.filterData)
      widgetModel.previousSelectedPosition = ctr
    }
  }
  return widgetModelList
}

fun getFilterList(availableFilterItems: List<FilterData>): List<WidgetFilterModel>? {
  val filterList = ArrayList<WidgetFilterModel>()
  try {
    if (availableFilterItems != null) {
      for (filterCount in availableFilterItems.indices) {
        val filterRowList = ArrayList<FilterRowData>()
        val item = availableFilterItems.get(filterCount)
        val filterSysName1 = item.filterSysName
        val keyValue1 = item.keyValue
        val filter = item.filter
        if (filter != null) {
          if (filter.table != null) {
            val filterRowArray: List<TableData> = filter.table
            for (filterRowCount in filterRowArray.indices) {
              val rowObj = filterRowArray.get(filterRowCount)
              val text = rowObj.text
              val value = rowObj.value
              val rowData = FilterRowData(filterRowCount, text, value, keyValue1)
              filterRowList.add(rowData)
            }
          }
        }
        val filterModel =
          WidgetFilterModel(filterSysName1, keyValue1, filterRowList)
        filterList.add(filterModel)
      }
    }
    ////
  } catch (ex: Exception) {
    ex.printStackTrace()
  }
  return filterList
}

fun getWidgetTypeFromAvailableWidgets(widgetTypeItems: List<WidgetType>): List<com.mosl.tabapp.model.dashboard.WidgetType> {
  val widgetModelList =
    java.util.ArrayList<com.mosl.tabapp.model.dashboard.WidgetType>()
  for (ctr in widgetTypeItems.indices) {
    val item = widgetTypeItems[ctr]
    if (item != null) {
      val widgetTypes = stringOrEmpty(item.widgetType)
      val title = stringOrEmpty(item.title)
      widgetModelList.add(com.mosl.tabapp.model.dashboard.WidgetType(widgetTypes, title))
    }
  }
  return widgetModelList
}