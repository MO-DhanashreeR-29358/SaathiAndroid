package com.mosl.tabapp.reactivation.createrequestReactivation

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import io.reactivex.rxjava3.core.Observable

interface CreateRequestReactivationView :BaseView {

    fun fnOnSearchClientClick():Observable<Unit>

    fun fnGetData(): ReactivationClientSearchPostModel
    fun fnRenderClientSearch(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>)

    fun fnOnCallMinDate():Observable<CreateReacivationUiEvent.OnCallMinDate>

    fun fnRenderMinDate(uiModel: UiModel<String>)

    fun fnRenderActualmargin(uiModel: UiModel<String>)

}