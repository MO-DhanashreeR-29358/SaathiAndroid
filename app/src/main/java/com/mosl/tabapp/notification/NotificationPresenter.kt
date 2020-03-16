package com.mosl.tabapp.notification

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.tabapp.constants.MOSLConstants.BUSINESS
import com.mosl.tabapp.constants.MOSLConstants.OPERATIONAL
import javax.inject.Inject

class NotificationPresenter @Inject constructor(
    view: NotificationView,
    val repository: NotificationRepository
) : BasePresenter<NotificationView>(view) {

    override fun start() {

            onFetchCalled()
                .map {
                    NotificationRequestInput(OPERATIONAL)
                }
                .switchMap {
                    repository.getNotificationData(it)
                        .compose(applyUiModel())
                }
                .subscribe {
                    view.render(it)
                }
                .addTo(disposable)

    }
}