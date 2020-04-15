package com.mosl.app.core.data.pendingapproval

import com.mosl.mobile.domain.pendingapproval.ApprovalSummaryByEmployeeItemModel
import com.mosl.mobile.domain.pendingapproval.ApprovalSummaryCountItemModel
import com.mosl.mobile.domain.pendingapproval.PendingApprovalData
import com.mosl.mobile.domain.pendingapproval.PendingApprovalRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.pendingapproval.PendingApprovalRequest
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class pendingApprovalRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi,
  val userManager: UserManager
) : PendingApprovalRepository {

  override fun getPendingApproval(): Observable<PendingApprovalData> {
    return Observable.zip(
      getCount(),
      getApprovalList(),
      BiFunction { summaryCount: ApprovalSummaryCountItemModel, listOfMeeting: List<ApprovalSummaryByEmployeeItemModel> ->

        PendingApprovalData(
          summaryCount = summaryCount,
          listOfApprovals = listOfMeeting
        )

      }
    )
  }

  private fun getCount(): Observable<ApprovalSummaryCountItemModel> {
    val appointmentRequest = PendingApprovalRequest(
      userManager.getUser()?.employeeCode ?: ""
    )

    return saathiApi.postApprovalSummaryCount(appointmentRequest)
      .map {
        it.mapToSummaryCount()
      }

  }

  private fun getApprovalList(): Observable<List<ApprovalSummaryByEmployeeItemModel>> {
    val meetingRequest = PendingApprovalRequest(
      userManager.getUser()?.employeeCode ?: ""
    )

    return saathiApi.postApprovalSummaryByEmployee(meetingRequest)
      .map {
        it.mapToListOfPendingApproval()
      }
  }

}