package com.mosl.mobile.domain.aulead

import com.mosl.saathi.api.model.dashboard.ausfbLead.AusfbLeadModel
import io.reactivex.Observable

interface AusfbLeadRepository {

    fun getAusfbLeadWidget(request: AusfbLeadInput): Observable<AusfbLeadModel>

}