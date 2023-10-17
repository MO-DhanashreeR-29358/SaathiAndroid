package com.mosl.tabapp.reactivation.summary

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.tabapp.common.FilterRowData
import io.reactivex.rxjava3.core.Observable

interface ReactivationSummaryView : BaseView {

    fun getSelectedPeriod(): FilterRowData
    fun getSelectedSource(): String
    fun onSearchClick(): Observable<Unit>
    fun renderSearchClick(uiModel: UiModel<ReactivationSummaryItem>)
    fun renderSourceList(uiModel: UiModel<List<String>>)
    fun getCurrentStage(): String

    fun onAssignedClick(): Observable<Unit>
    fun onContactedClick(): Observable<Unit>
    fun onNonContactedClick(): Observable<Unit>
    fun onReactivatedClientslick(): Observable<Unit>
    fun onTradedClick(): Observable<Unit>
    fun onRevenueClick(): Observable<Unit>
}