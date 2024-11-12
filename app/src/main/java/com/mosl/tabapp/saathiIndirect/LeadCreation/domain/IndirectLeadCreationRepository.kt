package com.mosl.tabapp.saathiIndirect.LeadCreation.domain

import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.domain.TransferLead
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadCreationRepository {
    fun getLeadCreationPageData(): Observable<IndirectLeadCreationPageData>
    fun getProductTypeRowDatas(selectedInterestedProduct: String): Observable<List<RowData>>
    fun getProspectSourceRowDatas(selectedSource: String): Observable<List<RowData>>
    fun getStatusRowDatas(selectedStage: String): Observable<List<RowData>>
    fun getStageRowDatas(): Observable<List<RowData>>
    fun getStateRowDatas(selectedCountry: String): Observable<List<RowData>>
    fun getCityRowDatas(selectedState: String): Observable<List<RowData>>
    fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>>
    fun getRefereeCode(text: String): Observable<SearchReferee>
    fun getRefereeName(text: String): Observable<SearchReferee>
    fun transferLeadForMobile(leadIdMobilePair: Pair<String, String>): Observable<TransferLead>

    fun getProfessionRowDatas(): Observable<List<RowData>>

    fun updateLead(request: UpdateLeadRequest): Observable<UpdateLeadResponse>

    fun getInterestInRowDatas(): Observable<List<RowData>>
    fun getCountryRowDatas(): Observable<List<RowData>>
    fun getProfileRowDatas(): Observable<List<RowData>>
    fun getBrokingProfileRowDatas(): Observable<List<RowData>>
    fun getIpProfileRowDatas(): Observable<List<RowData>>
    fun getFinalProfileRowDatas(): Observable<List<RowData>>
    fun getAccountTypeList(lookupTypeId : Int): Observable<List<RowData>>

}