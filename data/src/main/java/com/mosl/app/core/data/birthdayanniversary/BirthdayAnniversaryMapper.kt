package com.mosl.app.core.data.birthdayanniversary

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryListData
import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.FilterInput
import com.mosl.mobile.domain.birthdayanniversary.FiltersItemInput
import com.mosl.mobile.domain.birthdayanniversary.PermissionsData
import com.mosl.mobile.domain.birthdayanniversary.RequestInput
import com.mosl.mobile.domain.birthdayanniversary.SendEmailRequestInput
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryData
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryItem
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest
import com.mosl.saathi.api.model.birthdayanniversary.Filter
import com.mosl.saathi.api.model.birthdayanniversary.FiltersItem
import com.mosl.saathi.api.model.birthdayanniversary.Permissions
import com.mosl.saathi.api.model.birthdayanniversary.Request
import com.mosl.saathi.api.model.birthdayanniversary.SendEmailRequest

fun BirthdayAnniversaryItem.mapBirthdayResponseTransform(): BirthdayAnniversaryItemResponse {
  return BirthdayAnniversaryItemResponse(
    errors = errors,
    data = data.responseBirthdayAnniversaryTransform(),
    extraData = extraData,
    permissions = permissions.mapPermissionData(),
    total = total
  )
}

fun Permissions.mapPermissionData(): PermissionsData {
  return PermissionsData(
    read = read,
    businessProfileId = businessProfileId,
    export = export,
    systemName = systemName,
    write = write
  )
}

fun List<BirthdayAnniversaryData>.responseBirthdayAnniversaryTransform(): List<BirthdayAnniversaryListData> {
  return safeMap {
    BirthdayAnniversaryListData(
      clientName = it.clientName ?: "",
      clientCode = it.clientCode ?: "",
      ucid = it.ucid ?: "",
      birthdayCount = it.birthdayCount ?: "",
      email = it.email ?: "",
      backOfficeMobileNumber = it.backOfficeMobileNumber ?: "",
      anniversaryCount = it.anniversaryCount ?: "",
      clientPotential = it.clientPotential ?: "",
      anniversaryDate = it.anniversaryDate ?: "",
      anniversaryFlag = it.anniversaryFlag,
      birthdayFlag = it.birthdayFlag,
      clientType = it.clientType ?: "",
      dob = it.dob ?: "",
      noOfYearBirthday = it.noOfYearBirthday ?: ""

    )
  }
}

fun RequestInput.mapRequest(): Request {
  return Request(
    calculatedPage = calculatedPage,
    daytype = dayType,
    occasion = occasion,
    page = page,
    pageSize = pageSize,
    totalCount = totalCount
  )
}

fun BirthdayRequestInput.mapBirthdayRequest(): BirthdayAnniversaryRequest {
  return BirthdayAnniversaryRequest(
    filter = filter.mapFilter(),
    isAdminMode = isAdminMode,
    request = request.mapRequest()
  )
}

fun SendEmailRequestInput.mapSendEmailRequest(): SendEmailRequest {
  return SendEmailRequest(
    selectedUcids = selectedUcids,
    messageType = messageType,
    filterType = filterType,
    employeeCode = employeeCode,
    checkAll = checkAll,
    eventType = eventType
  )
}

fun FilterInput.mapFilter(): Filter {
  return Filter(
    filters = filters.mapFiltersItemInput(),
    logic = logic
  )
}

fun List<FiltersItemInput>.mapFiltersItemInput(): List<FiltersItem> {
  return safeMap {
    FiltersItem(
      operator = it.operator,
      field = it.field,
      value = it.value
    )
  }
}
