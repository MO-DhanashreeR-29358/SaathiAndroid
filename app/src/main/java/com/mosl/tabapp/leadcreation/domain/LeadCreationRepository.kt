package com.mosl.tabapp.leadcreation.domain

import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.model.EmployeeModel
import io.reactivex.Observable

interface LeadCreationRepository {
  fun getLeadCreationPageData(): Observable<LeadCreationPageData>
  fun getProductTypeRowDatas(selectedInterestedProduct: String): Observable<List<RowData>>
  fun getProspectSourceRowDatas(selectedSource: String): Observable<List<RowData>>
  fun getStatusRowDatas(selectedStage: String): Observable<List<RowData>>
  fun getEmployeeModelsFilterBy(text: String): Observable<List<EmployeeModel>>
  fun getStateRowDatas(selectedCountry: String): Observable<List<RowData>>
  fun getCityRowDatas(selectedState: String): Observable<List<RowData>>
  fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>>
  fun getRefereeCode(text: String): Observable<SearchReferee>
  fun getRefereeName(text: String): Observable<SearchReferee>
  fun transferLeadForMobile(leadIdMobilePair: Pair<String, String>): Observable<TransferLead>
}