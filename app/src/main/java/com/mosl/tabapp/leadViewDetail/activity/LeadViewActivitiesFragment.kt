package com.mosl.tabapp.leadViewDetail.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityData
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentLeadActivitiesBinding
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.AppointmentEvent
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.MeetingEvent
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesAppointmentChildItem
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesItem
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesMeetingChildItem
import com.mosl.tabapp.leadViewDetail.activity.item.SingleTextItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LeadViewActivitiesFragment : CommonFragment(), LeadViewActivitiesView {

  private lateinit var binding: FragmentLeadActivitiesBinding

  @Inject
  internal lateinit var presenter: LeadViewActivitiesPresenter

  private var pager: ViewPager? = null
  private val subject = PublishSubject.create<LeadViewActivitiesUiEvent>()

  private lateinit var appointmentSection: DataManagerSection
  private lateinit var meetingSection: DataManagerSection

  override fun getLayout(): Int {
    return R.layout.fragment_lead_activities
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentLeadActivitiesBinding.bind(view)
    setUpRecyclerView()
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  fun setViewPager(viewPager: ViewPager) {
    pager = viewPager
  }

  private fun setUpRecyclerView() {
    appointmentSection = DataManagerSection(onRetryClick)
    meetingSection = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>().apply {
      add(appointmentSection)
      add(meetingSection)
    }

    binding.rvActivities.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = groupAdapter
      setHasFixedSize(true)
    }

    groupAdapter.setOnItemClickListener { item, _ ->
      when (item) {
        is LeadViewActivitiesAppointmentChildItem -> subject.onNext(AppointmentEvent(item.data))
        is LeadViewActivitiesMeetingChildItem -> subject.onNext(MeetingEvent(item.data))
      }
    }

    appointmentSection.addEmptyItemInExpandable("Appointment", "No Appointment data")
    meetingSection.addEmptyItemInExpandable("Meeting", "No Meeting data")

  }

  override fun onAppointmentEventFired(): Observable<AppointmentEvent> = subject.ofType()
  override fun onMeetingEventFired(): Observable<MeetingEvent> = subject.ofType()

  private fun DataManagerSection.addEmptyItemInExpandable(
    expandableTitle: String,
    emptyText: String
  ) = update(listOf(
      ExpandableGroup(LeadViewActivitiesItem(expandableTitle)).apply {
        add(SingleTextItem(emptyText))
      }
  ))

  override fun getActivityRequest(): ActivityRequest {
    return ActivityRequest(
        leadId = leadId,
        ucid = mUcid
    )
  }

  override fun render(uiModel: UiModel<ActivityData>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { data ->

      when {
        data.listOfAppointment.isEmpty() -> appointmentSection.addEmptyItemInExpandable(
            "Appointment", "No Appointment data"
        )
        else -> appointmentSection.update(listOf(
            ExpandableGroup(LeadViewActivitiesItem("Appointment")).apply {
              addAll(data.listOfAppointment.map { appointment ->
                LeadViewActivitiesAppointmentChildItem(appointment)
              })
            }
        ))
      }



      when {
        data.listOfMeeting.isEmpty() -> meetingSection.addEmptyItemInExpandable(
            "Meeting", "No Meeting data"
        )

        else -> meetingSection.update(listOf(
            ExpandableGroup(LeadViewActivitiesItem("Meeting")).apply {
              addAll(data.listOfMeeting.map { meeting ->
                LeadViewActivitiesMeetingChildItem(meeting)
              })
            }
        ))
      }

    }

  }

  companion object {
    var leadId = "0"
    var mUcid = "0"

    @JvmStatic fun newInstance(
      position: Int,
      type: String,
      leadID: String,
      ucid: String
    ): LeadViewActivitiesFragment {

      leadId = leadID
      mUcid = ucid
      val args = Bundle().apply {
        putInt("Position", position)
        putString("Type", type)
        putString("LeadID", leadID)
        putString(UCID, ucid)
      }
      return LeadViewActivitiesFragment().apply {
        arguments = args
      }
    }
  }

}