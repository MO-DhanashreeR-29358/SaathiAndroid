package com.mosl.tabapp.calendar.calendarappoinslist

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.data.util.safeMap
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.calendar.CalendarUiEvent
import com.mosl.tabapp.calendar.CalendarUiEvent.CallAPi
import com.mosl.tabapp.calendar.calendarappoinslist.item.ItemCalendarAppointList
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DatePickerBinding
import com.mosl.tabapp.databinding.FragmentCalendarAppointsListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CalendarAppointsListFragment : CommonFragment(), CalendarAppointsListView {
  lateinit var binding: FragmentCalendarAppointsListBinding
  @Inject lateinit var presenter: CalendarAppointsListPresenter
  private val subject = PublishSubject.create<CalendarUiEvent>()
  private lateinit var section: DataManagerSection
  private lateinit var fromPostingDate: String
  private lateinit var fromDat: Date

  override fun getLayout(): Int {
    return R.layout.fragment_calendar_appoints_list
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentCalendarAppointsListBinding.bind(view)
    setUpView()
    setUpRecyclerView()
    presenter.start()

  }

  private fun setUpView() {
    section = DataManagerSection(onRetryClick)
    section.retrySubject = dataActionSubject

    var mainToDate = this.arguments!!.getString(MOSLConstants.DATE)
    mainToDate?.let {
      fromPostingDate = it
      mainToDate = CRMAppUtil.getDateFromString(
        mainToDate, MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING_SEND,
        MOSLConstants.DATE_FORMAT_OF_DD_MMM_YYYY
      )
      binding.tvDate.text = it
    }

    val itemList: List<CalenderItemResponse> =
      arguments?.getSerializable(CalendarFragment.DATA_LIST) as List<CalenderItemResponse>

    if (itemList.isNotEmpty()) {
      section.clearContent()
      section.setContent(itemList.safeMap { item -> ItemCalendarAppointList(item) })
    } else {
      binding.tvNoChart.visibility = View.VISIBLE
    }
  }

  private fun setUpRecyclerView() {
    val groupAdapter = GroupAdapter<ViewHolder>()

    binding.rvCalendarAppointsList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      adapter = groupAdapter
      (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }
    groupAdapter.add(section)
  }

  override fun onDatePicker(): Observable<Unit> {
    return binding.ivDatePicker.clicks()
  }

  override fun callCalendarApi(): Observable<CallAPi> {
    return subject.ofType()
  }

  override fun renderDatePicker() {
    showSelectDateDialog()
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
        binding.tvNoChart.visibility = View.GONE
        section.setContent(it.map { item -> ItemCalendarAppointList(item) })
      } else {
        binding.tvNoChart.visibility = View.VISIBLE
      }
    }
  }

  @SuppressLint("InflateParams")
  private fun showSelectDateDialog() {
    val today = Calendar.getInstance()
    val dialog = Dialog(requireContext())
    dialog.apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

      val dialogBinding =
        DatePickerBinding.bind(
          layoutInflater.inflate(
            R.layout.date_picker,
            null
          )
        )
      setContentView(dialogBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      dialogBinding.also {
        it.linearLayoutOfToDate.visibility = View.GONE
        it.datePickerFromDate.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

        if (!fromPostingDate.isBlank()) {
          try {
            val dateSplit = fromPostingDate.split("-").toTypedArray()
            it.datePickerFromDate.init(
              dateSplit[0].toInt(),
              dateSplit[1].toInt() - 1,
              dateSplit[2].toInt()
            ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
              fromPostingDate =
                year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
            }
          } catch (e: Exception) {
            Timber.d(e, "CalendarAppointsListFragment")
          }
        } else {
          it.datePickerFromDate.init(
            today[Calendar.YEAR], today[Calendar.MONTH],
            today[Calendar.DATE]
          ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            fromPostingDate =
              year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
          }
        }

        it.ibtnClose.setOnClickListener {
          dismiss()
        }

        it.btnSubmit.setOnClickListener { _ ->
          fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd")
          if (!fromPostingDate.isBlank()) {
            fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd")
            fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd")
            binding.tvDate.text =
              CRMAppUtil.getDateStr(fromDat, MOSLConstants.DATE_FORMAT_OF_DD_MMM_YYYY)

            subject.onNext(CallAPi(fromPostingDate))
          }
          dialog.dismiss()
        }
      }
    }.show()

  }

  fun prefix(input: Int): String? {
    return if (input.toString().length == 1) {
      "0$input"
    } else input.toString() + ""
  }

}
