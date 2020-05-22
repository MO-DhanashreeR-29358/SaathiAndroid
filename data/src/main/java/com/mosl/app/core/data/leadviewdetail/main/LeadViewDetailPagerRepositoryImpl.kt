package com.mosl.app.core.data.leadviewdetail.main

import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.saathi.api.SaathiApi
import javax.inject.Inject

class LeadViewDetailPagerRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadViewDetailPagerRepository {
}