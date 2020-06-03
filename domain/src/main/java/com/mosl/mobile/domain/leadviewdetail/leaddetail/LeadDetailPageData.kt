package com.mosl.mobile.domain.leadviewdetail.leaddetail

class LeadDetailPageData {

  data class LeadDetailArgument(
    @JvmField val position: Int,
    @JvmField val type: String,
    @JvmField val leadId: String,
    @JvmField val ucId: String
  )
}