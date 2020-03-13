package com.mosl.app.core.data.lead

import com.mosl.mobile.domain.lead.LeadInput
import com.mosl.mobile.domain.lead.LeadRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.lead.LeadModel
import io.reactivex.Observable
import javax.inject.Inject

class LeadRepositoryImpl @Inject constructor(
    val api: SaathiApi
) : LeadRepository {
    override fun getLeadWidget(request: LeadInput): Observable<LeadModel> {
        val leadRequest = request.requestTransform()
        return api.getLeadWidgetData(leadRequest).map {
            it.responseTransform()
        }
    }

}