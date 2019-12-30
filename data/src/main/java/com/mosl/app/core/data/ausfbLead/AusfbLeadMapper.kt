package com.mosl.app.core.data.ausfbLead

import com.mosl.mobile.domain.ausfbLead.AdditionalParametersInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadInput
import com.mosl.saathi.api.model.ausfbLead.AdditionalParametersRequest
import com.mosl.saathi.api.model.ausfbLead.AusfbLeadRequest

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
        fltBankName, fltBankSubBrokerCode, fltBankBranchCode, fltHierarchy, fltPeriod, fltProduct
    )
}