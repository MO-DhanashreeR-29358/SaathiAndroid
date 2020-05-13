package com.mosl.tabapp.leadcreation.ui

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.ui.LeadCreationUiEvent.CreateLeadEvent
import io.reactivex.Observable

interface LeadCreationView : BaseView {
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

  fun onInterestedProductSelected(): Observable<Int>
  fun getSelectedInterestedProduct(): RowData
  fun renderProductTypeOnInterestedProductSelection(uiModel: UiModel<List<RowData>>)

  fun onSourceSelected(): Observable<Int>
  fun getSelectedSource(): RowData
  fun renderProspectSourceOnSourceSelection(uiModel: UiModel<List<RowData>>)

  fun onStageSelected(): Observable<Int>
  fun getSelectedStage(): RowData
  fun renderStatusOnStageSelection(uiModel: UiModel<List<RowData>>)

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
  fun getLeadCreationUserInput(): LeadCreationUserInput
  fun renderOnCreateButtonClick(uiModule: UiModel<String>)

  fun onCreateLeadEventFired(): Observable<CreateLeadEvent>
  fun renderCreateLead(uiModel: UiModel<SaathiCommonResponse<CreateLeadResponse>>)

  fun onCancelButtonClicked(): Observable<Unit>


  companion object {
    const val INVALID_SELECTION_KEY = "-1"
  }
}