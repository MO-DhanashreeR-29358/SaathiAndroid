package com.mosl.tabapp.leadViewDetail.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.mosl.commons.data.UiModel
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentLeadActivitiesBinding
import com.mosl.tabapp.leadViewDetail.activity.item.LeadViewActivitiesItem
import com.mosl.tabapp.leadViewDetail.activity.item.SingleTextItem
import com.mosl.tabapp.model.LeadActivitiesAppointModel
import com.mosl.tabapp.model.LeadActivitiesMeetingModel
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import java.util.ArrayList
import javax.inject.Inject

class LeadViewActivitiesFragment : CommonFragment(), LeadViewActivitiesView {

  private lateinit var binding: FragmentLeadActivitiesBinding

  @Inject
  internal lateinit var presenter: LeadViewActivitiesPresenter

  private var pager: ViewPager? = null

  private lateinit var appointmentSection: DataManagerSection
  private lateinit var meetingSection: DataManagerSection

  private val appointmentList = ArrayList<LeadActivitiesAppointModel>()
  private val meetingList = ArrayList<LeadActivitiesMeetingModel>()

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
  }

  override fun render(uiModel: UiModel<String>) {

  }

  fun setViewPager(viewPager: ViewPager) {
    pager = viewPager
  }

  private fun setUpRecyclerView() {
    appointmentSection = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>().apply {
      add(appointmentSection)
      add(meetingSection)
    }

    binding.rvActivities.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = groupAdapter
      setHasFixedSize(true)
    }

    appointmentSection.update(listOf(
        ExpandableGroup(LeadViewActivitiesItem("Appointment")).apply {
          add(SingleTextItem("No Appointment data"))
        }
    ))

    meetingSection.update(listOf(
        ExpandableGroup(LeadViewActivitiesItem("Meeting")).apply {
          add(SingleTextItem("No Meeting data"))
        }
    ))

  }

  companion object {
    var leadId = "0"
    var mUcid = "0"

    @JvmStatic
    public fun newInstance(
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