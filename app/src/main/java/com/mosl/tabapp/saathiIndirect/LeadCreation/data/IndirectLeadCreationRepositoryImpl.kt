package com.mosl.tabapp.saathiIndirect.LeadCreation.data

import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.LeadSourceListRequest
import com.mosl.saathi.api.model.createlead.SearchRefereeModel
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageRequest
import com.mosl.saathi.api.model.lead.StateProvinceRequest
import com.mosl.saathi.api.model.lead.StatusRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadcreation.data.cityItemsToRowDatas
import com.mosl.tabapp.leadcreation.data.countryItemsToRowDatas
import com.mosl.tabapp.leadcreation.data.leadSourceItemsToRowDatas
import com.mosl.tabapp.leadcreation.data.mapToRowDataList
import com.mosl.tabapp.leadcreation.data.refereeResponseToReferee
import com.mosl.tabapp.leadcreation.data.stageItemsToRowDatas
import com.mosl.tabapp.leadcreation.data.stateProvinceItemsToRowDatas
import com.mosl.tabapp.leadcreation.data.statusItemsToRowDatas
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.saathiIndirect.LeadCreation.IndirectLeadCreationUserInput
import com.mosl.tabapp.saathiIndirect.LeadCreation.data.IndirectLeadCreationMapper.backgroundItemsToRowDatas
import com.mosl.tabapp.saathiIndirect.LeadCreation.data.IndirectLeadCreationMapper.mapToCreateLeadRequest
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadCreationPageData
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadCreationRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function7
import javax.inject.Inject

class IndirectLeadCreationRepositoryImpl @Inject constructor(
    val indirectSaathiApi: SaathiIndirectApi,
    val userManager: UserManager
) : IndirectLeadCreationRepository {

    override fun getLeadCreationPageData(): Observable<IndirectLeadCreationPageData> {
        return Observable.zip(
            getInterestInRowDatas(),
            getCategoryRowDatas(),
            getSourceRowDatas(),
            getStageRowDatas(),
            getCountryRowDatas(),
            getBackgroundRowDatas(),
            getWorkExpRowDatas(),
            Function7 { listOfInterestIn,listOfCategory,  listOfSource, listOfStage, listOfCountry, listOfBackground, listOfWorkExp ->
                IndirectLeadCreationPageData(
                    interestInRowDatas = listOfInterestIn,
                    categoryRowDatas = listOfCategory,
                    sourceRowDatas = listOfSource,
                    stageRowDatas = listOfStage,
                    countryRowDatas = listOfCountry,
                    backgroundRowDatas = listOfBackground,
                    workExpRowDatas = listOfWorkExp
                )
            }
        )
    }

    override fun getCategoryRowDatas(): Observable<List<RowData>> {
        val categoryRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.INDIRECT_CATEGORY
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
        if(request.leadAccountTypeId.isEmpty()){
            request.leadAccountTypeId = "-1"
        }
        return indirectSaathiApi.updateLead(request)
    }

    override fun getInterestInRowDatas(): Observable<List<RowData>> {
        val categoryRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.INDIRECT_INTERESTED_IN
        )
        return getRowDatas(categoryRequest)
    }

    private fun getRowDatas(request: LookupRequest): Observable<List<RowData>> {
        return indirectSaathiApi.getAllLookup(request)
            .map { IndirectLeadCreationMapper.transformLookupData(it.response?: mutableListOf()
            )}
    }

    private fun getSourceRowDatas(): Observable<List<RowData>> {
        val stageRequest = StageRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return indirectSaathiApi.getSourceList(stageRequest)
            .map { it.response?.mapToRowDataList()?: mutableListOf()  }
    }

    override fun getStageRowDatas(): Observable<List<RowData>> {
        val stageRequest = StageRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return indirectSaathiApi.getStageList(stageRequest)
            .map { it.response?.stageItemsToRowDatas()?: mutableListOf()  }
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
        return indirectSaathiApi.getLeadSourceList(prospectSourceRequest)
            .map { it.response?.leadSourceItemsToRowDatas() ?: mutableListOf() }
    }

    override fun getStatusRowDatas(selectedStage: String): Observable<List<RowData>> {
        val statusRequest = StatusRequest(stageID = selectedStage)
        return indirectSaathiApi.getStatusList(statusRequest)
            .map { it.response?.statusItemsToRowDatas()  ?: mutableListOf()}
    }

    override fun getCountryRowDatas(): Observable<List<RowData>> {
        return indirectSaathiApi.getCountries()
            .map { it.response?.countryItemsToRowDatas() ?: mutableListOf() }
    }
    override fun getBackgroundRowDatas(): Observable<List<RowData>> {
        val stageRequest = StageRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return indirectSaathiApi.getBackground(stageRequest)
            .map { it.response?.backgroundItemsToRowDatas() ?: mutableListOf() }
    }

    override fun getAssociateBrokerRowDatas(): Observable<List<RowData>> {
        val stageRequest = StageRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" })
        )
        return indirectSaathiApi.getBackground(stageRequest)
            .map { it.response?.backgroundItemsToRowDatas() ?: mutableListOf() }
    }

    override fun getWorkExpRowDatas(): Observable<List<RowData>> {
        val workExpRequest = LookupRequest(
            businessUnitId = requireNotNull(
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                { "businessUnitId in UserManager is not be null" }),
            lookupTypeId = MOSLConstants.INDIRECT_WORK_EXP
        )
        return getRowDatas(workExpRequest)
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

    override fun getStateRowDatas(selectedCountry: String): Observable<List<RowData>> {
        val stateRequest = StateProvinceRequest(selectedCountry)
        return indirectSaathiApi.getStateProvinces(stateRequest)
            .map { it.response?.stateProvinceItemsToRowDatas() ?: mutableListOf()}
    }

    override fun getCityRowDatas(selectedState: String): Observable<List<RowData>> {
        val cityRequest = CityListRequest(selectedState)
        return indirectSaathiApi.getCityList(cityRequest)
            .map { it.response?.cityItemsToRowDatas()?: mutableListOf() }
    }

    override fun createLead(input: IndirectLeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>> {
        val createLeadRequest = input mapToCreateLeadRequest userManager.getUser()
        return indirectSaathiApi.addUpdateLead(
            request = createLeadRequest
        )
    }

    override fun getRefereeCode(text: String): Observable<SearchReferee> {
        return getReferee(type = TYPE_REFEREE_CODE, text = text)
    }

    override fun getRefereeName(text: String): Observable<SearchReferee> {
        return getReferee(type = TYPE_REFEREE_NAME, text = text)
    }


    private fun getReferee(
        type: String,
        text: String
    ): Observable<SearchReferee> {
        val request = SearchRefereeModel(
            type = type,
            hint = text
        )
        return indirectSaathiApi.getRefereeCode(request)
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