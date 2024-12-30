package com.mosl.app.core.data.saathiIndirect.dashboard

import com.google.gson.Gson
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IndirectIFADashboardRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
) :IndirectIFADashboardRepository {
    override fun fnGetIFADashboardFilter(): Observable<String> {
        return saathiIndrectApi.getIFADashboard(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetTodaysBirthdayAnniversary(): Observable<String> {

        return  saathiIndrectApi.getIFABirthdayAnniversary(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetFirstMeetingNotDone(): Observable<String> {
        return saathiIndrectApi.getIFASaleshFirstMeetingNotDone(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetLeadsNotUpdated(): Observable<String> {
        return saathiIndrectApi.getIFALeadNotUpdate(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetSalesDashInactive(): Observable<String> {
        return saathiIndrectApi.getIFASalesDashInactive(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }

    override fun fnGetIFASalesDashboBuisnessSnapshot(): Observable<String> {
        return saathiIndrectApi.getIFASalesDashboBuisnessSnapshot(IndirectDashboardFilterPostRequest(employeeCode = "6160")).map {
            Gson().toJson(it.response)
        }
    }
}