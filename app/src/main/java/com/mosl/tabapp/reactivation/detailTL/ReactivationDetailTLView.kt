package com.mosl.tabapp.reactivation.detailTL

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationDetailCombo
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackItem
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackResponse
import io.reactivex.rxjava3.core.Observable

interface ReactivationDetailTLView : BaseView {

    fun getPeriodFilter() : String
    fun getCurrentStage() : String
    fun getEmpCode() : String
    fun getFirstReportingTo() : String
    fun renderData(uiModel: UiModel<ReactivationDetailCombo>)

}