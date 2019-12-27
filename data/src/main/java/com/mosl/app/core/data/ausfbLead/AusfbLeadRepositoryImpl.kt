package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import io.reactivex.Observable
import javax.inject.Inject

class AusfbLeadRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : AusfbLeadRepository {
    override fun getBankFilter(): Observable<List<BankCodeModel>> {
        return api.getBankFilter().map {
            it.transform()
        }
    }
}