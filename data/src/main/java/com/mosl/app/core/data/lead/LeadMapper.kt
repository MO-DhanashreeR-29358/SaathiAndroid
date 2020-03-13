package com.mosl.app.core.data.lead

import com.mosl.mobile.domain.lead.AdditionalParametersInput
import com.mosl.mobile.domain.lead.LeadGroupItemModel
import com.mosl.mobile.domain.lead.LeadInput
import com.mosl.saathi.api.model.dashboard.lead.*

fun LeadInput.requestTransform(): LeadRequest {
    return LeadRequest(
        businessProfileId = businessProfileId,
        addtionalParameters = addtionalParameters!!.additionalParametersTransform(),
        employeeCode = employeeCode,
        productId = productId,
        widgetSystemName = widgetSystemName

    )
}

fun AdditionalParametersInput.additionalParametersTransform(): AdditionalParametersRequest {
    return AdditionalParametersRequest(
        fltBankName, fltBankSubBrokerCode, fltBankBranchCode,
        fltHierarchy, fltPeriod, fltProduct, fltProspect
    )
}


fun LeadResponse.responseTransform(): LeadModel {
    return LeadModel(

        table = table?.itemTransform(),
        table1 = table1?.itemTransform(),
        table2 = table2?.groupItemTransform()

    )
}

fun List<LeadItem>.itemTransform(): List<LeadItemModel> {
    return map {
        LeadItemModel(
            id = it.id,
            stage = it.stage,
            C0 = it.C0,
            C1 = it.C1,
            C2 = it.C2,
            C3 = it.C3,
            C4 = it.C4,
            C5 = it.C5,
            C6 = it.C6,
            C7 = it.C7,
            total = it.total
        )
    }
}

fun List<LeadGroupItem>.groupItemTransform(): List<LeadGroupItemModel> {
    return map {
        LeadGroupItemModel(
            groupId = it.groupId,
            childId = it.childId,
            rowFilter = it.rowFilter
        )
    }
}