package com.mosl.tabapp.saathiIndirect.indirectLeadCreation

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.domain.TransferLead
import com.mosl.tabapp.saathiIndirect.indirectLeadCreation.IndirectLeadCreationUiEvent.*
import com.mosl.tabapp.model.EmployeeModel
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadCreationView : BaseView {
  fun renderLeadCreationPageData(uiModel: UiModel<LeadCreationPageData>)

  fun onPrimaryCheckboxChanged(): Observable<Boolean>
  fun renderOnPrimaryCheckboxChanges(isChecked: Boolean)

  fun onOfficePrimaryCheckboxChanged(): Observable<Boolean>
  fun renderOnOfficePrimaryCheckboxChanges(isChecked: Boolean)

  fun onSameAsCheckboxClicked(): Observable<Unit>
  fun renderOnSameAsAboveCheckboxClick()

  fun onPrimaryInfoClicked(): Observable<Unit>
  fun renderPrimaryInfoClick()

  fun onAdditionalInfoClicked(): Observable<Unit>
  fun renderAdditionalInfoClick()

  fun onCategoryChanged(): Observable<Int>
  fun getSelectedCategory(): RowData
  fun renderOnCategoryChange(category: String)

  fun onInterestedProductSelected(): Observable<Int>
  fun getSelectedInterestedProduct(): RowData
  fun renderProductTypeOnInterestedProductSelection(uiModel: UiModel<List<RowData>>)

  fun onSourceSelected(): Observable<Int>
  fun getSelectedSource(): RowData
  fun renderProspectSourceOnSourceSelection(uiModel: UiModel<List<RowData>>)

  fun onStageSelected(): Observable<Int>
  fun getSelectedStage(): RowData
  fun renderStatusOnStageSelection(uiModel: UiModel<List<RowData>>)

  fun onAssignToSearchEventFired(): Observable<AssignToSearchEvent>
  fun onSecondaryRMSearchEventFired(): Observable<SecondaryRMSearchEvent>
  fun onPCGSecondaryRMSearchEventFired(): Observable<PCGSecondaryRMSearchEvent>

  fun renderOnAssignToSearchEventFire(uiModel: UiModel<List<EmployeeModel>>)
  fun renderOnSecondaryRMSearchEventFire(uiModel: UiModel<List<EmployeeModel>>)

  fun onRefereeCodeChanged(): Observable<String>
  fun resetRefereeOnCodeChange()
  fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>)

  fun getRefereeCode(): String

  fun onRefereeNameChanged(): Observable<String>
  fun resetRefereeOnNameChange()
  fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>)

  fun onAddressLineOneChanged(): Observable<String>
  fun renderOnAddressLineOneChange(text: String)

  fun onAddressLineTwoChanged(): Observable<String>
  fun renderOnAddressLineTwoChange(text: String)

  fun onAddressLineThreeChanged(): Observable<String>
  fun renderOnAddressLineThreeChange(text: String)

  fun onPincodeChanged(): Observable<String>
  fun renderOnPincodeChange(pincode: String)

  fun isSameAsAboveChecked(): Boolean

  fun onCountrySelected(): Observable<Int>
  fun getSelectedCountry(): RowData
  fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>)

  fun onStateSelected(): Observable<Int>
  fun getSelectedState(): RowData
  fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>)

  fun onCitySelected(): Observable<Int>
  fun renderOnCitySelectAndSameAsAboveChecked(position: Int)

  fun onOfficeCountrySelected(): Observable<Int>
  fun getSelectedOfficeCountry(): RowData
  fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>)

  fun onOfficeStateSelected(): Observable<Int>
  fun getSelectedOfficeState(): RowData
  fun renderOfficeCityOnOfficeStateSelection(uiModel: UiModel<List<RowData>>)

  fun onCreateButtonClicked(): Observable<Unit>
  fun getLeadCreationUserInput(): IndirectLeadCreationUserInput
  fun renderOnCreateButtonClick(uiModule: UiModel<String>)

  fun onCreateLeadEventFired(): Observable<CreateLeadEvent>
  fun renderCreateLead(uiModel: UiModel<SaathiCommonResponse<CreateLeadResponse>>)

  fun onCancelButtonClicked(): Observable<Unit>

  fun onTransferLeadEventFired(): Observable<TransferLeadEvent>
  fun onTransferLeadPaymentFired(): Observable<TransferLeadPayment>
  fun getMobile(): String
  fun renderTransferLead(uiModel: UiModel<TransferLead>)
  fun onBranchChange(): Observable<Int>
  fun onBankChange(): Observable<Int>
  fun getBank(): BankCodeModel?
  fun getBranch(): BranchCodeModel?
  fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>)
  fun renderBranchChange()

  fun renderStageList(uiModel: UiModel<List<RowData>>)

  fun onCallAccountTypeFired(): Observable<CallAccountType>
  fun renderAccountTypeList(uiModel: UiModel<List<RowData>>)
  fun getAccountTypeId() : String?

  fun onChipsApiCall(): Observable<CallChipsApi>
  fun renderChipsList(uiModel: UiModel<List<RowData>>)

  fun onProspectSourceChanged(): Observable<Int>
  fun getSelectedProspectSource(): RowData
  fun renderOnProspectSourceChange(isBTL: Boolean)
  fun getAccountTypeLookupId() : Int

  companion object {
    const val INVALID_SELECTION_KEY = "-1"
    val SELECT_ROW_DATA = RowData(0, "-1", "Select")
  }
}