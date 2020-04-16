package com.mosl.app.core.data.pendingapproval.report

import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalDropdownData
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalRepository
import com.mosl.mobile.domain.pendingapproval.ProfileModel
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.pendingforapproval.ApprovelRequest
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PendingForApprovalRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi,
  val userManager: UserManager
) : PendingForApprovalRepository {

  override fun getPendingApproval(): Observable<PendingForApprovalDropdownData> {
    return Observable.zip(
      getEmployeeList(),
      getProfileList(),
      BiFunction { listOfEmployee: List<EmployeeModel>, listOfProfile: List<ProfileModel> ->

        PendingForApprovalDropdownData(
          listOfEmployee = listOfEmployee,
          listOfProfile = listOfProfile
        )

      }
    )
  }

  private fun getEmployeeList(): Observable<List<EmployeeModel>> {
    val appointmentRequest = ApprovelRequest(
      userManager.getUser()?.employeeCode ?: ""
    )

    return saathiApi.getApprovalUserList(appointmentRequest)
      .map {
        it.mapToListOfEmployee()
      }

  }

  private fun getProfileList(): Observable<List<ProfileModel>> {
    val meetingRequest = ApprovelRequest(
      userManager.getUser()?.employeeCode ?: ""
    )

    return saathiApi.getApprovalFieldList(meetingRequest)
      .map {
        it.mapToListOfProfile()
      }
  }

}