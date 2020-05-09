package com.mosl.tabapp.leadcreation.data

import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.SourceListRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageRequest
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import io.reactivex.Observable
import io.reactivex.functions.Function6
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
        Function6 { listOfCategory, listOfProfession, listOfInterestIn, listOfSource, listOfStage, listOfCountry ->
          LeadCreationPageData(
              categoryRowDatas = listOfCategory,
              professionRowDatas = listOfProfession,
              interestInRowDatas = listOfInterestIn,
              sourceRowDatas = listOfSource,
              stageRowDatas = listOfStage,
              countryRowDatas = listOfCountry
          )
        }
    )
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

  private fun getProfessionRowDatas(): Observable<List<RowData>> {
    val professionRequest = LookupRequest(
        businessUnitId = requireNotNull(
            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
            { "businessUnitId in UserManager is not be null" }),
        lookupTypeId = MOSLConstants.PROFESSIONID
    )
    return getRowDatas(professionRequest)
  }

  private fun getInterestInRowDatas(): Observable<List<RowData>> {
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

  private fun getStageRowDatas(): Observable<List<RowData>> {
    val stageRequest = StageRequest(
        businessUnitId = requireNotNull(
            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
            { "businessUnitId in UserManager is not be null" })
    )
    return saathiApi.getStageList(stageRequest)
        .map { it.stageItemsToRowDatas() }
  }

  private fun getCountryRowDatas(): Observable<List<RowData>> {
    return saathiApi.getCountries()
        .map { it.countryItemsToRowDatas() }
  }

  override fun createLead(input: LeadCreationUserInput): Observable<SaathiCommonResponse<CreateLeadResponse>> {
    val createLeadRequest = input mapToCreateLeadRequest userManager.getUser()
    return saathiApi.addUpdateLead(
        request = createLeadRequest
    )
  }
}