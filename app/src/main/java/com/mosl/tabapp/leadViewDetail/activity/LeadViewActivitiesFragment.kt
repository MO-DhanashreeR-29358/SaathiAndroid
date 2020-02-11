package com.mosl.tabapp.leadViewDetail.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.mosl.app.core.rx.applyUiModel
import com.mosl.saathi.api.model.leadviewactivities.DeleteAppointmentRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadActivitiesBinding
import com.mosl.tabapp.leadViewDetail.LeadViewActivitiesFragment
import com.mosl.tabapp.model.LeadActivitiesAppointModel
import com.mosl.tabapp.model.LeadActivitiesMeetingModel
import java.util.ArrayList

class LeadViewActivitiesFragment : CommonFragment(), OnClickListener {

  private lateinit var binding: FragLeadActivitiesBinding

  private val appointmentList = ArrayList<LeadActivitiesAppointModel>()
  private val meetingList = ArrayList<LeadActivitiesMeetingModel>()

  override fun getLayout(): Int {
    return R.layout.frag_lead_activities
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadActivitiesBinding.bind(view)
  }

  override fun onClick(v: View) {
    when (view?.id) {
      R.id.tvAppointment -> toggleSectionExpansion(binding.llAppointment, binding.tvAppointment)
      R.id.tvMeeting -> toggleSectionExpansion(binding.llMeeting, binding.tvMeeting)
    }

  }

  private fun deleteAppintments(
    meetingId: String,
    url: String
  ) {
    //login parameters
    when {
      CRMAppUtil.isNetworkEnabled(requireContext()) -> {
        val request = DeleteAppointmentRequest(meetingId)
        compositeDisposable.add(saathiApi.deleteAppointments(url, request)
            .compose(applyUiModel<String>())
            .subscribe { uiModel -> renderDeleteAppointments(uiModel, meetingId) })
      }
      else -> noInternetConnectionDialog()
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
  ) : RecyclerView.Adapter<AppointmentAdapter.> {

    class MyViewHolder() : RecyclerView.ViewHolder() {

    }

  }

}