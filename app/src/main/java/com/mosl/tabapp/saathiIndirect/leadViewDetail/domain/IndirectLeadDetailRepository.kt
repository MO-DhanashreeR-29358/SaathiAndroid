package com.mosl.tabapp.saathiIndirect.leadViewDetail.domain

import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadDetailRepository {
  fun getLeadDetail(leadId: String): Observable<IndirectLeadDetail>
}