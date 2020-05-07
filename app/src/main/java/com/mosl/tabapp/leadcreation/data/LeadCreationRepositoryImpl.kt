package com.mosl.tabapp.leadcreation.data

import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import io.reactivex.Observable
import javax.inject.Inject

class LeadCreationRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadCreationRepository {
  override fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>> {
    val createLeadRequest = input.mapToCreateLeadRequest()
    return saathiApi.addUpdateLead(
        request = createLeadRequest
    )
  }
}