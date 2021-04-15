package com.mosl.mobile.domain.dashboard

import io.reactivex.rxjava3.core.Observable

interface DashboardRepository {
  fun getDashboardMetaData(data: DashboardRequestData)
      : Observable<List<DashboardData>>

  fun getIncentivePolicy(
    dashboardId: String,
    data: DashboardRequestData
  ): Observable<IncentivePolicy>

  fun getIncentive(
    systemName: String,
    data: DashboardRequestData,
    map: Map<String, String>
  ): Observable<Incentive>

  fun getCurrentQuarterStatusData(
    systemName: String,
    data: DashboardRequestData,
    map: Map<String, String>
  ): Observable<String>

  fun getActionableCount(
    systemName: String,
    map: Map<String, String>
  ): Observable<Pair<ActionableCount, String>>
}