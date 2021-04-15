package com.mosl.app.core.data.dashboardreport

import com.mosl.mobile.domain.dashboardreport.*
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LeadAssignRepositoryImpl @Inject constructor(
    private val saathiApi: SaathiApi,
    val userManager: UserManager
) : LeadAssignRepository {


    override fun getStpLeadAssignList(leadAssignRequest: LeadAssignRequest):
            Observable<List<LeadAssignItem>> {

        val request = leadAssignRequest.mapToRequest()

        return saathiApi.getLeadAssignStpList(request)
            .map {
                it.mapToResponseList()
            }
    }

    override fun getStpLeadTransfer(stpLeadTransferModel: STPLeadTransferModel):
            Observable<STPLeadTransferResponse> {

        val request = stpLeadTransferModel.mapToAssignRequest()

        return saathiApi.getSTPLeadTransfer(request)
            .map {
                mapToMultiUpdateApprovalresponse(it.response, it.message)
            }
    }


}