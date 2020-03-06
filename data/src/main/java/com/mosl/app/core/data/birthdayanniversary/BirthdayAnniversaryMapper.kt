package com.mosl.app.core.data.birthdayanniversary

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemData
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryData

fun List<BirthdayAnniversaryData>.responseBirthdayAnniversaryTransform(): List<BirthdayAnniversaryItemData> {
  return safeMap {
    BirthdayAnniversaryItemData(
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