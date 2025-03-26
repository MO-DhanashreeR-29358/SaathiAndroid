package com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco

import com.google.gson.Gson
import com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco.CncFocoRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardCncAdminFilterPostRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CncFocoRepositoryImpl @Inject constructor(
    val api: SaathiIndirectApi,
    val userManager: UserManagerImpl,
):CncFocoRepository {
    override fun fnGetCncDashboardFilter(): Observable<String> {

        return api.getCNCFocoDashFilter(IndirectDashboardCncAdminFilterPostRequest(
employeeCode = userManager.getUser()?.employeeCode
        )).map {
            it.string().toString()
        }
    }
}