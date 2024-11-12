package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryFragment
import com.mosl.tabapp.leadViewDetail.leaddetail.LeadDetailFragment
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfilingFragment

class IndirectLeadDetailMainStateAdapter(
  activity: AppCompatActivity,
  val leadId: String = "0",
  val ucId: String = "0"
) : FragmentStateAdapter(activity) {

  override fun getItemCount(): Int = 3

  override fun createFragment(position: Int): Fragment {

    return when (position) {
      0 -> LeadDetailFragment.newInstance(0, MOSLConstants.DETAILS, leadId, ucId)
      1 -> LeadViewActivitiesFragment.newInstance(1, MOSLConstants.ACTIVITIES, leadId, ucId)
      else -> {
            MappingHistoryFragment.newInstance(2, MOSLConstants.MAPPING_HISTORY, leadId, ucId)
      }
    }

  }
}