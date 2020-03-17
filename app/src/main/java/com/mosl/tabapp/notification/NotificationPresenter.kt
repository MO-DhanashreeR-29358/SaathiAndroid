package com.mosl.tabapp.notification

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class NotificationPresenter @Inject constructor(
    view: NotificationView,
    val repository: NotificationRepository,
    private val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<NotificationView>(view) {

    override fun start() {
        view.onSpinnerSelection()
            .map {
                NotificationRequestInput(view.getNotificationRequestCategory())
            }.switchMap {
                repository.getNotificationData(it)
                    .compose(applyUiModel())
            }
            .subscribe {
                view.render(it)
            }
            .addTo(disposable)

        view.onItemClicked()
            .subscribe {
                navigateTo(it.additionalData)
            }
            .addTo(disposable)
    }

    private fun navigateTo(dataStr: String) {
        if (dataStr == null || dataStr.length < 1) {
            screenNavigator.openHome()
        }
        val data = NotificationData(dataStr)
        if (data.page.equals("LEADVIEW", ignoreCase = true)) {
            val bundle = Bundle()
            var leadId = data.paramMap!![LEAD_ID]
            var ucid = data.paramMap!![UCID]
            ucid = if (ucid == null || ucid.equals("", ignoreCase = true)) "0" else ucid
            leadId = if (leadId == null || leadId.equals("", ignoreCase = true)) "0" else leadId
            bundle.putString(UCID, ucid)
            bundle.putString(LEAD_ID, leadId)
            bundle.putParcelable(NOTIFICATION_DATA, data)
            screenNavigator.openPage(
                NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                false
            )
        } else if (data.page.equals("PENDINGAPPROVAL", ignoreCase = true)) {
            screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL, false)
        } else if (data.page.equals("BIRTHDAY", ignoreCase = true)) {
            screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY, false)
        } else if (data.page.equals("DASHBOARD", ignoreCase = true)) {
            screenNavigator.openHome()
        } else {
            screenNavigator.openHome()
        }
    }
}