package com.mosl.app.core.data.autarget

import com.mosl.mobile.domain.autarget.*
import com.mosl.saathi.api.model.dashboard.autarget.APTargetRequest
import com.mosl.saathi.api.model.dashboard.autarget.AuTargetRequest
import com.mosl.saathi.api.model.dashboard.target.AuTable1Item
import com.mosl.saathi.api.model.dashboard.target.AuTableItem
import com.mosl.saathi.api.model.dashboard.target.AuTargetItem

fun AuTargetInput.requestTransform(): AuTargetRequest {
    return AuTargetRequest(
        businessProfileId = businessProfileId,
        addtionalParameters = addtionalParameters!!.apTransform(),
        employeeCode = employeeCode,
        productId = productId,
        widgetSystemName = widgetSystemName

    )
}

fun APTargetInput.apTransform(): APTargetRequest {
    return APTargetRequest(
        fltBankName, fltBankSubBrokerCode, fltBankBranchCode,
        fltHierarchy, fltPeriod
    )
}


fun AuTargetItem.responseTransform(): TargetItemModel {
    return TargetItemModel(

        table = table?.itemTransform(),
        table1 = table1?.itemTransform1()

    )
}


fun List<AuTableItem>.itemTransform(): List<TableItemModel> {
    return map {
        TableItemModel(
            ord = it.ord,
            displayText = it.displayText,
            value = it.value


        )
    }
}


fun List<AuTable1Item>.itemTransform1(): List<Table1ItemModel> {
    return map {
        Table1ItemModel(

            formatted = it.formatted,
            label = it.label,
            value = it.value

        )
    }
}
