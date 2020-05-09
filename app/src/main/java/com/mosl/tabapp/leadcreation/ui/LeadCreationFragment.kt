package com.mosl.tabapp.leadcreation.ui

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.DedupeModelResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.CRMMOSLCommon.disableField
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.OFFICEADDRESS
import com.mosl.tabapp.constants.MOSLConstants.RESIDENCEADDRESS
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadCreationBinding
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.ui.LeadCreationUiEvent.CreateLeadEvent
import com.mosl.tabapp.model.AddressDetailModel
import com.mosl.tabapp.model.EmployeeModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.gone
import com.mosl.tabapp.util.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.frag_lead_creation.etAdd1
import javax.inject.Inject

class LeadCreationFragment : CommonFragment(),
    LeadCreationView {

  @Inject internal lateinit var presenter: LeadCreationPresenter
  private lateinit var binding: FragLeadCreationBinding
  private val subject = PublishSubject.create<LeadCreationUiEvent>()
  private val categoryAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val professionAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val interestInAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val productTypeAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val sourceAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val prospectSourceAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val stageAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val statusAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val countryAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  override fun getLayout(): Int = R.layout.frag_lead_creation

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadCreationBinding.bind(view)
    setAppTitle(R.string.page_title_lead_creation)
    setupView()
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  private var primaryRM: String? = null
  private var RefereeCode: String? = null
  private var RefereeName: kotlin.String? = null

  private fun setupView() {
    setupScrollView()
    disableField(binding.llResAdd)
    disableField(binding.llOffAdd)
    binding.tvPersonalInfo.toggleIcon(binding.llPersonalInformation)
    binding.tvAdditionInfo.toggleIcon(binding.llAdditionInfo)
    binding.actAssignTo.apply {
      setHint(R.string.str_assing)
      setListener { primaryRM = null }
      setOnClickListener {
        requireActivity().window
            .setSoftInputMode(
                LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    or LayoutParams.SOFT_INPUT_ADJUST_PAN
            )
        val searchList = CustomListPickerDialog(requireContext())
        searchList.threshold = MOSLConstants.SEARCH_LENTH

        searchList.show()
        searchList.setCustomTitle(resources.getString(string.str_assing))
        val assingViewListner: ListPickerListneable = object : ListPickerListneable {
          override fun onListItemSelect(data: Parcelable) {
            val clientModel =
              data as EmployeeModel
            if (clientModel.isNoData) {
              return
            }
            binding.actAssignTo.setText(clientModel.attendeeName)
            primaryRM = clientModel.attendeeCode
            searchList.dismiss()
            binding.actAssignTo.setClearIconVisible(true)
          }

          override fun onSearchTextChanged(constraint: CharSequence) {
            // searchUserBusinessProfile(constraint, searchList)
          }
        }
        searchList.setmListner(assingViewListner)
      }
    }

    binding.etRefereeCode.setOnFocusChangeListener(
        OnFocusChangeListener { _: View?, _: Boolean ->
          if (binding.etRefereeCode.getText().toString().length < 4) {
            RefereeCode = ""
            RefereeName = ""
            binding.etRefereeName.setText("")
            binding.etRefereeEmail.setText("")
            binding.etRefereeMobile.setText("")
          }
        }
    )
    binding.etRefereeName.setOnFocusChangeListener(
        OnFocusChangeListener { view: View?, b: Boolean ->
          if (binding.etRefereeName.getText().toString().length < 3) {
            RefereeCode = ""
            RefereeName = ""
            binding.etRefereeEmail.setText("")
            binding.etRefereeMobile.setText("")
            binding.etRefereeCode.setText("")
          }
        }
    )

    binding.etPincode.addTextChangedListener(
        object : TextWatcher {
          override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
          ) {
          }

          override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
          ) {
            if (!CRMAppUtil.isBlank(binding.etPincode.getText().toString())) {
              if (binding.etPincode.length() < 6) {
                binding.etPincode.error = "Invalid pincode"
              } else {
                binding.etPincode.error = null
              }
            } else {
              binding.etPincode.error = null
            }
            if (binding.cbSameAs.isChecked) {
              binding.etOffPincode.setText(binding.etPincode.getText().toString())
            }
          }

          override fun afterTextChanged(s: Editable) {
            if (binding.cbSameAs.isChecked) {
              binding.etOffPincode.setText(binding.etPincode.getText().toString())
            }
          }
        }
    )

    binding.etOffPincode.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
        if (!CRMAppUtil.isBlank(binding.etOffPincode.getText().toString())) {
          if (binding.etOffPincode.length() < 6) {
            binding.etOffPincode.error = "Invalid pincode."
          } else {
            binding.etOffPincode.error = null
          }
        } else {
          binding.etOffPincode.error = null
        }
      }

      override fun afterTextChanged(s: Editable) {}
    })
    binding.etLandLine.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (!CRMAppUtil.isBlank(binding.etLandLine.getText().toString())) {
          if (!CRMValidationRule.validateTel(
                  binding.etLandLine.getText().toString().trim({ it <= ' ' })
              )
          ) {
            binding.etLandLine.error = "Invalid Tel number"
          } else {
            binding.etLandLine.error = null
          }
        } else {
          binding.etLandLine.error = null
        }
      }
    })
    binding.etName.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (!CRMAppUtil.isBlank(binding.etName.getText().toString().trim({ it <= ' ' }))) {
          if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                  binding.etName.getText().toString().trim({ it <= ' ' })
              )
          ) {
            binding.etName.error = MOSLConstants.ErrorMsgName
          } else {
            binding.etName.error = null
          }
        } else {
          binding.etName.error = null
        }
      }
    })
    binding.etProspectRefereeName.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (!CRMAppUtil.isBlank(
                binding.etProspectRefereeName.text.toString().trim({ it <= ' ' })
            )
        ) {
          if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                  binding.etProspectRefereeName.getText().toString().trim { it <= ' ' }
              )
          ) {
            binding.etProspectRefereeName.error = MOSLConstants.ErrorMsgName
          } else {
            binding.etProspectRefereeName.error = null
          }
        } else {
          binding.etProspectRefereeName.error = null
        }
      }
    })
    binding.etContactPerson.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (!CRMAppUtil.isBlank(binding.etContactPerson.getText().toString().trim({ it <= ' ' }))) {
          if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                  binding.etContactPerson.getText().toString().trim({ it <= ' ' })
              )
          ) {
            binding.etContactPerson.error = MOSLConstants.ErrorMsgName
          } else {
            binding.etContactPerson.error = null
          }
        } else {
          binding.etContactPerson.error = null
        }
      }
    })

    binding.etAdd1.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (CRMAppUtil.isBlank(binding.etAdd1.getText().toString())) {
          binding.etAdd1.error = "Enter address"
        }
        if (binding.cbSameAs.isChecked) {
          binding.etOffAdd1.setText(etAdd1.getText().toString())
        }
      }
    })
    binding.etAdd2.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (CRMAppUtil.isBlank(binding.etAdd2.getText().toString())) {
          binding.etAdd2.error = "Enter address"
        }
        if (binding.cbSameAs.isChecked) {
          binding.etOffAdd2.setText(binding.etAdd2.getText().toString())
        }
      }
    })
    binding.etAdd3.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
      }

      override fun afterTextChanged(s: Editable) {
        if (binding.cbSameAs.isChecked) {
          binding.etOffAdd3.setText(binding.etAdd1.getText().toString())
        }
      }
    })
    binding.etProspectRefereerMobile.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
        if (!CRMAppUtil.isBlank(binding.etProspectRefereerMobile.getText().toString())) {
          if (!CRMValidationRule.validateMobile(
                  binding.etProspectRefereerMobile.getText().toString()
              )
          ) {
            binding.etProspectRefereeName.error = MOSLConstants.INVALID_MOBILE_NO
          } else {
            binding.etProspectRefereeName.error = null
          }
        } else {
          binding.etProspectRefereeName.error = null
        }
      }

      override fun afterTextChanged(s: Editable) {}
    })

  }

  private fun setupScrollView() {
    binding.scrollView.apply {
      descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
      isFocusable = true
      isFocusableInTouchMode = true
      setOnTouchListener { v, _ ->
        v.requestFocusFromTouch()
        false
      }
    }
  }

  private fun TextView.toggleIcon(parent: View) {
    setCompoundDrawablesWithIntrinsicBounds(
        0, 0,
        if (parent.visibility == View.VISIBLE) R.drawable.ic_minus else R.drawable.ic_plus,
        0
    )
  }

  override fun onPrimaryCheckboxChanged(): Observable<Boolean> {
    return binding.cbPrimaryAddress.checkedChanges()
  }

  override fun renderOnPrimaryCheckboxChanges(isChecked: Boolean) {
    when {
      isChecked -> binding.handlingWhenPrimaryIsChecked()
      else -> binding.handlingWhenPrimaryIsNotChecked()
    }
  }

  private fun FragLeadCreationBinding.handlingWhenPrimaryIsChecked() {
    enableField()
    cbOffPrimaryAddress.gone()
    cbSameAs.visible()
  }

  private fun FragLeadCreationBinding.handlingWhenPrimaryIsNotChecked() {
    disableField()
    cbOffPrimaryAddress.visible()
    cbSameAs.gone()
    cbOffPrimaryAddress.isChecked = false
    unCheckedSameAsAbove()
  }

  private fun FragLeadCreationBinding.unCheckedSameAsAbove() {
    etOffAdd1.setText("")
    etOffAdd2.setText("")
    etOffAdd3.setText("")
    etOffPincode.setText("")
    spOffCountry.setSelection(0)
    spOffState.setSelection(0)
    spOffCity.setSelection(0)
  }

  private fun enableField() {
    enableField(binding.llResAdd)
    enableField(binding.llOffAdd)
  }

  private fun disableField() {
    disableField(binding.llResAdd)
    disableField(binding.llOffAdd)
  }

  override fun onOfficePrimaryCheckboxChanged(): Observable<Boolean> =
    binding.cbOffPrimaryAddress.checkedChanges()

  override fun onPrimaryInfoClicked(): Observable<Unit> = binding.tvPersonalInfo.clicks()
  override fun onAdditionalInfoClicked(): Observable<Unit> = binding.tvAdditionInfo.clicks()
  override fun onCreateButtonClicked(): Observable<Unit> = binding.btnCreate.clicks()
  override fun onCancelButtonClicked(): Observable<Unit> = binding.btnCancel.clicks()
  override fun onSameAsCheckboxClicked(): Observable<Unit> = binding.cbSameAs.clicks()
  override fun onCreateLeadEventFired(): Observable<CreateLeadEvent> = subject.ofType()

  override fun renderLeadCreationPageData(uiModel: UiModel<LeadCreationPageData>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->
      binding.spCategory.setSpinnerData(categoryAdapter, data.categoryRowDatas)
      binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
      binding.spInterestIn.setSpinnerData(interestInAdapter, data.interestInRowDatas)
      binding.spSource.setSpinnerData(sourceAdapter, data.sourceRowDatas)
      binding.spStage.setSpinnerData(stageAdapter, data.stageRowDatas)
      binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
      binding.spOffCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
    }
  }

  private fun Spinner.setSpinnerData(
    arrayAdapter: ArrayAdapter<RowData>,
    rowDatas: List<RowData>
  ) {
    adapter = arrayAdapter
    arrayAdapter.setSpinnerData(this, rowDatas)
  }

  override fun renderOnOfficePrimaryCheckboxChanges(isChecked: Boolean) {
    when {
      isChecked -> enableField()
      else -> disableField()
    }
  }

  override fun renderPrimaryInfoClick() {
    toggleSectionExpansion(
        binding.llPersonalInformation,
        binding.tvPersonalInfo
    )
  }

  override fun renderAdditionalInfoClick() {
    toggleSectionExpansion(
        binding.llAdditionInfo,
        binding.tvAdditionInfo
    )
  }

  override fun renderOnCreateButtonClick(uiModule: UiModel<String>) {
    toggleProgressBar(uiModule.inProgress)
    uiModule.onFailure {
      showOnFailurePopup(it)
      openSectionExpansion(binding.llPersonalInformation, binding.tvPersonalInfo)
    }

    uiModule.onSuccess {
      MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal), MOSLConstants.CONFIRM_LEAD, "YES",
          "NO", true
      ) { action: Action, data: Array<Any?>? ->
        if (action == OK) {
          subject.onNext(CreateLeadEvent)
        }
      }
    }

  }

  override fun renderCreateLead(uiModel: UiModel<SaathiCommonResponse<CreateLeadResponse>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { data ->
      val context = requireContext()
      val response = data.response
      if (data.status && response != null) {
        MSFDialog.alertDialog(
            context, 0,
            getString(string.motilal_oswal),
            "Lead Created Successfully with lead Id " + response.leadID,
            "OK", true
        ) { action: Action, _: Array<Any?>? ->
          if (action == OK) { //dashbordFragment();
            navigator.openHome()
          }
        }
      } else if (response != null) {
        val dedupeModel =
          response.dedupeModel
        if (dedupeModel != null && data.errors != null) {
          dedupeDialog(dedupeModel)
        } else if (data.errors != null) {
          val errorMsg = data.errors!![0]
              .message
          MSFDialog.alertDialog(
              context, 0,
              getString(string.motilal_oswal),
              errorMsg, "OK", true, null
          )
        }
      }
    }
  }

  private fun dedupeDialog(dedupeModel: DedupeModelResponse) {

  }

  override fun getLeadCreationUserInput(): LeadCreationUserInput {

    val resAdd = AddressDetailModel(
        RESIDENCEADDRESS,
        binding.etAdd1.text.toString(),
        binding.etAdd2.text.toString(),
        binding.etAdd3.text.toString(),
        (binding.spCountry.selectedItem as RowData).key,
        (binding.spState.selectedItem as RowData).key,
        (binding.spCity.selectedItem as RowData).key,
        binding.etPincode.text.toString(),
        binding.cbPrimaryAddress.isChecked(),
        userManager.getUser()?.currentBusinessProfile?.businessUnitId
    )

    val offAdd = AddressDetailModel(
        OFFICEADDRESS,
        binding.etOffAdd1.text.toString(),
        binding.etOffAdd2.text.toString(),
        binding.etOffAdd3.text.toString(),
        (binding.spOffCountry.selectedItem as RowData).key,
        (binding.spOffState.selectedItem as RowData).key,
        (binding.spOffCity.selectedItem as RowData).key,
        binding.etOffPincode.text.toString(),
        binding.cbOffPrimaryAddress.isChecked(),
        userManager.getUser()?.currentBusinessProfile?.businessUnitId
    )

    return LeadCreationUserInput(
        selectedCategory = binding.spCategory.selectedItem as RowData,
        etCategoryName = binding.etName.text.toString(),
        etContactPerson = binding.etContactPerson.text.toString(),
        selectedInterestIn = binding.spInterestIn.selectedItem as RowData,
        selectedProductType = binding.spProductType.selectedItem as RowData,
        emailId = binding.etEmailId.text.toString(),
        alternateEmailId = binding.etAlternateEmailId.text.toString(),
        mobileNo = binding.etMobile.text.toString(),
        alternateMobileNo = binding.etAlternateMobno.text.toString(),
        landlineNo = binding.etLandLine.text.toString(),
        ekycMobileNo = binding.etEkycMobile.text.toString(),
        panNo = binding.etPanNum.text.toString(),
        aadharNo = binding.etAadharNum.text.toString(),
        sourceName = binding.spProspectSource.selectedItem.toString(),
        referCode = binding.etRefereeCode.text.toString(),
        referEmail = binding.etRefereeEmail.text.toString(),
        referName = binding.etRefereeName.text.toString(),
        referMobile = binding.etRefereeMobile.text.toString(),
        pinCode = binding.etPincode.text.toString(),
        officePinCode = binding.etOffPincode.text.toString(),
        name = binding.etName.text.toString(),
        isSameAsChecked = binding.cbSameAs.isChecked,
        selectedSource = binding.spSource.selectedItem as RowData,
        selectedProspectSource = binding.spProspectSource.selectedItem as RowData,
        prospectSourceDetail = binding.etProspectSourceDetails.text.toString(),
        proReferName = binding.etProspectRefereeName.text.toString(),
        proReferEmail = binding.etProspectRefereerEmail.text.toString(),
        proReferMob = binding.etProspectRefereerMobile.text.toString(),
        comment = getJsonSafeString(binding.etComments.text.toString()),
        professionalId = binding.spProfession.selectedItem as RowData,
        primRM = null,// todo attendeeCode,
        stageId = binding.spStage.selectedItem as RowData,
        statusId = binding.spStatus.selectedItem as RowData,
        residenceAdd = resAdd,
        officeAdd = offAdd
    )
  }

  override fun isDataEmpty(): Boolean = true

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }

}