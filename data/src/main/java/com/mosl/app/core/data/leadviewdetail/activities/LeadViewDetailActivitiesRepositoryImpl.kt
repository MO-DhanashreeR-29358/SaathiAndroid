package com.mosl.app.core.data.leadviewdetail.activities

import android.annotation.SuppressLint
import com.mosl.mobile.domain.leadviewdetail.activities.*
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.leadviewactivities.DeleteAppointmentRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class LeadViewDetailActivitiesRepositoryImpl @Inject constructor(
    private val saathiApi: SaathiApi,
    val saathiIndirectApi: SaathiIndirectApi,
    val userManager: UserManager

) : LeadViewDetailActivitiesRepository {

  override fun getActivityData(request: ActivityRequest): Observable<ActivityData> {
    return Observable.zip(
        getAppointment(request),
        getMeeting(request),
        BiFunction { listOfAppointment: List<Appointment>, listOfMeeting: List<Meeting> ->

          ActivityData(
              listOfAppointment = listOfAppointment,
              listOfMeeting = listOfMeeting
          )

        }
    )
  }

  @SuppressLint("SuspiciousIndentation")
  private fun getAppointment(request: ActivityRequest): Observable<List<Appointment>> {
    val appointmentRequest = request.mapToAppointmentRequest()
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.getAppointments(appointmentRequest)
              .map {
                  it.data?.mapToListOfAppointment() ?: mutableListOf()
              }
      }else{
          return saathiApi.getAppointments(appointmentRequest)
              .map {
                  it.mapToListOfAppointment()
              }
      }
  }

  @SuppressLint("SuspiciousIndentation")
  private fun getMeeting(request: ActivityRequest): Observable<List<Meeting>> {
    val meetingRequest = request.mapToMeetingRequest()
      if(userManager.getUser()?.isIndirectUser() == true){
          return saathiIndirectApi.getMeetings(meetingRequest)
              .map {
                  it.data?.mapToListOfMeeting() ?: mutableListOf()
              }
      }else{
          return saathiApi.getMeetings(meetingRequest)
              .map {
                  it.mapToListOfMeeting()
              }
      }

  }

  override fun deleteAppointment(
    meetingId: String,
    url: String
  ): Observable<String> {
    val request = DeleteAppointmentRequest(meetingId)
    return saathiApi.deleteAppointments(url, request)

  }
}