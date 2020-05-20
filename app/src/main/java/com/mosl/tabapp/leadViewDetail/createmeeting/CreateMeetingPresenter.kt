package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.CharSequenceToString
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class CreateMeetingPresenter @Inject constructor(
  view: CreateMeetingView,
  val repository: CreateMeetingRepository,
  val createMeetingData: CreateMeetingData
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
        .map { t -> view.getSelectedActivityType() }
        .share()

    activityTypeSelectedObs.map {
      Timber.d("activity type")
      it.isPersonalMeeting()
    }
        .startWith(false)
        .subscribe(view::toggleOTPVisibility)
        .addTo(disposable)

    activityTypeSelectedObs
        .filter { !it.equals(CreateMeetingConstants.SELECT_ACTIVITY_TYPE) }
        .switchMap {
          repository.getLookup(
              MEETINGREASON, null, view.getSelectedActivityType()?.lookUpID.toString()
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderMeetingReason)
        .addTo(disposable)

    val meetingLocationObs = view.onMeetingLocationSelected()
        .filter(InvalidSelectionPredicate)
        .map { t -> view.getSelectedMeetingLocation() }
        .share()
    meetingLocationObs.map {
      Timber.d("location %s", it.isLocationProper())
      it.isLocationProper()/*  && !it.equals(CreateMeetingConstants.SELECT_MEETING_LOCATION)*/
    }
//        .filter {
//          !it.equals(
//              CreateMeetingConstants.SELECT_MEETING_LOCATION)
////              && !it.lookUpName.equals("Other", ignoreCase = false)
//        }
        .switchMap {
          repository.getAddress(
              view.getSelectedMeetingLocation()?.lookUpID.toString(), createMeetingData.leadId
          )
              .compose(applyUiModel())
        }
        .subscribe(view::renderAddress)
        .addTo(disposable)

//    view.onMeetingReasonSelected()
//        .filter(InvalidSelectionPredicate)
////        .map { t -> view.getSelectedMeetingReason() }
    val meetingReasonObs = view.onMeetingReasonSelected()
        .filter(InvalidSelectionPredicate)
        .map { t -> view.getSelectedMeetingReason() }
        .share()

    meetingReasonObs.map {
      Timber.d("customer service ")
      it.isCustomerService()
    }
        .startWith(false)
        .subscribe(view::toggleActionableVisibility)
        .addTo(disposable)

    meetingReasonObs.map { it.isCustomerService() }
        .switchMap {
          repository.getCustomerServiceData()
              .compose(applyUiModel())
        }
        .subscribe(view::renderCustomerServiceData)
        .addTo(disposable)

    view.onCategorySelected()
        .filter(InvalidSelectionPredicate)
        .map { view.getSelectedCategory() }
        .switchMap {
          repository.getLookup(SUBCATEGORY, null, it.lookUpID.toString())
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

    val searchObs = view.getSearchText()
        .filter { t -> t.isNotEmpty() && t.isNotBlank() }
        .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
        .map(CharSequenceToString)
        .distinctUntilChanged()
        .share()

    searchObs?.map(MapStringIsBlank)
        ?.subscribe(view::toggleSearchState)
        ?.addTo(disposable)

    searchObs?.filter { it.length > 2 }
        ?.switchMap {
          repository.getAttendeeData(it)
              .compose(applyUiModel())
        }
        ?.subscribe(view::renderSearchResult)
        ?.addTo(disposable)
  }

  companion object {
    const val MEETINGREASON = 14
    const val SUBCATEGORY = 22
  }

}
