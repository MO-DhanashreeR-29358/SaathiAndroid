package com.mosl.tabapp.birthdayanniversary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput

import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.LEADPROFILE
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentBirthdayAnniversaryNewBinding
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable

import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BirthdayAnniversaryFragmentNew : CommonFragment(), BirthdayAnniversaryView {
  lateinit var binding: FragmentBirthdayAnniversaryNewBinding
  @Inject lateinit var presenter: BirthdayAnniversaryPresenter
  private lateinit var section: DataManagerSection
  private lateinit var spinnerProfileAdapter: ArrayAdapter<ProfileSpinner>
  private lateinit var spinnerEventAdapter: ArrayAdapter<String>

  override fun getLayout(): Int {
    return R.layout.fragment_birthday_anniversary_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentBirthdayAnniversaryNewBinding.bind(view)
    setUpView()
    setUpRecyclerView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {

    binding.apply {
      val context = requireContext()
      spinnerProfileAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      spProfile.adapter = spinnerProfileAdapter

      spinnerEventAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      spEventType.adapter = spinnerEventAdapter
      val eventTypeArray =
        context.resources.getStringArray(R.array.BirthdayAniversary)
      val arrEventType = listOf(*eventTypeArray)
      spinnerEventAdapter.setSpinnerData(spEventType, arrEventType)

    }

  }

  private fun setUpRecyclerView() {

    val groupAdapter = GroupAdapter<ViewHolder>()
    section = DataManagerSection(onRetryClick)
    section.retrySubject = dataActionSubject
    groupAdapter.add(section)
    binding.rvBirthdayAnniversary.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      adapter = groupAdapter
    }
  }

  override fun onProfileSpinnerSelection(): Observable<Int> {
    return binding.spProfile.itemSelections()
  }

  override fun onEventTypeSpinnerSelection(): Observable<Int> {
    return binding.spEventType.itemSelections()
  }

  override fun getProfileValue(): ProfileSpinner {
    return binding.spProfile.selectedItem as ProfileSpinner
  }

  override fun getProfileRequestInput(): LookupRequestInput {
    val businessId = userManager.getUser()!!.currentBusinessProfile.businessUnitId
    return LookupRequestInput(
      businessUnitId = businessId!!,
      lookupTypeId = LEADPROFILE,
      parentId = null,
      IgnoreBusinessUnit = null
    )
  }

  override fun getEventType(): String {
    return binding.spEventType.selectedItem as String
  }

  override fun renderProfileSpinner(uiModel: UiModel<List<LookupItemResponse>>) {
    val profileSpinnerList = ArrayList<ProfileSpinner>()
    profileSpinnerList.add(ProfileSpinner(-1, "All"))
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {

      it.safeMap { data ->
        profileSpinnerList.add(ProfileSpinner(data.lookUpid, data.lookUpName!!))
      }

    }
    spinnerProfileAdapter.setSpinnerData(binding.spProfile, profileSpinnerList)
  }

}
