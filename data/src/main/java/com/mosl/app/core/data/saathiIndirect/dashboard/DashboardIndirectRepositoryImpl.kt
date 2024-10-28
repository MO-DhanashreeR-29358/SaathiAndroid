package com.mosl.app.core.data.saathiIndirect.dashboard

import com.mosl.mobile.domain.saathiIndirect.dashboard.DashboardIndirctRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DashboardIndirectRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
) :DashboardIndirctRepository{

    override fun fnGetFOCODashboardFilter(): Observable<String> {

        return saathiIndrectApi.fnGetFOCODashFilter(FOCODashboardFilterpostReq(employeeCode = "16149", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )).map {
            it.string()
        }
    }
}