package com.mosl.tabapp.pendingApproval

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.pendingApproval.PendingApprovalUiEvent.ItemClick
import io.reactivex.Observable

interface PendingApprovalView : BaseView {
  fun onItemClicked(): Observable<ItemClick>
  fun render(uiModel: UiModel<List<NotificationResponseData>>)
}