package com.mosl.app.core.data.clients

import com.mosl.mobile.domain.clients.*
import com.mosl.saathi.api.model.dashboard.client.*

fun ClientInput.requestTransform(): ClientsRequest {
    return ClientsRequest(
        businessProfileId = businessProfileId,
        addtionalParameters = addtionalParameters.additionalParametersTransform(),
        employeeCode = employeeCode,
        productId = productId,
        widgetSystemName = widgetSystemName

    )
}

fun APClientsInput.additionalParametersTransform(): AddtionalParameters {
    return AddtionalParameters(
        fltHierarchy, fltPeriod
    )
}

fun ClientItemResponse.responseTransform(): ClientItemResponseModel {

    var tableList = mutableListOf<TableItemModel>()
    var table1List = mutableListOf<Table1ItemModel>()

    table?.apply {
        if (this.size > 0)
            tableList = this.itemTransform()
    }

    table1?.apply {
        if (this.size > 0)
            table1List = this.item1Transform()
    }

    return ClientItemResponseModel(
        tableList, table1List

    )
}


fun List<TableItem>.itemTransform(): MutableList<TableItemModel> {
    return map {
        TableItemModel(
            dayCount = it.dAYCNT,
            familyStatus = it.fAMILYSTATUS,
            mtdCount = it.mTDCNT,
            ytdCount = it.yTDCNT


        )
    }.toMutableList()
}


fun List<Table1Item>.item1Transform(): MutableList<Table1ItemModel> {
    return map {
        Table1ItemModel(
            count = it.count

        )
    }.toMutableList()
}