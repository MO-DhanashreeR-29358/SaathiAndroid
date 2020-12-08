package com.mosl.tabapp.leadViewDetail.leaddetail

import android.view.MotionEvent
import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.leaddetail.*
import io.reactivex.Observable

interface LeadDetailView : BaseView {
  fun renderArguments(data: LeadDetailArgument)
  fun onNestedScrollViewTouched(): Observable<MotionEvent>
  fun renderOnNestedScrollViewTouch()
  fun onPrimaryAddCheckboxClicked(): Observable<Unit>
  fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit>
  fun onSameAsCheckboxClicked(): Observable<Unit>
  fun getLeadCreationUserInput(): LeadCreationUserInput
  fun onSubmitButtonClicked(): Observable<Unit>
  fun onProfileLayoutClicked(): Observable<Unit>
  fun renderProfileClick()
  fun onAddProductClicked(): Observable<Unit>
  fun isLeadEditable(): Boolean
  fun openAddProductDialog()
  fun onEditLeadDetailClicked(): Observable<Unit>
  fun renderEditLeadDetailClick()
  fun renderPrimaryAddCheckBoxClicked()
  fun renderOffPrimaryAddCheckBoxClicked()
  fun renderSameAsAboveClicked()
  fun onClientDetailClicked(): Observable<Unit>
  fun onClientPortfolioDetailClicked(): Observable<Unit>
  fun onLeadDetailClicked(): Observable<Unit>
  fun onDocumentClicked(): Observable<Unit>
  fun onDOBClicked(): Observable<Unit>
  fun renderDOBClick()
  fun onAnniversaryClicked(): Observable<Unit>
  fun renderAnniversaryClick()
  fun renderOnCreateButtonClick()
  fun onRefereeCodeFocusChanged(): Observable<Boolean>
  fun renderOnRefereeCodeFocusChange()
  fun onRefereeNameFocusChanged(): Observable<Boolean>
  fun renderOnRefereeNameFocusChange()
  fun onLeadNameAfterTextChanged(): Observable<String>
  fun renderOnLeadNameAfterTextChange(text: String)
  fun onProsRefereeNameAfterTextChanged(): Observable<String>
  fun renderOnProsRefereeNameAfterTextChange(text: String)
  fun onProsRefereeMobileTextChanged(): Observable<String>
  fun renderOnProsRefereeMobileTextChange(text: String)
  fun onProsRefereeEmailTextChanged(): Observable<String>
  fun renderOnProsRefereeEmailTextChange(text: String)
  fun onContactPersonAfterTextChanged(): Observable<String>
  fun renderOnContactPersonAfterTextChange(text: String)
  fun onLandLineAfterTextChanged(): Observable<String>
  fun renderOnLandLineAfterTextChange(text: String)
  fun onAlterMobileTextChanged(): Observable<String>
  fun renderOnAlterMobileTextChange(text: String)
  fun onAlterEmailTextChanged(): Observable<String>
  fun renderOnAlterEmailTextChange(text: String)
  fun onEmailTextChanged(): Observable<String>
  fun renderOnEmailTextChange(text: String)
  fun onPanTextChanged(): Observable<String>
  fun renderOnPanTextChange(text: String)
  fun onAadharAfterTextChanged(): Observable<String>
  fun renderOnAadharAfterTextChange(text: String)
  fun onPinTextChanged(): Observable<String>
  fun renderOnPinTextChange(text: String)
  fun onOfficePinTextChanged(): Observable<String>
  fun renderOnOfficePinTextChange(text: String)
  fun onAdd1AfterTextChanged(): Observable<String>
  fun renderOnAdd1AfterTextChange(text: String)
  fun onAdd2AfterTextChanged(): Observable<String>
  fun renderOnAdd2AfterTextChange(text: String)
  fun onAdd3AfterTextChanged(): Observable<String>
  fun renderOnAdd3AfterTextChange(text: String)
  fun isCbSameAsChecked(): Boolean

  fun onClientDetailSectionClicked(): Observable<Unit>
  fun onClientPortfolioSectionClicked(): Observable<Unit>
  fun onLeadDetailSectionClicked(): Observable<Unit>

  fun toggleClientDetailSection()
  fun toggleLeadDetailSection()
  fun toggleClientPortfolioSection()

  fun onPortfolioSnapshotClicked(): Observable<Unit>
  fun togglePortfolio()
  fun onProfitLossSnapshotClicked(): Observable<Unit>
  fun toggleProfitLoss()
  fun onAvailableLimitSnapshotClicked(): Observable<Unit>
  fun toggleAvailableLimit()

  fun onBrokingPortfolioClicked(): Observable<Unit>
  fun toggleBrokingPortfolio()
  fun onAssetPortfolioClicked(): Observable<Unit>
  fun toggleAssetPortfolio()
  fun onLedgerClicked(): Observable<Unit>
  fun toggleLedger()
  fun onBrokingProductClicked(): Observable<Unit>
  fun toggleBrokingProduct()
  fun onAssetProductClicked(): Observable<Unit>
  fun toggleAssetProduct()

  fun onRefereeCodeChanged(): Observable<String>
  fun isRefereeCodeFocused(): Boolean
  fun resetRefereeOnCodeChange()
  fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>)
  fun getRefereeCode(): String
  fun onRefereeNameChanged(): Observable<String>
  fun isRefereeNameFocused(): Boolean
  fun resetRefereeOnNameChange()
  fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>)

  fun render(uiModel: UiModel<LeadDetailPageData>)
  fun renderLeadDetailAndProfession(uiModel: UiModel<LeadDetailAndProfession>)

  fun onUpdateProfileEventFired(): Observable<UpdateProfile>
  fun onProductSpecialEventFired(): Observable<ProductSpecialistEvent>
  fun renderProductSpecial(uiModel: UiModel<List<LeadEmployees>>)
  fun addProductEventFired(): Observable<AddProductEvent>
  fun renderAddProduct(uiModel: UiModel<List<AddProductData>>)
  fun renderAssignTo(uiModel: UiModel<List<LeadEmployees>>)
  fun onAssignToEventFired(): Observable<AssignToEvent>
  fun renderUpdateProfile(uiModel: UiModel<Pair<String, String>>)

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
  fun renderClientDetailWithPortfolioAndProfitLoss(uiModel: UiModel<ClientDetailWithPortfolioAndProfitLoss>)
  fun renderClientDetail(uiModel: UiModel<ClientDetail>)

  fun interestedProductSelected(): Observable<SelectInterestedProductEvent>
  fun renderStage(uiModel: UiModel<List<RowData>>)
  fun renderStatus(uiModel: UiModel<List<RowData>>)
  fun selectedStage(): Observable<SelectStageEvent>

  companion object {
    const val INVALID_SELECTION_KEY = "-1"
    val SELECT_ROW_DATA = RowData(0, "-1", "Select")
  }


}