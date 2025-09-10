package com.mosl.tabapp.leadcreation.domain

import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.model.EmployeeModel
import io.reactivex.rxjava3.core.Observable

interface LeadCreationRepository {
    fun getLeadCreationPageData(): Observable<LeadCreationPageData>
    fun getCategoryRowDatas(): Observable<List<RowData>>

    fun getClientTypeRowDatas(): Observable<List<RowData>>
    fun getProductTypeRowDatas(selectedInterestedProduct: String): Observable<List<RowData>>
    fun getProspectSourceRowDatas(selectedSource: String): Observable<List<RowData>>
    fun getStatusRowDatas(selectedStage: String): Observable<List<RowData>>
    fun getStageRowDatas(): Observable<List<RowData>>
    fun getRealEstateStageRowDatas(): Observable<List<RowData>>

    fun getEmployeeModelsFilterBy(text: String): Observable<List<EmployeeModel>>
    fun getEmployeeModelsFilterForSecondaryRM(text: String, branchCode : String): Observable<List<EmployeeModel>>
    fun getEmployeeModelsFilterForWMSecondaryRM(text: String): Observable<List<EmployeeModel>>
    fun getStateRowDatas(selectedCountry: String): Observable<List<RowData>>
    fun getCityRowDatas(selectedState: String): Observable<List<RowData>>
    fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>>
    fun getRefereeCode(text: String): Observable<SearchReferee>
    fun getRefereeName(text: String): Observable<SearchReferee>
    fun transferLeadForMobile(leadIdMobilePair: Pair<String, String>): Observable<TransferLead>

    fun getProfessionRowDatas(): Observable<List<RowData>>

    fun updateLead(request: UpdateLeadRequest): Observable<UpdateLeadResponse>

    fun getInterestInRowDatas(): Observable<List<RowData>>
    fun getSourceRowDatas(): Observable<List<RowData>>

    fun getCountryRowDatas(): Observable<List<RowData>>
    fun getBankFilter(): Observable<List<BankCodeModel>>
    fun getSTDCodeRowDatas(): Observable<List<RowData>>
    fun getProfileRowDatas(): Observable<List<RowData>>
    fun getBrokingProfileRowDatas(): Observable<List<RowData>>
    fun getIpProfileRowDatas(): Observable<List<RowData>>
    fun getFinalProfileRowDatas(): Observable<List<RowData>>
    fun updateProfile(
        profile: RowData,
        ucId: String
    ): Observable<Pair<String, String>>

    fun getAccountTypeList(lookupTypeId : Int): Observable<List<RowData>>

    fun getStandardRemarkList(lookupTypeId : Int): Observable<List<RowData>>

    fun getChipsList(
    ): Observable<List<RowData>>

    fun getSectorList(): Observable<List<LookupItem>>

    fun getDesignationList(): Observable<List<LookupItem>>

    fun getNetworthList(): Observable<List<LookupItem>>

}