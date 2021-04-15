package com.mosl.tabapp.pendingApproval

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.pendingapproval.PendingApprovalData
import com.mosl.tabapp.pendingApproval.PendingApprovalUiEvent.ItemClick
import io.reactivex.rxjava3.core.Observable

interface PendingApprovalView : BaseView {
  fun onItemClicked(): Observable<ItemClick>
  fun render(uiModel: UiModel<PendingApprovalData>)
}