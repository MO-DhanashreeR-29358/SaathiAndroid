package com.mosl.mobile.domain.attendance.model

data class SelfAttendance(
  @JvmField val present: Int,
  @JvmField val absent: Int,
  @JvmField val outDoor: Int,
  @JvmField val holiday: Int,
  @JvmField val leaveTaken: Int,
  @JvmField val lateIn: Int,
  @JvmField val pendingLeave: Int,
  @JvmField val balanceLeave: Int

)