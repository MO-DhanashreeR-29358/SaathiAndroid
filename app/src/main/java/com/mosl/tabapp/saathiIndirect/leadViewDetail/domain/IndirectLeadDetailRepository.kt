package com.mosl.tabapp.saathiIndirect.leadViewDetail.domain

import com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail.data.IndirectLeadDetail
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadDetailRepository {
  fun getLeadDetail(leadId: String): Observable<IndirectLeadDetail>

  fun getLeadDetailForMainPage(leadId: String): Observable<IndirectLeadDetailMain>
}