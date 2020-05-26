package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.AddUpdateMeeting
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.AttendeeItemClick
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.FranchiseIFAUser
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SaveConfirmClick
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SaveRequestData
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SearchTextChange
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SearchTextChangeSplAttendee
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SendOtpRequest
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.SplAttendeeItemClick
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.ValidationStatus
import com.mosl.tabapp.model.MeetingApptModel
import io.reactivex.Observable

interface CreateMeetingView : BaseView {
  fun render(uiModel: UiModel<ClientData>)
  fun renderAddress(uiModel: UiModel<String>)
  fun renderSaveRequest(uiModel: UiModel<AddUpdateMeeting>)
  fun renderMeetingReason(uiModel: UiModel<List<LookUp>>)
  fun renderSubCategory(uiModel: UiModel<List<LookUp>>)
  fun renderCustomerServiceData(uiModel: UiModel<Pair<List<LookUp>, List<LookUp>>>)
  fun renderFranchiseIFAUserData(uiModel: UiModel<Pair<List<LookUp>, List<LookUp>>>)
  fun renderSearchResult(
    uiModel: UiModel<List<Attendee>>,
    isAttendee: Boolean
  )
  fun renderOTP(uiModel: UiModel<String>)
  fun onMeetingLocationSelected(): Observable<Int>
  fun onMeetingReasonSelected(): Observable<Int>
  fun onActivityTypeSelected(): Observable<Int>
  fun onCategorySelected(): Observable<Int>
  fun onSendOTPClicked(): Observable<Unit>
  fun getSelectedMeetingLocation(): LookUp?
  fun getSelectedActionable(): LookUp?
  fun getSelectedBAClient(): LookUp?
  fun getSelectedOutstation(): LookUp?
  fun getSelectedMeetingDuration(): LookUp?
  fun getSelectedMeetingObjective(): LookUp?
  fun getSelectedMeetingReason(): LookUp?
  fun getSelectedActivityType(): LookUp?
  fun getSelectedCategory(): LookUp?
  fun getSelectedSubCategory(): LookUp?
  fun getPincode(): String
  fun getAmount(): String
  fun getMeetingAddress(): String
  fun getMeetingDesc(): String
  fun getCustomerComment(): String
  fun getCategory(): String
  fun getSubCategory(): String
  fun getAttendeeSearchText(): Observable<SearchTextChange>
  fun getDate(): String
  fun getSplAttendeeSearchText(): Observable<SearchTextChangeSplAttendee>
  fun getOTPButtonText(): String
  fun getLatitude(): String
  fun getLongitude(): String
  fun getGeoArea(): String
  fun getClientCode(): String
  fun getClientName(): String
  fun renderDatePicker()
  fun onDatePicker(): Observable<Unit>

  fun onAttendeeClicked(): Observable<Unit>
  fun onAttendeeAddedClicked(): Observable<Unit>
  fun onSplAttendeeClicked(): Observable<Unit>
  fun onSplAttendeeAddedClicked(): Observable<Unit>
  fun onResetClicked(): Observable<Unit>
  fun onSaveClicked(): Observable<Unit>
  fun showAttendeDialog()
  fun showAttendeAddedDialog()
  fun showSplAttendeDialog()
  fun showSplAttendeAddedDialog()
  fun showOTPDialog(
    ucid: String,
    leadId: String
  )

  fun showToast()

  fun onAttendeeItemClickEventFired(): Observable<AttendeeItemClick>
  fun onSplAttendeeItemClickEventFired(): Observable<SplAttendeeItemClick>
  fun renderOnAttendeeItemClick(position: Int)
  fun renderOnSplAttendeeItemClick(position: Int)

  fun toggleOTPVisibility(show: Boolean)
  fun toggleActionableVisibility(show: Boolean)
  fun toggleSearchState(
    queryEmpty: Boolean,
    isAttendee: Boolean
  )
  fun isOTPSuccessful(): Boolean

  fun checkPermissionAndProceed()
  fun sendOTP(): Observable<SendOtpRequest>
  fun isFranchiseIFAUser(): Observable<FranchiseIFAUser>
  fun resetData()
  fun validateFormData(): Boolean
  fun onDataValidated(): Observable<ValidationStatus>
  fun setInputData(meetingApptModel: MeetingApptModel)
  fun onDataSaved(): Observable<SaveRequestData>
  fun onSaveConfirmClick(): Observable<SaveConfirmClick>
}