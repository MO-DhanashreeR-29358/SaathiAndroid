package com.mosl.mobile.user.model

import com.mosl.saathi.api.model.login.BusinessProfile
import com.mosl.saathi.api.model.login.CurrentBusinessProfile
import com.mosl.saathi.api.model.login.LoginResponseData
import com.mosl.saathi.api.model.login.UserProfile
import com.mosl.saathi.api.model.splash.ReportTypeResponse
import com.mosl.saathi.api.model.splash.VersionResponseData

fun LoginResponseData.transform(
): LoginResponse {
  val userProfile = userInfo
  return LoginResponse(
      user = userProfile.transformUser(smtEnabled, ekycRevamp),
      smtEnabled = smtEnabled,
      ekycRevamp = ekycRevamp
  )
}

fun VersionResponseData.transform(
): VersionResponse {

  return VersionResponse(
      statusCode = statusCode,
      msg = msg,
      url = url,
      newEkyc = newEkyc,
      newEkycUrl = newEkycUrl
  )
}
fun UserProfile.transformUser(
  smtEnabled: String,
  ekycRevamp: String

): User {
  val defaultBusinessProfile: BusinessProfile = defaultBusinessProfile
  val currentBusinessProfile: CurrentBusinessProfile = currentBusinessProfile

  return User(
      smtEnabled = smtEnabled,
      ekycRevamp = ekycRevamp,
      userId = userId,
      employeeCode = employeeCode,
      firstName = firstName,
      lastName = lastName ?: "",
      imageUrl = imageUrl ?: "",
      mobile = mobile ?: "0",
      email = email ?: "",
      token = token ?: "0",
      userTypeSequence = userTypeSequence ?: "",
      defaultBusinessProfile = defaultBusinessProfile.transformDefaultBusinessProfile(),
      currentBusinessProfile = currentBusinessProfile.transformCurrentBusinessProfile()

  )
}

fun BusinessProfile.transformDefaultBusinessProfile(): BusinessProfileRequest {
  return BusinessProfileRequest(
      branchCode = branchCode ?: "0",
      userId = userId ?: "0"
  )
}

fun CurrentBusinessProfile.transformCurrentBusinessProfile(): CurrentBusinessProfileRequest {
  return CurrentBusinessProfileRequest(
      businessProfileId = businessProfileId ?: "0",
      name = name ?: "",
      businessUnitId = businessUnitId ?: "0"
  )
}

fun List<ReportTypeResponse>.transformReportData(): List<ReportType> {
  return this.map {
    ReportType(name = it.name, systemName = it.systemName,
        childMenuList = it.childMenuList.map {
          ChildMenu(
              name = it.name, systemName = it.systemName, ChildMenuList = it.ChildMenuList
          )
        })
  }
}


