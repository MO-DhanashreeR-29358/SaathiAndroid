package com.mosl.tabapp.reactivation.detail

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationDetailCombo
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackItem
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackResponse
import io.reactivex.rxjava3.core.Observable

interface ReactivationDetailView : BaseView {

    fun getPeriodFilter() : String
    fun getCurrentStage() : String
    fun renderData(uiModel: UiModel<ReactivationDetailCombo>)

    fun getFeedbackSubmitData():ReactivationFeedbackItem
    fun fnCallFeedbackSubmit():Observable<ReactivationDetailUiEvent.OnFeedbackSubmitCalled>
    fun renderFeedbackSubmit(uiModel: UiModel<ReactivationFeedbackResponse>)

    fun onHierarchyRowClick():Observable<ReactivationDetailUiEvent.OnHierarchyRowClick>

    fun onRefreshDetailTable():Observable<ReactivationDetailUiEvent.RefreshDetailTable>
}