package com.mosl.tabapp.reactivation.detail

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.common.RowData
import io.reactivex.rxjava3.core.Observable

interface ReactivationDetailView : BaseView {

    fun getPeriod() : String
    fun getCurrentStage() : String

}