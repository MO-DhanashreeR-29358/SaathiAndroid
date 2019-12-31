package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.AusfbLeadInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.ausfbLead.AusfbLeadModel
import io.reactivex.Observable
import javax.inject.Inject

class AusfbLeadRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : AusfbLeadRepository {
    override fun getAusfbLeadWidget(request: AusfbLeadInput): Observable<AusfbLeadModel> {
        val ausfbLeadRequest = request.requestTransform()
        return api.getAusfbLeadWidgetData(ausfbLeadRequest).map {

            it.responseTransform()
        }
    }

}