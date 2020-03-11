package com.mosl.tabapp.birthdayanniversary

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.FilterInput
import com.mosl.mobile.domain.birthdayanniversary.FiltersItemInput
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.birthdayanniversary.RequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.tabapp.R
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.GetDataList
import com.mosl.tabapp.birthdayanniversary.item.ItemBirthdayAnniversary
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.LEADPROFILE
import com.mosl.tabapp.constants.MOSLConstants.REPORT_PAGE_SIZE
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentBirthdayAnniversaryNewBinding
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BirthdayAnniversaryFragmentNew : CommonFragment(), BirthdayAnniversaryView,
  OnCheckedChangeListener {
  lateinit var binding: FragmentBirthdayAnniversaryNewBinding
  @Inject lateinit var presenter: BirthdayAnniversaryPresenter
  private lateinit var section: DataManagerSection
  private lateinit var spinnerProfileAdapter: ArrayAdapter<ProfileSpinner>
  private lateinit var spinnerEventAdapter: ArrayAdapter<String>
  private val subject = PublishSubject.create<BirthdayAnniversaryUiEvent>()
  private var filterType = "2"
  private var dayType = TODAY_FLAG
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
      layoutManager = GridLayoutManager(requireContext(), 3)
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

  override fun getDataList(): Observable<GetDataList> {
    return subject.ofType()
  }

  override fun renderGetData() {
    getData()
  }

  override fun renderBirthdayAnniversary(uiModel: UiModel<BirthdayAnniversaryItemResponse>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      Timber.d("Success")
      val itemList = it.data
      itemList.safeMap { data ->
        ItemBirthdayAnniversary(data, filterType, requireContext())
      }

    }
  }

  private fun getData() {
    var profileId: String
    val profileCodeStr = getProfileValue().id.toString()
    val profileNameStr = getProfileValue().lookUpName
    profileId = if (profileCodeStr.equals("-1", ignoreCase = true)) {
      ""
    } else {
      profileNameStr
    }

    val eventType = if (getEventType().equals("Birthday", ignoreCase = true)) {
      "B"
    } else {
      "A"
    }

    val filtersItem = FiltersItemInput()
    val valueList = java.util.ArrayList<String>()
    valueList.add(profileId)
    filtersItem.value = valueList
    val filters = ArrayList<FiltersItemInput>()

    filters.add(filtersItem)
    val filter = FilterInput(filters, "and")

    val request = RequestInput(
      filterType,
      "0",
      REPORT_PAGE_SIZE,
      eventType,
      "0",
      "1"
    )
    subject.onNext(
      GetDataList(
        BirthdayRequestInput(
          filter,
          request,
          true
        )
      )
    )

  }

  override fun onCheckedChanged(p0: RadioGroup?, checkedId: Int) {
    when (checkedId) {
      R.id.btnBelated -> {
        filterType = "1"
        dayType = "belated"
        getData()
        CRMAppUtil.collapse(binding.ivEmailAll)
      }
      R.id.btnToday -> {
        filterType = "2"
        dayType = "today"
        getData()
        CRMAppUtil.expand(binding.ivEmailAll)
      }
      R.id.btnUpcomming -> {
        filterType = "3"
        dayType = "upcoming"
        getData()
        CRMAppUtil.collapse(binding.ivEmailAll)
      }
      else -> {
        // Nothing to do
      }

    }
  }

  companion object {
    private const val TODAY_FLAG = "today"
  }

}
