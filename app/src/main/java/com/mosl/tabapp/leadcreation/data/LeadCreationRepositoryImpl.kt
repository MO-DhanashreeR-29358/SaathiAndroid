package com.mosl.tabapp.leadcreation.data

import com.mosl.app.core.data.bank.banckCodeTransform
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.*
import com.mosl.saathi.api.model.lead.*
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.domain.TransferLead
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.model.EmployeeModel
import com.mosl.tabapp.util.emptyString
import io.reactivex.Observable
import io.reactivex.functions.Function7
import java.net.URLEncoder
import javax.inject.Inject

class LeadCreationRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManager
) : LeadCreationRepository {

    override fun getLeadCreationPageData(): Observable<LeadCreationPageData> {
        return Observable.zip(
            getCategoryRowDatas(),
            getProfessionRowDatas(),
            getInterestInRowDatas(),
            getSourceRowDatas(),
            getStageRowDatas(),
            getCountryRowDatas(),
            getBankFilter(),
            Function7 { listOfCategory, listOfProfession, listOfInterestIn, listOfSource, listOfStage, listOfCountry, listBank ->
                LeadCreationPageData(
                    categoryRowDatas = listOfCategory,
                    professionRowDatas = listOfProfession,
                    interestInRowDatas = listOfInterestIn,
                    sourceRowDatas = listOfSource,
                    stageRowDatas = listOfStage,
                    countryRowDatas = listOfCountry,
                    listBank = listBank
                )
            }
        )
    }

    fun getBankFilter(): Observable<List<BankCodeModel>> {
        return saathiApi.getBankFilter().map {
            it.banckCodeTransform()
        }
    }

    private fun getCategoryRowDatas(): Observable<List<RowData>> {
        val categoryRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.LEADCATEGORY
        )
        return getRowDatas(categoryRequest)
    }

    override fun getProfessionRowDatas(): Observable<List<RowData>> {
        val professionRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.PROFESSIONID
        )
        return getRowDatas(professionRequest)
    }

    override fun updateLead(request: UpdateLeadRequest): Observable<UpdateLeadResponse> {
        return saathiApi.updateLead(request)
    }

    override fun getInterestInRowDatas(): Observable<List<RowData>> {
        val interestInRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.INTERESTEDIN
        )
        return getRowDatas(interestInRequest)
    }

    private fun getRowDatas(request: LookupRequest): Observable<List<RowData>> {
        return saathiApi.getAllLookup(request)
            .map { it.mapToRowDatas() }
    }

    private fun getSourceRowDatas(): Observable<List<RowData>> {
        val sourceRequest = SourceListRequest(
            id = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return saathiApi.getSourceList(sourceRequest)
            .map { it.mapToRowDataList() }
    }

    override fun getStageRowDatas(): Observable<List<RowData>> {
        val stageRequest = StageRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return saathiApi.getStageList(stageRequest)
            .map { it.stageItemsToRowDatas() }
    }

    override fun getProductTypeRowDatas(selectedInterestedProduct: String): Observable<List<RowData>> {
        val productTypeRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.PRODUCTTYPE,
            parentId = selectedInterestedProduct
        )
        return getRowDatas(productTypeRequest)
    }

    override fun getProspectSourceRowDatas(selectedSource: String): Observable<List<RowData>> {
        val prospectSourceRequest = LeadSourceListRequest(selectedSource)
        return saathiApi.getLeadSourceList(prospectSourceRequest)
            .map { it.leadSourceItemsToRowDatas() }
    }

    override fun getStatusRowDatas(selectedStage: String): Observable<List<RowData>> {
        val statusRequest = StatusRequest(stageID = selectedStage)
        return saathiApi.getStatusList(statusRequest)
            .map { it.statusItemsToRowDatas() }
    }

    override fun getEmployeeModelsFilterBy(text: String): Observable<List<EmployeeModel>> {
        val profileRequest = UserBusinessProfileRequest(
            employeeCode = URLEncoder.encode(text, "utf-8"),
            employeeName = URLEncoder.encode(text, "utf-8")
        )
        return saathiApi.getUserBusinessProfile(profileRequest)
            .map {
                it.profilesToEmployeeModels(userManager.getUser())
            }
    }

    override fun getCountryRowDatas(): Observable<List<RowData>> {
        return saathiApi.getCountries()
            .map { it.countryItemsToRowDatas() }
    }

    override fun getProfileRowDatas(): Observable<List<RowData>> {
        val request = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.LEADPROFILE
        )
        return getRowDatas(request)
    }

    override fun getBrokingProfileRowDatas(): Observable<List<RowData>> {
        val request = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.LEADBROKINGPROFILE
        )
        return getRowDatas(request)
    }

    override fun getIpProfileRowDatas(): Observable<List<RowData>> {
        val request = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.LEADIPPROFILE
        )
        return getRowDatas(request)
    }

    override fun getFinalProfileRowDatas(): Observable<List<RowData>> {
        val request = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.LEADFINALPROFILE
        )
        return getRowDatas(request)
    }

    override fun updateProfile(
        profile: RowData,
        ucId: String
    ): Observable<Pair<String, String>> {
        val request = ClientProfileRequest(profile.value, false, ucId)
        return saathiApi.saveClientProfile(request)
            .map {
                val approval = it.approvalMessage ?: emptyString()
                approval to it.clientPotential
            }
    }

    override fun getStateRowDatas(selectedCountry: String): Observable<List<RowData>> {
        val stateRequest = StateProvinceRequest(selectedCountry)
        return saathiApi.getStateProvinces(stateRequest)
            .map { it.stateProvinceItemsToRowDatas() }
    }

    override fun getCityRowDatas(selectedState: String): Observable<List<RowData>> {
        val cityRequest = CityListRequest(selectedState)
        return saathiApi.getCityList(cityRequest)
            .map { it.cityItemsToRowDatas() }
    }

    override fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>> {
        val createLeadRequest = input mapToCreateLeadRequest userManager.getUser()
        return saathiApi.addUpdateLead(
            request = createLeadRequest
        )
    }

    override fun getRefereeCode(text: String): Observable<SearchReferee> {
        return getReferee(type = TYPE_REFEREE_CODE, text = text)
    }

    override fun getRefereeName(text: String): Observable<SearchReferee> {
        return getReferee(type = TYPE_REFEREE_NAME, text = text)
    }

    override fun transferLeadForMobile(leadIdMobilePair: Pair<String, String>): Observable<TransferLead> {
        val request = TransferLeadRequest(
            mobile = leadIdMobilePair.second,
            currentUser = userManager.user.employeeCode
        )
        return saathiApi.transferLead(request)
            .map {
                if (it.status) {
                    TransferLead(
                        leadIdForTransfer = leadIdMobilePair.first,
                        isPaymentDone = it.response?.isPaymentDone ?: false,
                        message = it.message
                    )

                } else {
                    throw ServiceFailureException(it.message)
                }

            }
    }

    private fun getReferee(
        type: String,
        text: String
    ): Observable<SearchReferee> {
        val request = SearchRefereeModel(
            type = type,
            hint = text
        )
        return saathiApi.getRefereeCode(request)
            .map {
                it refereeResponseToReferee type
            }
    }

    companion object {
        val SELECT_ROW_DATA = RowData(0, "-1", "Select")
        const val TYPE_REFEREE_CODE = "ByCode"
        const val TYPE_REFEREE_NAME = "ByName"
    }
}