package com.mosl.tabapp.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.LeadActivitiesAppointData
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentCalendarNewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragmentNew : CommonFragment(), CalendarFragmentView, OnDateSelectedListener {
  lateinit var binding: FragmentCalendarNewBinding
  @Inject lateinit var presenter: CalendarFragmentPresenter
  private lateinit var section: DataManagerSection
  private val oneDayDecorator = OneDayDecorator()

  private lateinit var selectionDate: String

  override fun getLayout(): Int {
    return R.layout.fragment_calendar_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentCalendarNewBinding.bind(view)
    setUpView()
    setUpRecyclerView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    binding.apply {
      materialCalendarView.setOnDateChangedListener(this@CalendarFragmentNew)
      materialCalendarView.showOtherDates = MaterialCalendarView.SHOW_ALL

      val instance = Calendar.getInstance()
      materialCalendarView.setSelectedDate(instance.time)

      val instance1 = Calendar.getInstance()
      instance1[instance1[Calendar.YEAR] - 1, Calendar.JANUARY] = 1
      instance1[instance1[Calendar.YEAR], Calendar.JANUARY] = 1

      val instance2 = Calendar.getInstance()
      instance2[instance2[Calendar.YEAR], Calendar.DECEMBER] = 31

      //selectionDate = getSelectedDatesString()
      materialCalendarView.addDecorators(
        MySelectorDecorator(requireActivity()),
        HighlightMonthsDecorator(),
        oneDayDecorator
      )
      tvDay.text = CRMAppUtil.getDBDate(getSelectedDate(), "E MMMM d")
    }
  }

  private fun setUpRecyclerView() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    section = DataManagerSection(onRetryClick)
    section.retrySubject = dataActionSubject
    binding.recyclerView.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(false)
      adapter = groupAdapter
      (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
      isNestedScrollingEnabled = false
    }
    groupAdapter.add(section)
  }

  override fun getSelectedDatesString(): String {
    val date: CalendarDay = binding.materialCalendarView.selectedDate ?: return "No Selection"
    return FORMATTER.format(date.date)
  }

  override fun getSelectedDate(): Date? {
    val date: CalendarDay = binding.materialCalendarView.selectedDate ?: return null
    return date.date
  }

  override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
    dataActionSubject.onNext(DataAction.Fetch)
    oneDayDecorator.setDate(date.date)
    binding.materialCalendarView.invalidateDecorators()
  }

  override fun renderCalendar(uiModel: UiModel<List<CalenderItemResponse>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      section.clearContent()
      if (it.isNotEmpty()) {
        Timber.d("Success")
        binding.tvNochart.visibility = View.GONE
        for (item in it) {
          val itemData = LeadActivitiesAppointData(
            status = item.status,
            meetingId = item.meetingID,
            leadId = item.leadId,
            name = item.name,
            email = item.email,
            mobNo = item.mobNo,
            date = item.dateDisplay,
            time = item.meetingDate,
            attendeeEmp = item.attendeeEmp,
            product = item.product,
            statusName = item.statusName,
            activityType = item.activityType,
            meetingReason = item.meetingReason,
            meetingDuration = item.meetingDuration,
            category = item.category,
            actionable = item.actionable,
            comments = item.comments,
            stage = item.stage,
            callback = item.stage,
            queryDesc = item.queryDesc,
            ucId = item.leadId,
            createdBy = item.createdBy,
            repeatOnCode = item.repetitionEndsOn
          )
          section.setContent(ItemCalendarFragment(itemData))
        }

      } else {
        binding.tvNochart.visibility = View.VISIBLE
      }

    }
  }

  companion object {
    val FORMATTER = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  }
}
