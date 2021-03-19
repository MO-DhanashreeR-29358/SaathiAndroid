package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.*
import com.mosl.tabapp.leadViewDetail.CreateMeetingProductModel
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent.*
import com.mosl.tabapp.model.MeetingApptModel
import io.reactivex.Observable

interface CreateMeetingView : BaseView {
  fun render(uiModel: UiModel<ClientData>)
  fun renderAddress(uiModel: UiModel<String>)
  fun renderSaveRequest(uiModel: UiModel<AddUpdateMeeting>)
  fun renderMeetingReason(uiModel: UiModel<List<LookUp>>)
  fun renderSubCategory(uiModel: UiModel<List<LookUp>>)
  fun renderCustomerServiceData(uiModel: UiModel<Pair<List<LookUp>, List<LookUp>>>)
  fun renderSearchResult(
    uiModel: UiModel<List<Attendee>>,
    isAttendee: Boolean
  )

  fun renderOTP(uiModel: UiModel<String>)
  fun onTravellingModeSelected(): Observable<Int>
  fun onMeetingLocationSelected(): Observable<Int>
  fun onMeetingReasonSelected(): Observable<Int>
  fun onMobileNumberSelected(): Observable<Int>
  fun onActivityTypeSelected(): Observable<Int>
  fun onProductSelected(): Observable<Int>
  fun onCategorySelected(): Observable<Int>
  fun onSendOTPClicked(): Observable<Unit>
  fun getSelectedMeetingLocation(): LookUp?
  fun getSelectedTravellingMode(): LookUp?
  fun getSelectedActionable(): LookUp?
  fun getSelectedMeetingDuration(): LookUp?
  fun getSelectedMeetingObjective(): LookUp?
  fun getSelectedMeetingReason(): LookUp?
  fun getSelectedActivityType(): LookUp?
  fun getMobileNumberType(): MobileListData?
  fun getSelectedProduct(): LookUp?
  fun getSelectedCategory(): LookUp?
  fun getSelectedSubCategory(): LookUp?
  fun getPincode(): String?
  fun getAmount(): String?
  fun getMeetingAddress(): String?
  fun getMeetingDesc(): String
  fun getCustomerComment(): String?
  fun getCategory(): String
  fun getSubCategory(): String?
  fun getAttendeeSearchText(): Observable<SearchTextChange>
  fun getDate(): String
  fun getSplAttendeeSearchText(): Observable<SearchTextChangeSplAttendee>
  fun getOTPButtonText(): String
  fun getLatitude(): String
  fun getLongitude(): String
  fun getAttendeeList(): List<AddedAttendee>
  fun getSplAttendeeList(): List<AddedAttendee>
  fun getTravellingmodeList(): List<CreateMeetingTravellingModel>
  fun getproductList(): List<CreateMeetingProductModel>
  fun getGeoArea(): String
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
    leadId: String,
    mobileNumber: String
  )

  fun showToast()

  fun onAttendeeItemClickEventFired(): Observable<AttendeeItemClick>
  fun onSplAttendeeItemClickEventFired(): Observable<SplAttendeeItemClick>
  fun renderOnAttendeeItemClick(position: Int)
  fun renderOnSplAttendeeItemClick(position: Int)
  fun renderOnProductClick(lookUp: LookUp)
  fun renderOnTravellingModeClick(lookUp: LookUp)

  fun toggleOTPVisibility(show: Boolean)
  fun onMobileClick()
  fun toggleActionableVisibility(show: Boolean)
  fun toggleSearchState(
    queryEmpty: Boolean,
    isAttendee: Boolean
  )

  fun isOTPSuccessful(): Boolean

  fun checkPermissionAndProceed()
  fun sendOTP(): Observable<SendOtpRequest>
  fun resetData()
  fun validateFormData(): Boolean
  fun validateMobileNumber(): Boolean
  fun onDataValidated(): Observable<ValidationStatus>
  fun setInputData(meetingApptModel: MeetingApptModel)
  fun onDataSaved(): Observable<SaveRequestData>
  fun onSaveConfirmClick(): Observable<SaveConfirmClick>
}