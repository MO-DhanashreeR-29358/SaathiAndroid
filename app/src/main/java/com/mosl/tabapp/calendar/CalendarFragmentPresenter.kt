package com.mosl.tabapp.calendar

import android.os.Bundle
import android.os.Parcelable
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.calendar.CalendarRepository
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.calendar.CalendarFragmentNew.Companion.DATA_LIST
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import java.io.Serializable
import javax.inject.Inject

class CalendarFragmentPresenter @Inject constructor(
  view: CalendarFragmentView,
  val repository: CalendarRepository,
  val userManager: UserManagerImpl,
  val navigator: ScreenNavigatorImpl
) : BasePresenter<CalendarFragmentView>(view) {
  override fun start() {
    onFetchCalled()
      .map {
        getCalendarRequest()
      }
      .switchMap {
        repository.postCalenderByDate(it)
          .compose(applyUiModel())
      }.subscribe {
        view.renderCalendar(it)
      }
      .addTo(disposable)

    view.onItemClicked()
      .map {
        it.data
      }
      .subscribe {
        //view.navigateToChecklistFragment(it.data)
        val bundle = Bundle()
        bundle.putString(MOSLConstants.DATE, view.getSelectedDatesString())
        bundle.putString(MOSLConstants.NAME, it.name)
        bundle.putString(MOSLConstants.TIME, it.meetingDate)
        bundle.putSerializable(DATA_LIST, view.getResponseList() as Serializable)
        navigator.openPage(NavigationConstants.NAVIGATE_TO_CALENDER_LIST, bundle, true)
      }
      .addTo(disposable)
  }

  private fun getCalendarRequest(): CalenderRequestInput {
    return CalenderRequestInput(
      meetingType = "A",
      meetingDate = view.getSelectedDatesString(),
      attendeeCode = userManager.user.employeeCode
    )
  }

}