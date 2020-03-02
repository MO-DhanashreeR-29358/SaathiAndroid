package com.mosl.app.core.data.setappointment


import com.mosl.mobile.domain.setappointment.ClientDetailRequestInput
import com.mosl.mobile.domain.setappointment.LeadViewRequestInput
import com.mosl.mobile.domain.setappointment.LookupRequestInput
import com.mosl.mobile.domain.setappointment.MeetingByIdInput
import com.mosl.mobile.domain.setappointment.MeetingByIdItemData
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class SetAppointmentRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi
) : SetAppointmentRepository {
  override fun getClientImage(request: ClientDetailRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getLeadViewByid(request: LeadViewRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAllLookUp(request: LookupRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setAppointmentMeeting() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getMeetingById(request: MeetingByIdInput): Observable<MeetingByIdItemData> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getClientDetail(request: ClientDetailRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}