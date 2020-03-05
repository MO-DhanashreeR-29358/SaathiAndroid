package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.data.leadviewdetail.profiling.LeadViewProfilingRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import com.mosl.tabapp.constants.MOSLConstants
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class LeadProfilingModule {

  @Binds
  abstract fun bindLeadProfilingView(fragment: LeadProfilingFragment): LeadProfilingView

  @Binds
  abstract fun bindLeadViewProfilingRepository(repository: LeadViewProfilingRepositoryImpl): LeadViewProfilingRepository

  @Module
  companion object {
    @Provides @JvmStatic @Named("leadId")
    fun getLeadId(fragment: LeadProfilingFragment): String {
      return fragment.arguments?.getString(MOSLConstants.LEAD_ID, "0") ?: "0"
    }
  }
}