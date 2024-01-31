package com.mosl.tabapp.attendancedialog

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.attendance.LatLng
import io.reactivex.rxjava3.core.Observable

interface AttendanceDialogView : BaseView {
    fun renderLogAttendancePopup(uiModel: UiModel<String>, action: String?)
    fun renderEmpAction(action: String?)
    fun renderSubmitResponse(uiModel: UiModel<String>)
    fun onSubmitClick(): Observable<Unit>
    fun happySelected(): Observable<Unit>
    fun excitedSelected(): Observable<Unit>
    fun sadSelected(): Observable<Unit>

  companion object {
    const val SIGN_IN = "SignIn"
    const val SIGN_OUT = "SignOut"
    const val COLLECTION_OFFICER = "CollectionOfficer"
  }

}

data class AttendancePageData(
    @JvmField val action: String,
    @JvmField val userLocation: LatLng,
    @JvmField val userLoginBranch: String,
    @JvmField val geoArea: String
)