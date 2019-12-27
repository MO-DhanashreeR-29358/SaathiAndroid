package com.mosl.mobile.domain.ausfbLead

import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import io.reactivex.Observable

interface AusfbLeadRepository {
    fun getBankFilter(): Observable<List<BankCodeModel>>
}