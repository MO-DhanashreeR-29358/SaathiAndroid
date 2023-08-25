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

}