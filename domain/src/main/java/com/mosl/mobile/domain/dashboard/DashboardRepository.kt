package com.mosl.mobile.domain.dashboard

import io.reactivex.Observable

interface DashboardRepository {
  fun getDashboardMetaData(data: DashboardRequestData)
      : Observable<List<DashboardData>>

//  fun getIncentivePolicy(dashboardId: String, profileID : String)

  fun getIncentive(
    systemName: String,
    data: DashboardRequestData,
    map: Map<String, String>
  ):
      Observable<Incentive>

//  fun getActionableCount(systemName : String)
}