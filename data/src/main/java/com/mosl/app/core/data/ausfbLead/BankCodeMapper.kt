package com.mosl.app.core.data.ausfbLead

import com.mosl.app.core.data.util.defaultSelection
import com.mosl.mobile.domain.ausfbLead.SelectBank
import com.mosl.mobile.domain.ausfbLead.SelectBranch
import com.mosl.mobile.domain.ausfbLead.SelectSubBranch
import com.mosl.saathi.api.model.ausfbLead.*

fun List<BankCodeResponse>.banckCodeTransform(): List<BankCodeModel> {
    return map {
        BankCodeModel(

            bankId = it.bankId,
            bankName = it.bankName


        )
    }
        .orEmpty()
        .defaultSelection(SelectBank)
}

fun List<BranchCodeResponse>.branchCodeTransform(): List<BranchCodeModel> {
    return map {
        BranchCodeModel(

            bankId = it.bankId,
            branchId = it.branchId,
            branchName = it.branchName


        )
    }.orEmpty()
        .defaultSelection(SelectBranch)
}


fun List<SubBranchCodeResponse>.subBranchCodeTransform(): List<SubBranchCodeModel> {
    return map {
        SubBranchCodeModel(

            bankBranchId = it.bankBranchId,
            subBrokerName = it.subBrokerName


        )
    }.orEmpty()
        .defaultSelection(SelectSubBranch)
}