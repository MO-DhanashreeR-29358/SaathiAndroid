package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.AusfbLeadInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class AusfbLeadRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : AusfbLeadRepository {
    override fun getAusfbLeadWidget(ausfbLeadRequest: AusfbLeadInput): Observable<String> {
        val request = ausfbLeadRequest.requestTransform()
        return api.getAusfbLeadWidgetData(request)
    }

}