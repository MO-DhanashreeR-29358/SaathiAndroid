package com.mosl.tabapp.leadViewDetail.updatemeeting

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragUpdateMeetingBinding
import javax.inject.Inject

class UpdateMeetingFragment : CommonFragment(), UpdateMeetingView {

  @Inject
  internal lateinit var presenter: UpdateMeetingPresenter

  lateinit var binding: FragUpdateMeetingBinding

  override fun getLayout(): Int {
    return R.layout.frag_update_meeting
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    setAppTitle("Update Meeting")
    binding = FragUpdateMeetingBinding.bind(view)
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  private var meetingDate: String? = null
  private var activityTypeId: String? = null

  private var meetingReasonID: String? = null
  private var actionableID: String? = null
  private var categoryID: String? = null
  private var subCategoryID: String? = null

  override fun renderMeetingById(uiModel: UiModel<Meeting>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { data ->
      meetingDate = stringOrEmpty(data.meetingDate)
      activityTypeId = stringOrEmpty(data.activityTypeID)
      val productTypeID = stringOrEmpty(data.productTypeID)

      meetingReasonID = stringOrEmpty(data.meetingReasonID)
      val meetingDescription = stringOrEmpty(data.meetingDescription)
      val customerRemark = stringOrEmpty(data.customerRemark)
      actionableID = stringOrEmpty(data.actionableID)
      categoryID = stringOrEmpty(data.categoryID)
      val categoryDescription = stringOrEmpty(data.categoryDescription)
      subCategoryID = stringOrEmpty(data.subCategoryID)
      val subCategoryDescription = stringOrEmpty(data.subCategoryDescription)
      val meetingAttendee = data.meetingAttendeeList

    }
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}