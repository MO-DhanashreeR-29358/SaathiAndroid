package com.mosl.app.core.data.reactivation

import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.saathi.api.model.reactivation.ReactivationClientSearchResponse
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

fun List<ReactivationClientSearchResponse>?.transformReactivationClientSearchList(): List<ReactivationClientSearchResponseModel>?
{
    return  this?.map {
        ReactivationClientSearchResponseModel(
            creationDate = it.creationDate,

            lastModifiedBy = it.lastModifiedBy,

            finalActivationDate365 = it.finalActivationDate365,

            valuePackLast15Days = it.valuePackLast15Days,

            finalSegment = it.finalSegment,

            finalActivationDate =it.finalActivationDate,

            currentRMCode =it.currentRMCode,

            metInLast30Days =it.metInLast30Days,

            clientVerticalSameAsRMVertical =it.clientVerticalSameAsRMVertical,

            marginLast15Days =it.marginLast15Days,

            startDate =it.startDate,

            clientName =it.clientName,

            tGSEdumoRevenue =it.tGSEdumoRevenue,

            subSegment =it.subSegment,

            currentRMName =it.currentRMName,

            remarks =it.remarks,

            branch =it.branch,

            requestTypeFlag =it.requestTypeFlag,

            lastTradedDate180 =it.lastTradedDate180,

            lastTradedDate =it.lastTradedDate,

            totalMarginCollected =it.totalMarginCollected,

            actualActivationDate =it.actualActivationDate,

            lastMarginDate =it.lastMarginDate,

            rMVertical =it.rMVertical,

            createdBy =it.createdBy,

            mappedAdvisorCode =it.mappedAdvisorCode,

            zone =it.zone,

            unutilizedVP =it.unutilizedVP,

            stockMarginCollected =it.stockMarginCollected,

            requestorRMCode =it.requestorRMCode,

            marginProofFileName =it.marginProofFileName,

            businessUnitId =it.businessUnitId,

            reactivationType =it.reactivationType,

        minMarginVP =it.minMarginVP,

        mappedAdvisorVertical =it.mappedAdvisorVertical,

        cashMarginCollected =it.cashMarginCollected,

        ledgerBalance =it.ledgerBalance,

        requestTypeDesc =it.requestTypeDesc,

        clientCode =it.clientCode,

        lastModificationDate =it.lastModificationDate,

        region =it.region,

        id =it.id,

        mappedAdvisorName =it.mappedAdvisorName,

        dPBalance =it.dPBalance,

        errorMessage =it.errorMessage,

        branchShift =it.branchShift
        )
    }
}