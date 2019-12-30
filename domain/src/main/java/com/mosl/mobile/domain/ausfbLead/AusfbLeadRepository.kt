package com.mosl.mobile.domain.ausfbLead

import io.reactivex.Observable

interface AusfbLeadRepository {

    fun getAusfbLeadWidget(request: AusfbLeadInput): Observable<String>

}