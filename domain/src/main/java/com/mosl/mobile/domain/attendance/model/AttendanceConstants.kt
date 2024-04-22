package com.mosl.mobile.domain.attendance.model

/**
 * Created by Mohan Seelam on 20-07-2018.
 */
object AttendanceConstants {
  private const val SelectReason = "Select Reason"
  private const val EarlyDepartCompensation = "Early depart compensation"
  private const val HeavyRain = "Heavy Rain"
  private const val LateComingRegularization = "Late coming Regularization"
  private const val LateCompensation = "Late compensation"
  private const val OfficialDuty = "Official Duty"
  private const val PersonalReason = "Personal reason"
  private const val TechnicalIssue = "Technical Issue"
  private const val TrafficJam = "Traffic Jam"
  private const val TrainRunningLate = "Train running late"
  private const val OfficialTour = "Was on Duty / Official Tour"
  private const val Clientmeeting = "Client Meeting"
  private const val CompensatoryDayOff = "Compensatory Day Off"
  private const val CouldNotLogin = "Could not Login"
  private const val ForgotToLogin = "Had forgotten to Login"
  private const val PresentationAtClientSite = "Presentation at Client Site"
  private const val TechnicalMeeting = "Technical Meeting"
  private const val InTraining = "Was on Training"
  private const val WillBeInTraining = "Will be on Training"
  private const val WillBeInOfficialTour = "Will be on Duty / Official Tour"
  private const val NotFeelingWell = "Was not feeling well"
  private const val PersonalWork = "Personal Work"
  private const val GoingNativePlace = "Going Native Place"
  private const val Vacation = "Going for Vacation"
  private const val LeaveDeduction = "Leave deduction against Late Coming"
  private const val FullDay = "Full day"
  private const val PL = "PL"

  fun getLateInReason(): List<String> {
    return listOf(
        SelectReason, EarlyDepartCompensation, HeavyRain, LateComingRegularization,
        LateCompensation,
        OfficialDuty, PersonalReason, TechnicalIssue, TrafficJam, TrainRunningLate, OfficialTour
    )
  }

  fun getNotSignInReason(): List<String> {
    return listOf(
        SelectReason, Clientmeeting, CompensatoryDayOff, CouldNotLogin, ForgotToLogin,
        OfficialDuty, PresentationAtClientSite, TechnicalMeeting, OfficialTour, InTraining,
        WillBeInTraining, WillBeInOfficialTour
    )
  }

  fun getLeaveReasons(): List<String> {
    return listOf(
        SelectReason, NotFeelingWell, PersonalWork, GoingNativePlace, Vacation,
        LeaveDeduction
    )
  }
}