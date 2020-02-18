package com.mosl.app.core.data.leadviewdetail.profiling

import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import com.mosl.saathi.api.SaathiApi
import javax.inject.Inject

class LeadViewProfilingRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadViewProfilingRepository {

}