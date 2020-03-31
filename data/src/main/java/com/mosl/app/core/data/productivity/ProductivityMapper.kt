package com.mosl.app.core.data.productivity

import com.mosl.mobile.domain.productivity.ProductivityAdditionalParametersInput
import com.mosl.mobile.domain.productivity.ProductivityInput
import com.mosl.saathi.api.model.dashboard.productivity.ProductivityAdditionalParameterRequest
import com.mosl.saathi.api.model.dashboard.productivity.ProductivityRequest

fun ProductivityInput.requestTransform(): ProductivityRequest {
  return ProductivityRequest(
    businessProfileId = businessProfileId,
    addtionalParameters = additionalParameters.additionalParametersTransform(),
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName
  )
}

private fun ProductivityAdditionalParametersInput.additionalParametersTransform(): ProductivityAdditionalParameterRequest {
  return ProductivityAdditionalParameterRequest(
    fltHierarchy = fltHierarchy
  )
}

