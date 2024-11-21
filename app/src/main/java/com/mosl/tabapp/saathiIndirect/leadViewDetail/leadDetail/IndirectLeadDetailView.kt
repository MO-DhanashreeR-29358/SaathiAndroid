package com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail

import android.view.MotionEvent
import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.ui.LeadCreationUiEvent
import com.mosl.tabapp.leaddetail.*
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadDetailPageData
import com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail.data.IndirectLeadDetail
import io.reactivex.rxjava3.core.Observable

interface IndirectLeadDetailView : BaseView {
    fun renderArguments(data: LeadDetailArgument)
    fun onNestedScrollViewTouched(): Observable<MotionEvent>
    fun renderOnNestedScrollViewTouch()
    fun onPrimaryAddCheckboxClicked(): Observable<Unit>
    fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit>
    fun onSameAsCheckboxClicked(): Observable<Unit>
    fun getUpdateLeadUSerInput(): UpdateLeadRequest
    fun onSubmitButtonClicked(): Observable<Unit>
    fun onAddProductClicked(): Observable<Unit>
    fun isLeadEditable(): Boolean
    fun openAddProductDialog()
    fun onEditLeadDetailClicked(): Observable<Unit>
    fun renderEditLeadDetailClick()
    fun renderPrimaryAddCheckBoxClicked()
    fun renderOffPrimaryAddCheckBoxClicked()
    fun renderSameAsAboveClicked()

    fun onLeadDetailClicked(): Observable<Unit>
    fun onDocumentClicked(): Observable<Unit>
    fun onDOBClicked(): Observable<Unit>
    fun renderDOBClick()
    fun onAnniversaryClicked(): Observable<Unit>
    fun renderAnniversaryClick()
    fun onRefereeCodeFocusChanged(): Observable<Boolean>
    fun renderOnRefereeCodeFocusChange()
    fun onRefereeNameFocusChanged(): Observable<Boolean>
    fun renderOnRefereeNameFocusChange()
    fun onAdd1AfterTextChanged(): Observable<String>
    fun renderOnAdd1AfterTextChange(text: String)
    fun onAdd2AfterTextChanged(): Observable<String>
    fun renderOnAdd2AfterTextChange(text: String)
    fun onAdd3AfterTextChanged(): Observable<String>
    fun renderOnAdd3AfterTextChange(text: String)
    fun isCbSameAsChecked(): Boolean
    fun renderOnUpdateButtonClick(uiModel: UiModel<String>)
    fun onUpdateLeadEventFired(): Observable<UpdateLeadDetail>
    fun renderUpdateDetail(uiModel: UiModel<UpdateLeadResponse>)

    fun onLeadDetailSectionClicked(): Observable<Unit>

    fun toggleLeadDetailSection()


    fun onRefereeCodeChanged(): Observable<String>
    fun isRefereeCodeFocused(): Boolean
    fun resetRefereeOnCodeChange()
    fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>)
    fun getRefereeCode(): String
    fun onRefereeNameChanged(): Observable<String>
    fun isRefereeNameFocused(): Boolean
    fun resetRefereeOnNameChange()
    fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>)

    fun render(uiModel: UiModel<IndirectLeadDetailPageData>)
    fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetail>)

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



    companion object {
        const val INVALID_SELECTION_KEY = "-1"
    }


}