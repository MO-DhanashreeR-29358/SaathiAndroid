package com.mosl.tabapp.leadViewDetail.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityData
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.leadviewdetail.activities.Meeting
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogAppointmentsBinding
import com.mosl.tabapp.databinding.FragmentLeadActivitiesBinding
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.*
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesAppointmentChildItem
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesItem
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesMeetingChildItem
import com.mosl.tabapp.leadViewDetail.activity.item.SingleTextItem
import com.mosl.tabapp.ui.CustomDialog
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class LeadViewActivitiesFragment : CommonFragment(), LeadViewActivitiesView {

  private lateinit var binding: FragmentLeadActivitiesBinding

  @Inject
  internal lateinit var presenter: LeadViewActivitiesPresenter

  private var pager: ViewPager? = null
  private val subject = PublishSubject.create<LeadViewActivitiesUiEvent>()
  private var listOfAppointment: List<Appointment> = emptyList()
  private var listOfMeeting: List<Meeting> = emptyList()

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
    val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
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
  override fun onRescheduleEventFired(): Observable<RescheduleEvent> = subject.ofType()
  override fun onUpdateEventFired(): Observable<UpdateEvent> = subject.ofType()
  override fun onDeleteEventFired(): Observable<DeleteEvent> = subject.ofType()

  private fun DataManagerSection.addEmptyItemInExpandable(
    expandableTitle: String,
    emptyText: String
  ) = update(listOf(
      ExpandableGroup(LeadViewActivitiesItem(expandableTitle)).apply {
        add(SingleTextItem(emptyText))
      }
  ))

  override fun getActivityRequest(): ActivityRequest {
    if(mUcid.equals("null",ignoreCase = true)){
      mUcid = "0"
    }
    return ActivityRequest(
        leadId = leadId,
        ucid = mUcid.ifEmpty { "0" }
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
        else -> {
          listOfAppointment = data.listOfAppointment
          appointmentSection.update(listOf(
              ExpandableGroup(LeadViewActivitiesItem("Appointment")).apply {
                addAll(data.listOfAppointment.map { appointment ->
                  LeadViewActivitiesAppointmentChildItem(appointment)
                })
              }
          ))
        }
      }



      when {
        data.listOfMeeting.isEmpty() -> meetingSection.addEmptyItemInExpandable(
            "Meeting", "No Meeting data"
        )

        else -> {
          listOfMeeting = data.listOfMeeting
          meetingSection.update(listOf(
              ExpandableGroup(LeadViewActivitiesItem("Meeting")).apply {
                addAll(data.listOfMeeting.map { meeting ->
                  LeadViewActivitiesMeetingChildItem(meeting)
                })
              }
          ))
        }
      }

    }

  }

  override fun renderAppointmentClicked(data: Appointment) {
    if (userManager.user.employeeCode == data.createdBy) {
      openLeadViewDialog(data)
    }
  }

  override fun renderDeleteAppointment(
    uiModel: UiModel<String>,
    meetingId: String
  ) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      alertDialog(
          activity, 0,
          resources.getString(R.string.motilal_oswal),
          APPT_CANCELLED, "OK", true
      ) { _, _ -> }

      val list = listOfAppointment.filter { it.meetingId != meetingId }
      listOfAppointment = list
      when {
        listOfAppointment.isNotEmpty() -> appointmentSection.update(listOf(
            ExpandableGroup(LeadViewActivitiesItem("Appointment")).apply {
              addAll(listOfAppointment.map { appointment ->
                LeadViewActivitiesAppointmentChildItem(appointment)
              })
            }
        ))
        else -> appointmentSection.addEmptyItemInExpandable(
            "Appointment", "No Appointment data"
        )
      }
    }
  }

  private fun openLeadViewDialog(appointment: Appointment) {
    val viewBinding: DialogAppointmentsBinding =
      DialogAppointmentsBinding.bind(layoutInflater.inflate(R.layout.dialog_appointments, null))

    val dialog = Dialog(requireContext()).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setCanceledOnTouchOutside(false)
      setContentView(viewBinding.root)
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    val date = CRMAppUtil.getDate("${appointment.date} ${appointment.time}", "dd MMM yyyy hh:mma")
    viewBinding.ivUpdateMeeting.visibility = when {
      CRMAppUtil.isBeforeDate(date) -> View.VISIBLE
      else -> View.GONE
    }

    viewBinding.ivRescheduleMeeting.setOnClickListener {
      dialog.dismiss()
      subject.onNext(
          RescheduleEvent(
              data = appointment.copy(
                  ucid = mUcid
              )
          )
      )

    }

    viewBinding.ivUpdateMeeting.setOnClickListener {
      dialog.dismiss()
      subject.onNext(
          UpdateEvent(
              data = appointment.copy(
                  ucid = mUcid
              )
          )
      )

    }

    viewBinding.ivCancelMeeting.setOnClickListener {
      dialog.dismiss()
      when {
        CRMAppUtil.isBlank(appointment.repeatOnCode) -> {

          val customDialog = CustomDialog(
              requireContext(),
              "Are you sure you want to cancel this appointment ?",
              true,
              true,
              true,
              "Cancel Appointment",
              YES,
              NO
          ) { action: MSFDialog.Action, _ ->
            if (action == MSFDialog.Action.OK) {

              subject.onNext(DeleteEvent(appointment.meetingId, "DeactivateMeeting"))
            }

          }
          customDialog.show()
        }
        else -> {
          val customDialog = CustomDialog(
              requireContext(),
              "Are you sure you want to cancel this appointment ?",
              true,
              true,
              true,
              "Cancel Appointment",
              "Current",
              "All"
          ) { action: MSFDialog.Action, _ ->
            when (action) {
              MSFDialog.Action.OK -> subject.onNext(
                  DeleteEvent(appointment.meetingId, "DeactivateMeeting")
              )
              MSFDialog.Action.CANCEL -> subject.onNext(
                  DeleteEvent(appointment.meetingId, "DeactivateALLMeeting")
              )
            }

          }
          customDialog.show()

        }
      }
    }

    viewBinding.ivClose.setOnClickListener {
      dialog.dismiss()
    }

    dialog.show()

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