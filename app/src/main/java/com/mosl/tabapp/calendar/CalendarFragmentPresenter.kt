package com.mosl.tabapp.calendar

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.calendar.CalendarRepository
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import javax.inject.Inject

class CalendarFragmentPresenter @Inject constructor(
  view: CalendarFragmentView,
  val repository: CalendarRepository,
  val userManager: UserManagerImpl
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
  }

  private fun getCalendarRequest(): CalenderRequestInput {
    return CalenderRequestInput(
      meetingType = "A",
      meetingDate = view.getSelectedDatesString(),
      attendeeCode = userManager.user.employeeCode
    )
  }

}