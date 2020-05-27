package com.mosl.mobile.domain.dashboard

data class DashboardData(
  @JvmField val dashboardFor: String,
  @JvmField val name: String,
  @JvmField val businessProfileID: Int,
  @JvmField val ID: Int,
  @JvmField val groups: List<DashboardGroupsData>
)

data class DashboardGroupsData(
  @JvmField val groupName: String,
  @JvmField val systemName: String,
  @JvmField val imageUrl2: String,
  @JvmField val imageUrl: String,
  @JvmField val dashboardGroupId: Int,
  @JvmField val availableFilters: List<FilterData>,
  @JvmField val availableWidgets: List<WidgetData>,
  @JvmField val children: List<DashboardGroupsData>
)

data class FilterData(
  @JvmField val filterSysName: String,
  @JvmField val keyValue: String,
  @JvmField val filter: Filter
)

data class Filter(
  @JvmField val table: List<TableData>
)

data class TableData(
  @JvmField val text: String,
  @JvmField val value: String
)

data class WidgetData(
  @JvmField val imageUrl2: String,
  @JvmField val imageUrl: String,
  @JvmField val sysName: String,
  @JvmField val name: String,
  @JvmField val ID: Int,
  @JvmField val filterData: List<FilterData>,
  @JvmField val widgeType: List<WidgetType>
)

data class WidgetType(
  @JvmField val widgetType: String,
  @JvmField val title: String
)