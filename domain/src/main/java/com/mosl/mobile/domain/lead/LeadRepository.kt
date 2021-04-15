package com.mosl.mobile.domain.lead

import com.mosl.saathi.api.model.dashboard.lead.LeadModel
import io.reactivex.rxjava3.core.Observable

interface LeadRepository {

    fun getLeadWidget(request: LeadInput): Observable<LeadModel>

}