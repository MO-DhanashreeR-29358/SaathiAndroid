package com.mosl.tabapp.saathiIndirect.leadViewDetail.domain

import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail.data.IndirectLeadDetail
import com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail.data.leadViewItemToLeadDetail
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.leadViewItemToLeadDetailMain
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IndirectLeadDetailRepositoryImpl @Inject constructor(
    val saathiApi: SaathiIndirectApi,
    val userManager: UserManager
) : IndirectLeadDetailRepository {

    override fun getLeadDetail(leadId: String): Observable<IndirectLeadDetail> {
        val request = LeadViewRequest(leadId)
        return saathiApi.getLeadView(request)
            .map {
                it.response?.leadViewItemToLeadDetail()!!
            }
    }

    override fun getLeadDetailForMainPage(leadId: String): Observable<IndirectLeadDetailMain> {
        val request = LeadViewRequest(leadId)
        return saathiApi.getLeadView(request)
            .map {
                it.response?.leadViewItemToLeadDetailMain()!!
            }
    }
}