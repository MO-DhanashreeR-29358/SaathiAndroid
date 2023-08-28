package com.mosl.tabapp.reactivation.detail

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationDetailCombo

interface ReactivationDetailView : BaseView {

    fun getPeriodFilter() : String
    fun getCurrentStage() : String
    fun renderData(uiModel: UiModel<ReactivationDetailCombo>)
}