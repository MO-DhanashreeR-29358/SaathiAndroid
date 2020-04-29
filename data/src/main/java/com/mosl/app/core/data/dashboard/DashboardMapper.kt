package com.mosl.app.core.data.dashboard

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.DashboardGroupsData
import com.mosl.mobile.domain.dashboard.Filter
import com.mosl.mobile.domain.dashboard.FilterData
import com.mosl.mobile.domain.dashboard.TableData
import com.mosl.mobile.domain.dashboard.WidgetData
import com.mosl.mobile.domain.dashboard.WidgetType
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableFilterItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableWidgetsItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.GroupsItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.WidgetTypeItem
import java.util.Collections

object DashboardMapper {
  fun transform(list: List<DashboardMetaDataItem>): List<DashboardData> {
    return list.map {
      DashboardData(
          dashboardFor = it.dashboardFor ?: "",
          businessProfileID = it.businessProfileId ?: 0,
          ID = it.id ?: 0,
          name = it.name ?: "",
          groups = transformGroups(it.groups)
      )
    }
  }

  fun transformGroups(list: List<GroupsItem>?): List<DashboardGroupsData> {
    return list!!.map {
      DashboardGroupsData(
          groupName = it.groupName ?: "",
          dashboardGroupId = it.dashBoardGroupId ?: 0,
          imageUrl = it.imageUrl ?: "",
          imageUrl2 = it.imageUrl2 ?: "",
          systemName = it.systemName ?: "",
          availableFilters = transformFilterData(it.availableFilter),
          availableWidgets = transformWidgetData(it.availableWidgets),
          children = transformGroups(it.children)
      )
    }
  }

  fun transformFilterData(list: List<AvailableFilterItem>?): List<FilterData> {
    return list!!.map {
      FilterData(
          filterSysName = it.filterSysName ?: "",
          keyValue = it.keyValue ?: "",
          filter = tranformFilter(it.filterData)
      )
    }
  }

  fun tranformFilter(data: com.mosl.saathi.api.model.dashboard.dashboardmetadata.FilterData): Filter {

    val data = data.table?.let {
      it.safeMap {
        TableData(
            text = it.text ?: "",
            value = it.value ?: ""
        )
      }
    }
    return Filter(
        table = data ?: Collections.emptyList()
    )
  }

  fun transformWidgetData(list: List<AvailableWidgetsItem>?): List<WidgetData> {
    return list!!.map {
      WidgetData(
          imageUrl = it.imageUrl ?: "",
          imageUrl2 = it.imageUrl2 ?: "",
          name = it.name ?: "",
          ID = it.id ?: 0,
          sysName = it.systemName ?: "",
          widgeType = transformWidgetType(it.widgetType),
          filterData = transformFilterData(it.availableFilter)
      )
    }
  }

  fun transformWidgetType(list: List<WidgetTypeItem>?): List<WidgetType> {
    return list!!.map {
      WidgetType(
          widgetType = it.widgetTypes ?: "",
          title = it.title ?: ""
      )
    }
  }
}