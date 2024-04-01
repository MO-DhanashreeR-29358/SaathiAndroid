package com.mosl.tabapp.reactivation.createrequestReactivation

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.tabapp.helper.fileuploader.SaathiUploadUiSubject
import io.reactivex.rxjava3.core.Observable

interface CreateRequestReactivationView :BaseView {

    fun fnOnSearchClientClick():Observable<Unit>

    fun fnGetData(): ReactivationClientSearchPostModel
    fun fnRenderClientSearch(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>)


    fun fnRenderMinDate(uiModel: UiModel<String>)

    fun fnOnViewClientDetailClick():Observable<Unit>
    fun fnRenderViewClientDetailsClick()

    fun fnRenderActualmargin(uiModel: UiModel<String>)

    fun fnOnUploadDocClick():Observable<Unit>
    fun fnOpenFileSelet()
    fun onSelectedFileEventFired(): Observable<SaathiUploadUiSubject.SelectedFileEvent>

    fun onSelectedImageEventFired(): Observable<SaathiUploadUiSubject.SelectedImageEvent>
    fun fnOnUpdateClick():Observable<Unit>

    fun fnOnResetClick():Observable<Unit>
    fun fnRenderReset()
}