package com.mosl.tabapp.saathiIndirect.prospestMeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import io.reactivex.rxjava3.core.Observable

interface IndirectProspestMeetingView:BaseView {
    fun onTextSearchCalled():Observable<IndirectProspestMeetingUiEvent.SearchTextChange>

    fun onTextSearchedRender(uiModel: UiModel<List<ClientSearchModel>>)

    fun onFCodeTextSearchCalled():Observable<IndirectProspestMeetingUiEvent.SearchFCodeTextChange>

    fun onFCodeTextSearchRender(uiModel: UiModel<List<ClientSearchModel>>)

    fun onResetButtonClicked():Observable<Unit>

    fun onSearchButtonClicked():Observable<Unit>

    fun onSearchButtonRender()

    fun onResetButtonRender()

}