package com.mosl.app.core.data.autarget

import com.mosl.mobile.domain.target.AditionalParametersTarget
import com.mosl.mobile.domain.target.Table1ItemModel
import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.saathi.api.model.dashboard.target.TableItem
import com.mosl.saathi.api.model.dashboard.target.TargetItemResponse
import com.mosl.saathi.api.model.dashboard.target.TargetTable1Item
import com.mosl.saathi.api.model.dashboard.targetnew.APTargetRequest
import com.mosl.saathi.api.model.dashboard.targetnew.TargetRequest

fun TargetInput.requestTransform(): TargetRequest {
    return TargetRequest(
        businessProfileId = businessProfileId,
        addtionalParameters = addtionalParameters!!.apTransform(),
        employeeCode = employeeCode,
        productId = productId,
        widgetSystemName = widgetSystemName

    )
}

fun AditionalParametersTarget.apTransform(): APTargetRequest {
    return APTargetRequest(
        fltBankName, fltBankSubBrokerCode, fltBankBranchCode,
        fltHierarchy, fltPeriod?.replace("null", "MTD") ?: "MTD"
    )
}


fun TargetItemResponse.responseTransform(): TargetItemModel {
    return TargetItemModel(

        table = table?.itemTransform(),
        table1 = table1?.itemTransform1()

    )
}


fun List<TableItem>.itemTransform(): List<TableItemModel> {
    return map {
        TableItemModel(
            ord = it.ord,
            displayText = it.displayText,
            value = it.value ?: "0"


        )
    }
}


fun List<TargetTable1Item>.itemTransform1(): List<Table1ItemModel> {
    return map {
        Table1ItemModel(

            formatted = it.formatted,
            label = it.label,
            value = it.value

        )
    }
}
