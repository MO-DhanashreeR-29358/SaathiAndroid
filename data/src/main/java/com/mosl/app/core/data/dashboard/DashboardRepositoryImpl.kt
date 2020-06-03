package com.mosl.app.core.data.dashboard

import com.mosl.mobile.domain.dashboard.ActionableCount
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.mobile.domain.dashboard.Incentive
import com.mosl.mobile.domain.dashboard.IncentivePolicy
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.CommonRequest
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountRequest
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyRequest
import io.reactivex.Observable
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : DashboardRepository {
  override fun getDashboardMetaData(data: DashboardRequestData): Observable<List<DashboardData>> {
    return saathiApi.getDashboardMetaData(
        DashboardMetaDataRequest(
            businessProfileId = data.profileId, employeeCode = data.empCode,
            productId = data.productId
        )
    )
        .map { DashboardMapper.transform(it) }
  }

  override fun getIncentive(
    systemName: String,
    data: DashboardRequestData,
    map: Map<String, String>
  ): Observable<Incentive> {
    return saathiApi.getIncentive(
        CommonRequest(
            businessProfileId = data.profileId, employeeCode = data.empCode,
            productId = data.productId, widgetSystemName = systemName, addtionalParameters = map
        )
    )
        .map { DashboardMapper.tranformIncentiveData(it) }
  }

  override fun getIncentivePolicy(
    dashboardId: String,
    data: DashboardRequestData
  ): Observable<IncentivePolicy> {
    return saathiApi.getIncentivePolicy(
        IncentivePolicyRequest(
            employeeCode = data.empCode,
            businessProfileId = data.profileId,
            dashboardId = dashboardId
        )
    )
        .map { DashboardMapper.transformIncentivePolicyData(it) }
  }

  override fun getCurrentQuarterStatusData(
    systemName: String,
    data: DashboardRequestData,
    map: Map<String, String>
  ): Observable<String> {
    return saathiApi.getDashboardData(
        CommonRequest(
            businessProfileId = data.profileId, employeeCode = data.empCode,
            productId = data.productId, widgetSystemName = systemName, addtionalParameters = map
        )
    )
  }

  override fun getActionableCount(
    systemName: String,
    map: Map<String, String>
  ): Observable<Pair<ActionableCount, String>> {
    return saathiApi.getActionableCount(
        ActionableCountRequest(
            widgetSystemName = systemName,
            addtionalParameters = map
        )
    )
        .map { DashboardMapper.transformActionableData(it) }
        .map { Pair(it, systemName) }
  }
}