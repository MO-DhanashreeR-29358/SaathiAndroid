package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class ValidateOtpResponse(

  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,

  @JvmField @SerializedName("FullName") val fullName: String,

  @JvmField @SerializedName("LastLogin") val lastLogin: String,

  @JvmField @SerializedName("Permissions") val permissions: Any,

  @JvmField @SerializedName("Token") val token: String,

  @JvmField @SerializedName("RefreshToken") val refreshToken: String,

  @JvmField @SerializedName("Gender") val gender: Int,

  @JvmField @SerializedName("EmployeePhoto") val employeePhoto: String,

  @JvmField @SerializedName("EmployeeId") val employeeId: String,

  @JvmField @SerializedName("RoleId") val roleId: Int,

  @JvmField @SerializedName("ExpiresOn") val expiresOn: Int,

  @JvmField @SerializedName("DepartmentCode") val departmentCode: String
): HrmsErrorResponse()