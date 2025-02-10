package com.mosl.tabapp.advancesearch.quickSearch

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchInput
import com.mosl.mobile.domain.snapshot.AdvanceSearchDataCombo
import io.reactivex.rxjava3.core.Observable

interface QuickSearchTabView :BaseView {

    fun getAdditionalParameterMap(rowData: String?, columnData: String?): Map<String, String>
    fun renderData(uiModel: UiModel<AdvanceSearchDataCombo>)

    fun onTableRowClicked():Observable<QuickSearchUiEvent.onTableRowClicked>
    fun fnRenderTableRowClicked(strLeadId:String)

    fun fnGetSearchInput(): AdvanceSearchInput

    fun onSearchButtonClicked():Observable<Unit>

    fun onResetButtonClicked():Observable<Unit>

    fun fnRenderSearchButtonClicked()

    fun fnRenderResetButtonClicked()
}