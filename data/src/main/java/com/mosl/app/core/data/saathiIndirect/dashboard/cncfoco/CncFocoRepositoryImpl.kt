package com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco

import com.google.gson.Gson
import com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco.CncFocoDashboardMapper.mapToCncDashboardClientMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco.CncFocoDashboardMapper.mapToCncDashboardMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.mapToIFADashboardMeetingGraph
import com.mosl.app.core.data.saathiIndirect.dashboard.requestTransform
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
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
    override fun fnGetCncDashboardFilter(): Observable<IndirectFilterDataClass> {

        return api.getCNCFocoDashFilter(IndirectDashboardCncAdminFilterPostRequest(
employeeCode = userManager.getUser()?.employeeCode
        )).map {
            CncFocoDashboardMapper.fnTranformFilterList(it.response)
        }
    }

    override fun fngetCNCFocoDashRevenueWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return api.getCNCFocoDashRevenueWidgetData(req).map {
            mapToCncDashboardMeetingGraph(it)
        }
    }

    override fun fngetCNCFocoDashNoOfClientMeetingWidgetData(request: FocoDashboardInput): Observable<FocoDashboardModel> {
        val req = request.requestTransform()
        return  api.getCNCFocoDashNoOfClientMeetingWidgetData(req).map {
            mapToCncDashboardClientMeetingGraph(it)
        }
    }
}