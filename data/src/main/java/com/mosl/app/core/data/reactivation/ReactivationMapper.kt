package com.mosl.app.core.data.reactivation

import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.saathi.api.model.reactivation.ReactivationSourceResponse
import com.mosl.saathi.api.model.reactivation.ReactivationSummaryData

fun ReactivationSummaryData.summaryItemResponseTransform(): ReactivationSummaryItem {
    return ReactivationSummaryItem(
            assigned = assigned,
            contacted = contacted,
            nonContacted = nonContacted,
            reactivatedClients = reactivatedClients,
            traded = traded,
            revenue = revenue
        )
    }


fun List<ReactivationSourceResponse>.transformSourceList(): List<String>{
    return this.map{
        it.source
    }
}