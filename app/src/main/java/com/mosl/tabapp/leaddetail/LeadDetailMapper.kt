package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.tabapp.util.emptyString

fun LeadViewItem.leadViewItemToLeadDetail(): LeadDetail {
  return LeadDetail(
      leadOwner = leadOwner ?: emptyString(),
      contactPerson = contactPerson ?: emptyString(),
      sourceId = sourceID.toString()
  )
}