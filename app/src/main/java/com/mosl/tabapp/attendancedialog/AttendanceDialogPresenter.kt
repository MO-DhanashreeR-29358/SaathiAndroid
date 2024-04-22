package com.mosl.tabapp.attendancedialog

import com.mosl.app.core.base.BasePresenter
import com.mosl.app.core.data.attendance.AttendanceRepositoryImpl
import com.mosl.mobile.domain.attendance.model.MarkAttendanceData
import com.mosl.mobile.user.login.UserManager
import javax.inject.Inject
import  com.mosl.commons.rx.addTo
import io.reactivex.rxjava3.core.Observable

class AttendanceDialogPresenter @Inject constructor(
    view: AttendanceDialogView,
    val repository: AttendanceRepositoryImpl,
    private val pageData: AttendancePageData,
    private val userManager: UserManager
) : BasePresenter<AttendanceDialogView>(view) {
    override fun start() {

        onFetchCalled()
            .switchMap {
                val employeeCode = userManager.getUser()?.employeeCode.orEmpty()
                repository.logAttendancePopup(employeeCode)
                    .compose(applyUiModel())
            }.subscribe {
                view.renderLogAttendancePopup(it, pageData.action)
            }.addTo(disposable)

        val signInObs = Observable.merge(view.happySelected().map { 1 },
            view.excitedSelected().map { 2 },
            view.sadSelected().map { 3 })
            .filter { pageData.action == AttendanceDialogView.SIGN_IN }
            .share()

        disposable.add(signInObs
            .switchMap {
                repository.markEmpAttendance(
                    MarkAttendanceData(
                        pageData.userLocation,
                        pageData.userLoginBranch,
                        pageData.geoArea,
                        it
                    )
                ).compose(applyUiModel())
            }.subscribe {
                view.renderSubmitResponse(it)
            })
    }
}