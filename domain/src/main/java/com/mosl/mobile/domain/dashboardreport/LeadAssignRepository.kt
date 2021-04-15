package com.mosl.mobile.domain.dashboardreport

import io.reactivex.rxjava3.core.Observable

interface LeadAssignRepository {

    fun getStpLeadAssignList(leadAssignRequest: LeadAssignRequest): Observable<List<LeadAssignItem>>
    fun getStpLeadTransfer(stpLeadTransferModel: STPLeadTransferModel): Observable<STPLeadTransferResponse>

}