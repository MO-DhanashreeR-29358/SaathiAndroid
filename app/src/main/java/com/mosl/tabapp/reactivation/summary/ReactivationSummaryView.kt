package com.mosl.tabapp.reactivation.summary

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.common.RowData
import io.reactivex.rxjava3.core.Observable

interface ReactivationSummaryView : BaseView {

    fun onPeriodChanged(): Observable<Int>
    fun getSelectedPeriod(): FilterRowData
    fun renderOnPeriodChange(uiModel: UiModel<ReactivationSummaryItem>)

    fun getCurrentStage(): String

    fun onAssignedClick(): Observable<Unit>
    fun onContactedClick(): Observable<Unit>
    fun onNonContactedClick(): Observable<Unit>
    fun onReactivatedClientslick(): Observable<Unit>
    fun onTradedClick(): Observable<Unit>
    fun onRevenueClick(): Observable<Unit>
}