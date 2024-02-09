package com.mosl.user.data.login

import com.mosl.saathi.api.model.attendance.TokenResponse
import com.mosl.saathi.api.model.attendance.ValidateOtpResponse
import com.mosl.user.domain.model.TokenInfo
import com.mosl.user.domain.model.User
import java.util.Date

object DataMapper {
  fun transformData(
      data: ValidateOtpResponse,
      mobileNumber: String
  ) =
    User(
        attenCount = "",
        employeeId = data.employeeId,
        employeeCode = data.employeeCode,
        employeeMoNo = mobileNumber,
        fullName = data.fullName,
        gender = data.gender,
        inTime = data.lastLogin,
        updateDate = Date(),
        photoPath = data.employeePhoto,
        token = data.token,
        departmentCode = data.departmentCode,
        uniqueId = data.roleId/*data.uniqueID.toString()*/
    )

  fun transformTokenDate(data: TokenResponse): TokenInfo {
    return TokenInfo(
        accessToken = data.token,
        refreshToken = data.refreshToken,
        expiresIn = data.expiresOn
    )
  }
}