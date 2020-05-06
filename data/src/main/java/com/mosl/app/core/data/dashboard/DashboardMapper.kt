package com.mosl.app.core.data.dashboard

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.dashboard.ActionableCount
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.DashboardGroupsData
import com.mosl.mobile.domain.dashboard.Filter
import com.mosl.mobile.domain.dashboard.FilterData
import com.mosl.mobile.domain.dashboard.Incentive
import com.mosl.mobile.domain.dashboard.IncentivePolicy
import com.mosl.mobile.domain.dashboard.IncentiveTableData
import com.mosl.mobile.domain.dashboard.IncentiveTableData1
import com.mosl.mobile.domain.dashboard.Table
import com.mosl.mobile.domain.dashboard.Table1
import com.mosl.mobile.domain.dashboard.TableData
import com.mosl.mobile.domain.dashboard.WidgetData
import com.mosl.mobile.domain.dashboard.WidgetType
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableFilterItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableWidgetsItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.GroupsItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.WidgetTypeItem
import com.mosl.saathi.api.model.dashboard.incentive.IncentiveItem
import com.mosl.saathi.api.model.dashboard.incentive.Table1Item
import com.mosl.saathi.api.model.dashboard.incentive.TableItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyItem
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

  fun transformIncentivePolicyData(incentivePolicyItem: IncentivePolicyItem): IncentivePolicy {
    return IncentivePolicy(filePath = incentivePolicyItem.filePath ?: "")
  }

  fun tranformIncentiveData(incentiveItem: IncentiveItem): Incentive {
    return Incentive(
        tableData = transformTableData(incentiveItem.table),
        tableData1 = transformTableData1(incentiveItem.table1)
    )
  }

  fun transformTableData(lst: List<TableItem>?): List<IncentiveTableData> {
    return lst!!.safeMap {
      IncentiveTableData(
          q1 = it.q1 ?: 0.0F,
          q2 = it.q2 ?: 0.0F,
          q3 = it.q3 ?: 0.0F,
          q4 = it.q4 ?: 0.0F,
          parameters = it.parameters ?: "",
          ytd = it.yTD ?: 0.0F
      )
    }
  }

  fun transformTableData1(lst: List<Table1Item>?): List<IncentiveTableData1> {
    return lst?.safeMap {
      IncentiveTableData1(
          q1 = it.q1 ?: "0",
          q2 = it.q2 ?: "0",
          q3 = it.q3 ?: "0",
          q4 = it.q4 ?: "0",
          name = it.name ?: "",
          ytd = it.yTD ?: "0",
          widgetType = it.widgetType ?: ""
      )
    } ?: Collections.emptyList()
  }

  fun transformActionableData(actionableCountItem: ActionableCountItem): ActionableCount {
    return ActionableCount(
        table = transformActionableTable(actionableCountItem.table),
        table1 = transformActionableTable1(actionableCountItem.table1)
    )
  }

  fun transformActionableTable(list: List<com.mosl.saathi.api.model.dashboard.actionablecount.TableItem>?): List<Table> {
    return list?.map {
      Table(
          type = it.types ?: ""
      )
    } ?: Collections.emptyList()
  }

  fun transformActionableTable1(list: List<com.mosl.saathi.api.model.dashboard.actionablecount.Table1Item>?): List<Table1> {
    return list?.map {
      Table1(
          count = it.count ?: 0
      )
    } ?: Collections.emptyList()
  }
}