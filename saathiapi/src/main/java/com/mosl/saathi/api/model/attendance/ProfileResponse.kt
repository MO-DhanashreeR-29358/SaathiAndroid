package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
  @JvmField @SerializedName("AttendanceCount") val attendanceCount: Int,
  @JvmField @SerializedName("AttendanceType") val attendanceType: Int,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
  @JvmField @SerializedName("InTime") val inTime: String,
  @JvmField @SerializedName("FullName") val fullName: String,
  @JvmField @SerializedName("EmployeeId") val employeeId: Int,
  @JvmField @SerializedName("RedirectUrl") val redirectUrl: String,
  @JvmField @SerializedName("JoiningDate") val joiningDate: String,
  @JvmField @SerializedName("IsInitialDeclaration") val isInitialDeclaration: Boolean
)