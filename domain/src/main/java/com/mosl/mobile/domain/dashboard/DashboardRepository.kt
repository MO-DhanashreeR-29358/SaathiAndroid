package com.mosl.mobile.domain.dashboard

import io.reactivex.Observable

interface DashboardRepository {
  fun getDashboardMetaData(data: DashboardRequestData)
      : Observable<List<DashboardData>>
}