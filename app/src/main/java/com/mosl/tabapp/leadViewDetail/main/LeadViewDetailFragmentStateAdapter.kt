package com.mosl.tabapp.leadViewDetail.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadViewDetail.LeadViewInteractionFragment
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesFragment
import com.mosl.tabapp.leadViewDetail.history.MappingHistoryFragment
import com.mosl.tabapp.leadViewDetail.leaddetail.LeadDetailFragment
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfilingFragment

class LeadViewDetailFragmentStateAdapter(
  activity: AppCompatActivity,
  val leadId: String = "0",
  val ucId: String = "0"
) : FragmentStateAdapter(activity) {

  override fun getItemCount(): Int = if (leadId == "0") 4 else 5

  override fun createFragment(position: Int): Fragment {

    return when (position) {
      0 -> LeadDetailFragment.newInstance(0, MOSLConstants.DETAILS, leadId, ucId)
      1 -> LeadViewActivitiesFragment.newInstance(1, MOSLConstants.ACTIVITIES, leadId, ucId)
      else -> {
        if (leadId == "0") {
          when (position) {
            2 -> MappingHistoryFragment.newInstance(2, MOSLConstants.MAPPING_HISTORY, leadId, ucId)
            else -> LeadViewInteractionFragment.newInstance(
                3, MOSLConstants.MAPPING_INTERACTION, leadId, ucId
            )
          }

        } else {
          when (position) {
            2 -> LeadProfilingFragment.newInstance(leadId)
            3 -> MappingHistoryFragment.newInstance(3, MOSLConstants.MAPPING_HISTORY, leadId, ucId)
            else -> LeadViewInteractionFragment.newInstance(
                4, MOSLConstants.MAPPING_INTERACTION, leadId, ucId
            )
          }

        }
      }
    }

  }
}