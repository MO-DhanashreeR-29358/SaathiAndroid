package com.mosl.app.core.data.dashboard

import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest
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

}