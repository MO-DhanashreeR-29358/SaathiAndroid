package com.mosl.app.core.data.leadviewdetail.main

import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.mobile.domain.leadviewdetail.main.LeadProduct
import com.mosl.mobile.domain.leadviewdetail.main.PersonalDetail
import com.mosl.saathi.api.model.lead.LeadViewItem

fun LeadViewItem.leadViewItemToLeadDetail(): LeadDetail {
  val emptyString = ""
  val personalDetail = PersonalDetail(
      name = contact?.name ?: emptyString,
      ucId = contact?.uCID ?: emptyString,
      mobile = contact?.mobile ?: emptyString,
      email = contact?.emailId ?: emptyString
  )
  val leadProducts = leadProductMapping?.map {
    LeadProduct(
        clientCode = it.clientCode ?: emptyString,
        stageName = it.stageName ?: emptyString,
        isDefault = it.isDefault,
        interestedIn = it.interestedIn.toString(),
        pendingDays = it.pendingDays.toString()
    )
  } ?: emptyList()

  return LeadDetail(
      id = leadID.toString(),
      branchCode = branchcode ?: emptyString,
      personalDetail = personalDetail,
      createMeetingId = createMeetingId.toString(),
      leadOwner = leadOwner ?: emptyString,
      source = sourceName ?: emptyString,
      prospectSource = prospectSource ?: emptyString,
      prospectSourceDetail = leadSourceDetailsID.toString(),
      oaoUserId = oAOUserid.toString(),
      diyCode = diyCode.toString(),
      diyLink = diyLink.toString(),
      leadProducts = leadProducts
  )
}