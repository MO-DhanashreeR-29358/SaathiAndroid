package com.mosl.tabapp.leadViewDetail.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentLeadActivitiesBinding
import com.mosl.tabapp.leadViewDetail.LeadViewActivitiesFragment
import com.mosl.tabapp.model.LeadActivitiesAppointModel
import com.mosl.tabapp.model.LeadActivitiesMeetingModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import java.util.ArrayList
import javax.inject.Inject

class LeadViewActivitiesFragment : CommonFragment(), LeadViewActivitiesView {

  private lateinit var binding: FragmentLeadActivitiesBinding

  @Inject
  internal lateinit var presenter: LeadViewActivitiesPresenter

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

  private fun setUpRecyclerView() {
    appointmentSection = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>().apply {
      add(appointmentSection)
    }

  }

  companion object {
    var leadId = "0"
    var mUcid = "0"

    fun newInstance(
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

  private class AppointmentAdapter(
    val appointmentList: List<LeadActivitiesAppointModel>
  ) : RecyclerView.Adapter<AppointmentAdapter.MyViewHolder1>() {

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): MyViewHolder1 {
      TODO(
          "not implemented"
      ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
      TODO(
          "not implemented"
      ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(
      holder: MyViewHolder1,
      position: Int
    ) {
      TODO(
          "not implemented"
      ) //To change body of created functions use File | Settings | File Templates.
    }

    class MyViewHolder1(
      val view: View
    ) : RecyclerView.ViewHolder(view) {

    }

  }

}