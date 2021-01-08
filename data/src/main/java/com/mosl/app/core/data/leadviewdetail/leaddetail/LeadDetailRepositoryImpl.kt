package com.mosl.app.core.data.leadviewdetail.leaddetail

import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailRepository
import com.mosl.saathi.api.SaathiApi
import javax.inject.Inject

class LeadDetailRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadDetailRepository {
}