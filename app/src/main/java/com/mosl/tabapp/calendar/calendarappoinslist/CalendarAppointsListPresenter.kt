package com.mosl.tabapp.calendar.calendarappoinslist

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.calendar.CalendarRepository
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import javax.inject.Inject

class CalendarAppointsListPresenter @Inject constructor(
  view: CalendarAppointsListView,
  val repository: CalendarRepository,
  val userManager: UserManagerImpl
) : BasePresenter<CalendarAppointsListView>(view) {
  override fun start() {

    view.onDatePicker()
      .subscribe {
        view.renderDatePicker()
      }
      .addTo(disposable)

    view.callCalendarApi()
      .map {
        getCalendarRequest(it.selectionDate)
      }
      .switchMap {
        repository.postCalenderByDate(it)
          .compose(applyUiModel())
      }.subscribe {
        view.renderCalendar(it)
      }
      .addTo(disposable)
  }

  private fun getCalendarRequest(selectionDate: String): CalenderRequestInput {
    return CalenderRequestInput(
      meetingType = "A",
      meetingDate = selectionDate,
      attendeeCode = userManager.user.employeeCode
    )
  }
}