package com.mosl.tabapp.saathiIndirect.leadViewDetail.domain

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import java.net.URLEncoder
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

}