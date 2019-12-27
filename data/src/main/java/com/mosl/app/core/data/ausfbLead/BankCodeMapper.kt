package com.mosl.app.core.data.ausfbLead

import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.ausfbLead.BankCodeResponse

fun List<BankCodeResponse>.transform(): List<BankCodeModel> {
    return mapNotNull {
        BankCodeModel(

            bankId = it.bankId,
            bankName = it.bankName


        )
    }
}