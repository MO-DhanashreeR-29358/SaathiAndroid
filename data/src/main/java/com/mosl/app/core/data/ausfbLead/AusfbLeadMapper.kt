package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.AdditionalParametersInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadInput
import com.mosl.saathi.api.model.dashboard.ausfbLead.*

fun AusfbLeadInput.requestTransform(): AusfbLeadRequest {
    return AusfbLeadRequest(
        businessProfileId = businessProfileId,
        addtionalParameters = addtionalParameters!!.additionalParametersTransform(),
        employeeCode = employeeCode,
        productId = productId,
        widgetSystemName = widgetSystemName

    )
}

fun AdditionalParametersInput.additionalParametersTransform(): AdditionalParametersRequest {
    return AdditionalParametersRequest(
//        fltBankName, fltBankSubBrokerCode, fltBankBranchCode,
        fltHierarchy, fltPeriod, fltProduct
    )
}


fun AusfbLeadResponse.responseTransform(): AusfbLeadModel {
    return AusfbLeadModel(

        table = table?.itemTransform(),
        table1 = table1?.itemTransform(),
        table2 = table2?.groupItemTransform()

    )
}

fun List<AusfbLeadItem>.itemTransform(): List<AusfbLeadItemModel> {
    return map {
        AusfbLeadItemModel(
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

fun List<AusfbLeadGroupItem>.groupItemTransform(): List<AusfbLeadGroupItemModel> {
    return map {
        AusfbLeadGroupItemModel(
            groupId = it.groupId,
            childId = it.childId,
            rowFilter = it.rowFilter
        )
    }
}