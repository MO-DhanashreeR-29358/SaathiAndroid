package com.mosl.tabapp.leadViewDetail.createmeeting

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import com.mosl.mobile.domain.createmeeting.OTPRequestData
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.Companion.ARG_LEAD_ID
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.Companion.ARG_UCID
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.Companion.SEND_OTP_LABEL
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment.Companion.VERIFY_OTP_LABEL
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import com.mosl.tabapp.model.MeetingApptModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class CreateMeetingPresenter @Inject constructor(
    view: CreateMeetingView,
    val repository: CreateMeetingRepository,
    private val createMeetingData: CreateMeetingData,
    val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<CreateMeetingView>(view) {
    override fun start() {
        Observable.merge(onFetchCalled(), onRetryCalled())
            .switchMap {
                repository.getInitialPageData(createMeetingData)
                    .compose(applyUiModel())
            }
            .subscribe(view::render)
            .addTo(disposable)

        val activityTypeSelectedObs = view.onActivityTypeSelected()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedActivityType() }
            .share()

        val productSelectedObs = view.onProductSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedProduct() }
            .share()

        val travellingModeSelectedObs = view.onTravellingModeSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedTravellingMode() }
            .share()

        activityTypeSelectedObs.map {
            Timber.d("activity type")
            it?.isPersonalMeeting() ?: false
        }
            .startWithItem(false)
            .subscribe(view::toggleOTPVisibility)
            .addTo(disposable)

        activityTypeSelectedObs
            .filter { it != CreateMeetingConstants.SELECT_ACTIVITY_TYPE }
            .switchMap {
                repository.getLookup(
                    MEETINGREASON, null, view.getSelectedActivityType()?.lookUpID.toString()
                )
                    .compose(applyUiModel())
            }
            .subscribe(view::renderMeetingReason)
            .addTo(disposable)

        view.onChipsApiCall()
            .switchMap {
                repository.getChipsList()
                    .compose(applyUiModel())
            }
            .subscribe(view::renderChipsList)
            .addTo(disposable)

        productSelectedObs.filter { it != CreateMeetingConstants.SELECT_PRODUCT }
            .subscribe {
                view.getSelectedProduct()
                    ?.let { it1 -> view.renderOnProductClick(it1) }
            }
            .addTo(disposable)

        travellingModeSelectedObs.filter { it != CreateMeetingConstants.SELECT_TRAVELLING_MODE }
            .subscribe {
                view.getSelectedTravellingMode()
                    ?.let { it1 -> view.renderOnTravellingModeClick(it1) }
            }
            .addTo(disposable)

        view.onDatePicker()
            .subscribe {
                view.renderDatePicker()
            }
            .addTo(disposable)

        view.onMeetingLocationSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedMeetingLocation() }
            .filter {
                it != CreateMeetingConstants.SELECT_MEETING_LOCATION/* &&
                        it?.isLocationProper() ?: false*/
            }
            .switchMap {
                repository.getAddress(it?.lookUpID.toString(), createMeetingData.leadId)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderAddress)
            .addTo(disposable)

        val meetingReasonObs = view.onMeetingReasonSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedMeetingReason() }
            .filter { it != CreateMeetingConstants.SELECT_MEETING_REASON }
            .share()

        view.onMobileNumberSelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getMobileNumberType() }
            .filter { it != CreateMeetingConstants.SELECT_MOBILE_NUMBER }
            .subscribe { view.onMobileClick() }
            .addTo(disposable)

        meetingReasonObs.map {
            it?.isCustomerService() ?: false
        }
            .startWithItem(false)
            .subscribe(view::toggleActionableVisibility)
            .addTo(disposable)

        meetingReasonObs.filter { it?.isCustomerService() ?: false }
            .switchMap {
                repository.getCustomerServiceData()
                    .compose(applyUiModel())
            }
            .subscribe(view::renderCustomerServiceData)
            .addTo(disposable)

        view.onCategorySelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedCategory() }
            .switchMap {
                repository.getLookup(SUBCATEGORY, null, it?.lookUpID.toString())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderSubCategory)
            .addTo(disposable)

        view.onTouchedUntouchedEventFired()
            .switchMap {
                repository.getLookup(TOUCHEDUNTOUCHED, null, null)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderTouchedUntouchedList)
            .addTo(disposable)

        view.onFeedbackEventFired()
            .switchMap {
                repository.getLookup(FEEDBACK, null, null)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderFeedbackList)
            .addTo(disposable)

        view.onCategorySelected()
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .map { view.getSelectedCategory() }
            .switchMap {
                repository.getLookup(SUBCATEGORY, null, it?.lookUpID.toString())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderSubCategory)
            .addTo(disposable)


        view.onAttendeeClicked()
            .subscribe {
                view.showAttendeDialog()
            }
            .addTo(disposable)

        view.onSplAttendeeClicked()
            .subscribe {
                view.showSplAttendeDialog()
            }
            .addTo(disposable)

        val searchObs = view.getAttendeeSearchText()
            .filter { it.text.isNotEmpty() }
            .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
            .map { it.text }
            .distinctUntilChanged()
            .share()

        searchObs?.map(MapStringIsBlank)
            ?.subscribe {
                view.toggleSearchState(it, true)
            }
            ?.addTo(disposable)

        searchObs?.filter { it.length > 2 }
            ?.switchMap {
                repository.getAttendeeData(it)
                    .compose(applyUiModel())
            }
            ?.subscribe { view.renderSearchResult(it, true) }
            ?.addTo(disposable)

        val searchSplAttendeeObs = view.getSplAttendeeSearchText()
            .filter { it.text.isNotEmpty() }
            .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
            .map { it.text }
            .distinctUntilChanged()
            .share()

        searchSplAttendeeObs?.map(MapStringIsBlank)
            ?.subscribe {
                view.toggleSearchState(it, false)
            }
            ?.addTo(disposable)

        searchSplAttendeeObs?.filter { it.length > 2 }
            ?.switchMap {
                repository.getSplAttendeeData(it)
                    .compose(applyUiModel())
            }
            ?.subscribe { view.renderSearchResult(it, false) }
            ?.addTo(disposable)

        view.onAttendeeAddedClicked()
            .subscribe { view.showAttendeAddedDialog() }
            .addTo(disposable)

        view.onSplAttendeeAddedClicked()
            .subscribe { view.showSplAttendeAddedDialog() }
            .addTo(disposable)

        var isOTPSentSuccessful = view.onSendOTPClicked()
            .filter { view.validateMobileNumber() }
            .map {
                view.isOTPSuccessful()
            }
            .share()

        isOTPSentSuccessful.filter { t: Boolean ->
            t
        }
            .subscribe { view.showToast() }
            .addTo(disposable)

    isOTPSentSuccessful.filter { t: Boolean -> !t }
        .map { view.getOTPButtonText() }
        .filter { t -> t == SEND_OTP_LABEL }
        .subscribe { view.checkPermissionAndProceed() }
        .addTo(disposable)

        isOTPSentSuccessful.filter { t: Boolean -> !t }
            .map { view.getOTPButtonText() }
            .filter { t -> t == VERIFY_OTP_LABEL }
            .subscribe {
                view.showOTPDialog(
                    createMeetingData.ucid,
                    createMeetingData.leadId,
                    view.getMobileNumberType()?.contactNumber.toString()
                )
            }
            .addTo(disposable)

        view.onAttendeeItemClickEventFired()
            .map {
                it.position
            }
            .subscribe(view::renderOnAttendeeItemClick)
            .addTo(disposable)

        view.onSplAttendeeItemClickEventFired()
            .map {
                it.position
            }
            .subscribe(view::renderOnSplAttendeeItemClick)
            .addTo(disposable)

        view.sendOTP()
            .switchMap {
                repository.sendOTP(
                    OTPRequestData(
                        latitude = view.getLatitude(),
                        longitude = view.getLongitude(),
                        geoArea = view.getGeoArea(),
                        ucid = createMeetingData.ucid,
                        leadid = createMeetingData.leadId,
                        mobileNumber = view.getMobileNumberType()?.contactNumber.toString()
                    )
                )
                    .compose(applyUiModel())
            }
            .subscribe(view::renderOTP)
            .addTo(disposable)

        view.onResetClicked()
            .subscribe { view.resetData() }
            .addTo(disposable)

        view.onSaveClicked()
            .subscribe { view.validateFormData() }
            .addTo(disposable)

        view.onDataValidated()
            .filter { it.boolean }
            .map { getInputData() }
            .subscribe { view.setInputData(it) }
            .addTo(disposable)

        view.onDataSaved()
            .switchMap {
                repository.saveRequest(it.data)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderSaveRequest)
            .addTo(disposable)

    view.onSaveConfirmClick()
        .subscribe {
          val bundle = Bundle()
          bundle.putString(ARG_LEAD_ID, createMeetingData.leadId)
          bundle.putString(ARG_UCID, createMeetingData.ucid)
          screenNavigator.openPage(
              NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER.toString(), bundle,
              addToStack = true,
              forceReload = false
          )
        }
        .addTo(disposable)

        view.onLocationSaveEventFired()
            .subscribe {
                view.renderLocationSave(it.data)
            }
            .addTo(disposable)
  }

    private fun getInputData(): MeetingApptModel {
        val attendeeList = if (view.getAttendeeList()
                .isEmpty()
        ) {
            emptyList()
        } else view.getAttendeeList()
            .map {
                AddedAttendeeModel(it.attendeeType, it.attended, it.attendeeName, it.attendeeCode)
            }

        val splAttendeeList = if (view.getSplAttendeeList()
                .isEmpty()
        ) {
            emptyList()
        } else
            view.getSplAttendeeList()
                .map {
                    AddedSplAttendeeModel(
                        it.attendeeType,
                        it.attended,
                        it.attendeeName,
                        it.attendeeCode
                    )
                }

        val data = MeetingApptModel()
        data.meetingType = "M"
        data.displayType = "A"
        data.pincode = view.getPincode()
        data.amount = view.getAmount()
        data.meetingAddress = view.getMeetingAddress()
        data.meetingAddressTypeId = view.getSelectedMeetingLocation()?.lookUpID.toString()
        data.leadId = createMeetingData.leadId
        data.ucid = createMeetingData.ucid
        data.meetingDate = view.getDate()
        data.product = "0"
        data.setProductList(view.getproductList())
        data.meetingDuration = view.getSelectedMeetingDuration()?.lookUpID.toString()
        data.meetingObjectiveID = view.getSelectedMeetingLocation()?.lookUpID.toString()
        data.activityTypeID = view.getSelectedActivityType()?.lookUpID.toString()
        data.meetingReason = view.getSelectedMeetingReason()?.lookUpID.toString()
        data.meetingDes = CRMAppUtil.getJsonSafeString(view.getMeetingDesc())
        data.meetingAttendee = attendeeList
        data.splMeetingAttendee = splAttendeeList
        data.travellingList = view.getTravellingmodeList()
        data.customerComment = CRMAppUtil.getJsonSafeString(view.getCustomerComment())
        data.actionables = view.getSelectedActionable()?.lookUpID.toString()
        data.category = view.getSelectedCategory()?.lookUpID.toString()
        data.etCategory = view.getCategory()
        data.subCategory = view.getSelectedSubCategory()?.lookUpID.toString()
        data.etSubCategory = view.getSubCategory()
        data.geoArea = view.getGeoArea()
        data.geoLatitude = view.getLatitude()
        data.geoLongitude = view.getLongitude()
        data.touchedUntouchedId = view.getSelectedTouchedUntouched()?.lookUpID.toString()
        data.feedbackId = view.getSelectedFeedback()?.lookUpID.toString()
        data.meetingEventId = view.getSelectedEvent()
        return data
    }

  companion object {
    const val MEETINGREASON = 14
    const val SUBCATEGORY = 22
    const val TOUCHEDUNTOUCHED = 132
    const val FEEDBACK = 133
  }

}
