package com.mosl.tabapp.reactivation.createrequestReactivation

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.reactivation.ReactivationActualMarginPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.mobile.domain.reactivation.ReactivationSubmitPostModel
import com.mosl.tabapp.helper.fileuploader.SaathiUploadUiSubject
import io.reactivex.rxjava3.core.Observable

interface CreateRequestReactivationView :BaseView {

    fun fnOnSearchClientClick():Observable<Unit>

    fun fnGetData(): ReactivationClientSearchPostModel
    fun fnRenderClientSearch(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>)


    fun fnRenderMinDate(uiModel: UiModel<String>)

    fun fnOnViewClientDetailClick():Observable<Unit>
    fun fnRenderViewClientDetailsClick()

    fun fnGetMarginPostData(): ReactivationActualMarginPostModel
    fun fnCallActualMargin():Observable<CreateReacivationUiEvent.OnCallActualMargin>
    fun fnRenderActualmargin(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>)

    fun fnOnUploadDocClick():Observable<Unit>
    fun fnOpenFileSelet()
    fun onSelectedFileEventFired(): Observable<SaathiUploadUiSubject.SelectedFileEvent>

    fun onSelectedImageEventFired(): Observable<SaathiUploadUiSubject.SelectedImageEvent>

    fun fnRenderImageSelect(strFileUrl:String)

    fun fnGetReactivationPostModel():ReactivationSubmitPostModel
    fun fnOnUpdateClick():Observable<Unit>
    fun  fnRenderUpdate(uiModel: UiModel<String>)
    fun fnOnResetClick():Observable<Unit>
    fun fnRenderReset()

}