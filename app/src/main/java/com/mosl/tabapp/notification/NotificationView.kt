package com.mosl.tabapp.notification

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.notification.NotificationUiEvent.ItemClick
import io.reactivex.Observable

interface NotificationView : BaseView {
    fun onItemClicked(): Observable<ItemClick>
    fun onSpinnerSelection(): Observable<Int>
    fun getNotificationRequestCategory(): String
    fun render(uiModel: UiModel<List<NotificationResponseData>>)
}