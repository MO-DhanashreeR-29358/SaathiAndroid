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

  fun onPrimaryInfoClicked(): Observable<Unit>
  fun renderPrimaryInfoClick()

  fun onAdditionalInfoClicked(): Observable<Unit>
  fun renderAdditionalInfoClick()

  fun onInterestedProductSelected(): Observable<Int>
  fun getSelectedInterestedProduct(): RowData
  fun renderProductTypeOnInterestedProductSelection(uiModel: UiModel<List<RowData>>)

  fun onCountrySelected(): Observable<Int>
  fun getSelectedCountry(): RowData
  fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>)

  fun onStateSelected(): Observable<Int>
  fun getSelectedState(): RowData
  fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>)

  fun onCreateButtonClicked(): Observable<Unit>
  fun getLeadCreationUserInput(): LeadCreationUserInput
  fun renderOnCreateButtonClick(uiModule: UiModel<String>)

  fun onCreateLeadEventFired(): Observable<CreateLeadEvent>
  fun renderCreateLead(uiModel: UiModel<SaathiCommonResponse<CreateLeadResponse>>)

  fun onCancelButtonClicked(): Observable<Unit>

  fun onSameAsCheckboxClicked(): Observable<Unit>

  companion object {
    const val INVALID_SELECTION_KEY = "-1"
  }
}