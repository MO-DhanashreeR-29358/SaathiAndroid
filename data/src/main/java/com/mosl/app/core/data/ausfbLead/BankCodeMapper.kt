package com.mosl.app.core.data.ausfbLead

import com.mosl.saathi.api.model.ausfbLead.*

fun List<BankCodeResponse>.banckCodeTransform(): List<BankCodeModel> {
    return map {
        BankCodeModel(

            bankId = it.bankId,
            bankName = it.bankName


        )
    }
}

fun List<BranchCodeResponse>.branchCodeTransform(): List<BranchCodeModel> {
    return map {
        BranchCodeModel(

            bankId = it.bankId,
            branchId = it.branchId,
            branchName = it.branchName


        )
    }
}


fun List<SubBranchCodeResponse>.subBranchCodeTransform(): List<SubBranchCodeModel> {
    return map {
        SubBranchCodeModel(

            bankBranchId = it.bankBranchId,
            subBrokerName = it.subBrokerName


        )
    }
}