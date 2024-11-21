package com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data

import com.mosl.mobile.domain.leadviewdetail.main.ClientDetail
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.lead.IndirectLeadViewItem
import com.mosl.tabapp.util.emptyString

fun IndirectLeadViewItem.leadViewItemToLeadDetailMain(): IndirectLeadDetailMain {
    val emptyString = ""
    val personalDetail = IndirectPersonalDetail(
        name = contact?.name ?: emptyString,
        ucId = contact?.uCID ?: emptyString,
        mobile = contact?.mobile ?: emptyString,
        email = contact?.emailId ?: emptyString
    )
    val leadProducts = leadProductMapping?.map {
        IndirectLeadProduct(
            clientCode = it.clientCode ?: emptyString,
            stageName = it.stageName ?: emptyString,
            isDefault = it.isDefault,
            interestedIn = it.interestedIn.toString(),
            pendingDays = it.pendingDays.toString()
        )
    } ?: emptyList()

    return IndirectLeadDetailMain(
        id = leadID.toString(),
        branchCode = branchcode ?: emptyString,
        personalDetail = personalDetail,
        createMeetingId = createMeetingId?:0,
        leadOwner = leadOwner ?: emptyString,
        source = sourceName ?: emptyString,
        prospectSource = prospectSource ?: emptyString,
        prospectSourceDetail = leadSourceDetailsID.toString(),
        oaoUserId = oAOUserid,
        leadProducts = leadProducts,
        clientType = clientType?: "",
        leadName = leadName ?: emptyString()
    )
}

internal fun ClientDetailItem.toClientDetail(): ClientDetail {
    return ClientDetail(
        name = clientName ?: "",
        id = leadID.toString(),
        oaoUserId = oAOUserid,
        currentStage = stageName ?: "",
        currentStageSince = pendingDays ?: "",
        branchCode = branchCode ?: "",
        clientCode = clientCode ?: "",
        mobile = mobile ?: ""
    )
}