package com.mosl.tabapp.saathiIndirect.LeadCreation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.view.isVisible
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.core.prefs.StringPreference
import com.mosl.app.coreui.spinner.getItems
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.app.coreui.spinner.setSpinnerDataWithSelection
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.user.annotation.BranchCode
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.DedupeModelResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogDedupeBinding
import com.mosl.tabapp.databinding.FragmentIndirectLeadCreationBinding
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadCreationPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.domain.TransferLead
import com.mosl.tabapp.leadcreation.ui.LeadCreationView
import com.mosl.tabapp.saathiIndirect.LeadCreation.IndirectLeadCreationUiEvent.*
import com.mosl.tabapp.model.AddressDetailModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.*
import com.mosl.validator.data.ValidationError
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class IndirectLeadCreationFragment : CommonFragment(),
    IndirectLeadCreationView {
    @Inject
    internal lateinit var presenter: IndirectLeadCreationPresenter
    private lateinit var binding: FragmentIndirectLeadCreationBinding
    private val subject = PublishSubject.create<IndirectLeadCreationUiEvent>()
    private var isReferee = false
    private val categoryAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val interestInAdapter: ArrayAdapter<RowData> by lazy {
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

    private val countryAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stateAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stateAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val cityAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val cityAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val backgroundAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }
    private val workExpAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }


    private val selectedList = ArrayList<RowData>()

    @Inject
    @field:BranchCode
    lateinit var branchCodePreference: StringPreference

    override fun getLayout(): Int = R.layout.fragment_indirect_lead_creation

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIndirectLeadCreationBinding.bind(view)
        createDefaultSelectList()
        setAppTitle(R.string.page_title_lead_creation)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }
    override fun onFragmentResume() {
        super.onFragmentResume()
        setAppTitle(R.string.page_title_lead_creation)
    }
    private fun createDefaultSelectList() {
        selectedList.add(LeadCreationRepositoryImpl.SELECT_ROW_DATA)
    }

    private var primaryRM: String? = null
    private var secondaryRM: String? = ""
    private val searchList: CustomListPickerDialog by lazy {
        CustomListPickerDialog(requireContext())
    }
    private val secondaryRMSearchList: CustomListPickerDialog by lazy {
        CustomListPickerDialog(requireContext())
    }

    private fun setupView() {
        setupScrollView()

        binding.spCity.setDialogTitle("Search City")
        binding.spOffCity.setDialogTitle("Search City")
        disableField(binding.llResAdd)
        disableField(binding.llOffAdd)
        binding.tvPersonalInfo.toggleIcon(binding.llPersonalInformation)
        binding.tvAdditionInfo.toggleIcon(binding.llAdditionInfo)


        binding.etRefereeCode.setOnFocusChangeListener { _: View?, _: Boolean ->
            if (binding.etRefereeCode.text
                    .toString().length < 4
            ) {
                binding.etRefereeName.setText("")
                binding.etRefereeEmail.setText("")
                binding.etRefereeMobile.setText("")
            }
        }
        binding.etRefereeName.setOnFocusChangeListener { _: View?, _: Boolean ->
            if (binding.etRefereeName.text
                    .toString().length < 3
            ) {
                binding.etRefereeEmail.setText("")
                binding.etRefereeMobile.setText("")
                binding.etRefereeCode.setText("")
            }
        }

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
                if (!CRMAppUtil.isBlank(
                        binding.etLandLine.text
                            .toString()
                    )
                ) {
                    if (!CRMValidationRule.validateTel(
                            binding.etLandLine.text
                                .toString()
                                .trim { it <= ' ' }
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
                if (!CRMAppUtil.isBlank(
                        binding.etName.text
                            .toString()
                            .trim { it <= ' ' }
                    )
                ) {
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            binding.etName.text
                                .toString()
                                .trim { it <= ' ' }
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

        //Expected margin feild should not display for Mo internationl user
      if(userManager.getUser()!!.isMoInternationUser()){
          binding.ilExpectedMargin.isVisible = false
      }



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

    private fun FragmentIndirectLeadCreationBinding.handlingWhenPrimaryIsChecked() {
        enableField()
        cbOffPrimaryAddress.gone()
        cbSameAs.visible()
    }

    private fun FragmentIndirectLeadCreationBinding.handlingWhenPrimaryIsNotChecked() {
        disableField()
        cbOffPrimaryAddress.visible()
        cbSameAs.gone()
        cbSameAs.isChecked = false
        cbOffPrimaryAddress.isChecked = false
        unCheckedSameAsAbove()
    }

    private fun FragmentIndirectLeadCreationBinding.unCheckedSameAsAbove() {
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

    override fun onRefereeCodeChanged(): Observable<String> {
        return binding.etRefereeCode.afterTextChangeEvents()
            .map { it.editable?.toString() ?: emptyString() }
    }

    override fun getRefereeCode(): String {
        return binding.etRefereeCode.text?.toString() ?: emptyString()
    }

    override fun resetRefereeOnCodeChange() {
        binding.apply {
            etRefereeName.setText(emptyString())
            etRefereeEmail.setText(emptyString())
            etRefereeMobile.setText(emptyString())
        }
    }

    override fun onRefereeNameChanged(): Observable<String> {
        return binding.etRefereeName.afterTextChangeEvents()
            .map { it.editable?.toString() ?: "" }
    }

    override fun resetRefereeOnNameChange() {
        binding.apply {
            etRefereeCode.setText(emptyString())
            etRefereeEmail.setText(emptyString())
            etRefereeMobile.setText(emptyString())
        }
    }

    override fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>) {
        binding.pbLoadingIndicatorRefreeCode toggle uiModel.inProgress
        renderOnRefereeChange(uiModel)
    }

    override fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>) {
        binding.pbLoadingIndicatorRefreeName toggle uiModel.inProgress
        renderOnRefereeChange(uiModel)
    }

    private fun renderOnRefereeChange(uiModel: UiModel<SearchReferee>) {
        uiModel.onFailure {
            isReferee = false
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeMobile.setText(emptyString())
        }
        uiModel.onSuccess {
            when (it.type) {
                "ByCode" -> binding.etRefereeName.setText(it.name)
                else -> binding.etRefereeCode.setText(it.code)
            }
            binding.etRefereeEmail.setText(it.email)
            binding.etRefereeMobile.setText(it.mobile)
            isReferee = true
        }
    }

    private infix fun ProgressBar.toggle(inProgress: Boolean) {
        if (inProgress) visible() else gone()
    }

    override fun onAddressLineOneChanged(): Observable<String> {
        return binding.etAdd1.afterTextChangeEvents()
            .map {
                it.editable?.toString() ?: ""
            }
    }

    override fun renderOnAddressLineOneChange(text: String) {
        binding.etOffAdd1.setText(text)
    }

    override fun onAddressLineTwoChanged(): Observable<String> {
        return binding.etAdd2.afterTextChangeEvents()
            .map {
                it.editable?.toString() ?: ""
            }
    }

    override fun renderOnAddressLineTwoChange(text: String) {
        binding.etOffAdd2.setText(text)
    }

    override fun onAddressLineThreeChanged(): Observable<String> {
        return binding.etAdd3.afterTextChangeEvents()
            .map {
                it.editable?.toString() ?: ""
            }
    }

    override fun renderOnAddressLineThreeChange(text: String) {
        binding.etOffAdd3.setText(text)
    }

    override fun onPincodeChanged(): Observable<String> {
        return binding.etPincode.afterTextChangeEvents()
            .map {
                it.editable?.toString().toString()
            }
    }

    override fun renderOnPincodeChange(pincode: String) {
        binding.etOffPincode.setText(pincode)
    }

    override fun onCategoryChanged(): Observable<Int> = binding.spCategory.itemSelections()
    override fun onAssignToSearchEventFired(): Observable<AssignToSearchEvent> = subject.ofType()
    override fun onSecondaryRMSearchEventFired(): Observable<SecondaryRMSearchEvent> {
        return  subject.ofType()
    }
    override fun onPCGSecondaryRMSearchEventFired(): Observable<PCGSecondaryRMSearchEvent> {
        return  subject.ofType()
    }
    override fun onPrimaryInfoClicked(): Observable<Unit> = binding.tvPersonalInfo.clicks()
    override fun onAdditionalInfoClicked(): Observable<Unit> = binding.tvAdditionInfo.clicks()
    override fun onCreateButtonClicked(): Observable<Unit> = binding.btnCreate.clicks()
    override fun onCancelButtonClicked(): Observable<Unit> = binding.btnCancel.clicks()
    override fun onSameAsCheckboxClicked(): Observable<Unit> = binding.cbSameAs.clicks()
    override fun isSameAsAboveChecked(): Boolean = binding.cbSameAs.isChecked
    override fun onCreateLeadEventFired(): Observable<CreateLeadEvent> = subject.ofType()
    override fun onCountrySelected(): Observable<Int> = binding.spCountry.itemSelections()
    override fun onOfficeCountrySelected(): Observable<Int> = binding.spOffCountry.itemSelections()
    override fun onInterestedProductSelected(): Observable<Int> =
        binding.spInterestIn.itemSelections()

    override fun onSourceSelected(): Observable<Int> = binding.spSource.itemSelections()
    override fun onStageSelected(): Observable<Int> = binding.spStage.itemSelections()
    override fun onStateSelected(): Observable<Int> = binding.spState.itemSelections()
        .map {
            binding.spCity.setSpinnerData(cityAdapter, selectedList)
            it
        }

    override fun onOfficeStateSelected(): Observable<Int> =
        binding.spOffState.itemSelections()

    override fun onCitySelected(): Observable<Int> = binding.spCity.itemSelections()

    override fun renderOnCitySelectAndSameAsAboveChecked(position: Int) {
        binding.spOffCity.setSelection(position)
    }

    override fun getSelectedInterestedProduct(): RowData =
        binding.spInterestIn.selectedItem as RowData

    override fun getSelectedCategory(): RowData = binding.spCategory.selectedItem as RowData
    override fun getSelectedSource(): RowData = binding.spSource.selectedItem as RowData
    override fun getSelectedStage(): RowData = binding.spStage.selectedItem as RowData
    override fun getSelectedCountry(): RowData = binding.spCountry.selectedItem as RowData
    override fun getSelectedOfficeCountry(): RowData = binding.spOffCountry.selectedItem as RowData
    override fun getSelectedState(): RowData = binding.spState.selectedItem as RowData
    override fun getSelectedOfficeState(): RowData = binding.spOffState.selectedItem as RowData

    override fun renderOnCategoryChange(category: String) {

    }

    override fun renderLeadCreationPageData(uiModel: UiModel<IndirectLeadCreationPageData>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            binding.spCategory.setSpinnerData(categoryAdapter, data.categoryRowDatas)
            binding.spInterestIn.setSpinnerData(interestInAdapter, data.interestInRowDatas)
            binding.spSource.setSpinnerData(sourceAdapter, data.sourceRowDatas)
            binding.spStage.setSpinnerData(stageAdapter, data.stageRowDatas)
            binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
            binding.spOffCountry.setSpinnerData(countryAdapterOff, data.countryRowDatas)
            binding.spBackground.setSpinnerData(backgroundAdapter, data.backgroundRowDatas)
            binding.spWorkExp.setSpinnerData(workExpAdapter, data.workExpRowDatas)

            binding.spCategory.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                    when (getSelectedCategory().value) {

                    }
                }
               override fun onNothingSelected(parent: AdapterView<*>?) {}
            })


        }
    }


    override fun renderProspectSourceOnSourceSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { binding.spProspectSource.setSpinnerData(prospectSourceAdapter, it) }
    }

    override fun renderStatusOnStageSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { binding.spStatus.setSpinnerData(statusAdapter, it) }
    }



    override fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spState.setSpinnerData(stateAdapter, it)
            binding.spOffState.setSpinnerData(stateAdapterOff, it)
            binding.spCity.setSpinnerData(cityAdapter, selectedList)
            if (binding.cbSameAs.isChecked) {
                binding.spOffCountry.setSelection(binding.spCountry.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            Timber.i("RenderStateOffice=>>Called")
            binding.spOffState.setSpinnerData(stateAdapterOff, it)
            binding.spOffCity.setSpinnerData(cityAdapterOff, selectedList)
            if (binding.cbSameAs.isChecked) {
                binding.spOffCity.setSpinnerData(cityAdapterOff, cityAdapter.getItems())
                binding.spOffCity.setSelection(binding.spCity.selectedItemPosition)
            }
        }
    }

    override fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spCity.setSpinnerData(cityAdapter, it)
            binding.spOffCity.setSpinnerData(cityAdapterOff, it)
            if (binding.cbSameAs.isChecked) {
                binding.spOffState.setSelection(binding.spState.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeCityOnOfficeStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { toggleProgressBar(uiModel.inProgress) }
        uiModel.onSuccess {
            binding.spOffCity.setSpinnerData(cityAdapterOff, it)
        }
    }

    private fun Spinner.setSpinnerData(
        arrayAdapter: ArrayAdapter<RowData>,
        rowData: List<RowData>
    ) {
        adapter = arrayAdapter
        arrayAdapter.setSpinnerData(this, rowData)
    }

    private fun Spinner.setSpinnerDataWithSelection(
        arrayAdapter: ArrayAdapter<RowData>,
        rowData: List<RowData>,
        data: RowData
    ) {
        adapter = arrayAdapter
        arrayAdapter.setSpinnerDataWithSelection(this, rowData, data)
    }

    override fun renderOnOfficePrimaryCheckboxChanges(isChecked: Boolean) {
        when {
            isChecked -> enableField()
            else -> disableField()
        }
    }

    override fun renderOnSameAsAboveCheckboxClick() {
        when {
            binding.cbSameAs.isChecked -> binding.handlingWhenSameAsCheckboxClickedAndChecked()
            else -> binding.handlingWhenSameAsCheckboxClickedAndUnChecked()
        }
    }

    private fun FragmentIndirectLeadCreationBinding.handlingWhenSameAsCheckboxClickedAndChecked() {
        disableField(llOffAdd)
        etOffAdd1.text = etAdd1.text
        etOffAdd2.text = etAdd2.text
        etOffAdd3.text = etAdd3.text
        etOffPincode.text = etPincode.text
        spOffCountry.setSelection(spCountry.selectedItemPosition)
        spOffState.setSpinnerData(stateAdapterOff, stateAdapter.getItems())
        spOffState.setSelection(spState.selectedItemPosition)
        spOffCity.setSpinnerData(cityAdapterOff, cityAdapter.getItems())
        spOffCity.setSelection(spCity.selectedItemPosition)
    }

    private fun FragmentIndirectLeadCreationBinding.handlingWhenSameAsCheckboxClickedAndUnChecked() {
        enableField(llOffAdd)
        unCheckedSameAsAbove()
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

    private fun showErrorWithFocus(error: ValidationError) {
        removeErrorMessage()
        error.error?.let {
            when (error.identifier as Int) {
                1 -> {
                    binding.tvNameError.setText(getString(it))
                }
                2 -> {
                    binding.tvInterestInError.setText(getString(it))
                }
                3 -> {
                    binding.tvEmailError.setText(getString(it))
                }
                4 , 5-> {
                    binding.tvAlternateEmailError.setText(getString(it))
                }
                6 ,7 -> {
                    binding.tvMobileError.setText(getString(it))
                }
                8 -> {
                    binding.tvLandLineError.setText(getString(it))
                }
                9, 10 -> {
                    binding.tvPANError.setText(getString(it))
                }
                11,12 -> {
                    binding.tvExpectedMarginError.setText(getString(it))
                }
                13 -> {
                    binding.tvCategoryError.setText(getString(it))
                }
                14 -> {
                    binding.tvBackgroundError.setText(getString(it))
                }
                15 -> {
                    binding.tvReferCodeError.setText(getString(it))
                }
                16, 17 -> {
                    binding.tvReferNameError.setText(getString(it))
                }
                18, 19 -> {
                    binding.tvReferMobError.setText(getString(it))
                }
                20 -> {
                    binding.tvReferEmailError.setText(getString(it))
                }
                21 -> {
                    binding.tvPinError.setText(getString(it))
                }
                22 -> {
                    binding.tvOffPinError.setText(getString(it))
                }
                else -> {
                }

            }
        }
    }

    private fun removeErrorMessage() {
        binding.apply {
            tvNameError.setText("")
            tvEmailError.setText("")
            tvAlternateEmailError.setText("")
            tvMobileError.setText("")
            tvLandLineError.setText("")
            tvPANError.setText("")
            binding.tvExpectedMarginError.setText("")
            tvCategoryError.setText("")
            tvBackgroundError.setText("")
            tvReferCodeError.setText("")
            tvReferNameError.setText("")
            tvReferEmailError.setText("")
            tvReferMobError.setText("")
            tvPinError.setText("")
            tvOffPinError.setText("")
            tvInterestInError.setText("")
        }
    }

    override fun renderOnCreateButtonClick(uiModule: UiModel<String>) {
        toggleProgressBar(uiModule.inProgress)
        removeErrorMessage()
        uiModule.onFailure {
            openSectionExpansion(binding.llPersonalInformation, binding.tvPersonalInfo)
            if (it is ValidationException) {
                showErrorWithFocus(it.error)
            }
        }

        uiModule.onSuccess {
            MSFDialog.alertDialog(
                requireContext(), 0,
                resources.getString(string.motilal_oswal), MOSLConstants.CONFIRM_LEAD, "YES",
                "NO", true
            ) { action: Action, _: Array<Any?>? ->
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
                val dedupeModel = response.dedupeModel
                if (data.errors != null) {
                    val errorMsg = data.errors!![0]
                        .message
                    MSFDialog.alertDialog(
                        context, 0,
                        getString(string.motilal_oswal),
                        errorMsg, "OK", true, null
                    )
                } else if (dedupeModel != null && data.errors != null) {
                    dedupeDialog(dedupeModel)
                }
            }else
            MSFDialog.alertDialog(
                context, 0,
                context.getResources().getString(R.string.motilal_oswal),
                data.errors?.get(0)?.message, "OK", true, null
            )
        }
    }

    private fun dedupeDialog(dedupeModel: DedupeModelResponse) {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogDedupeBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.apply {
                when (dedupeModel.dedupeWith) {
                    "Lead" -> {
                        llLeadID.visible()
                        llStage.visible()
                        llProduct.visible()
                        llLeadName.visible()
                        llClientName.gone()
                        llClientCode.gone()
                    }
                    "Prospect" -> {
                        llLeadID.gone()
                        llStage.gone()
                        llProduct.gone()
                        llLeadName.gone()
                        llClientName.gone()
                        llClientCode.gone()
                    }
                    "Client" -> {
                        llLeadID.gone()
                        llStage.gone()
                        llProduct.gone()
                        llLeadName.gone()
                        llClientName.visible()
                        llClientCode.visible()
                    }
                }
                tvLeadName.text = dedupeModel.leadName ?: ""
                tvClientName.text = dedupeModel.clientName ?: ""
                tvClientCode.text = dedupeModel.clientCode ?: ""
                tvProduct.text = dedupeModel.productName ?: ""
                tvCheckMark.text = dedupeModel.checkRemark ?: ""
                tvLeadId.text = dedupeModel.leadID ?: ""
                tvStage.text = dedupeModel.stage ?: ""
                tvDedupeWith.text = dedupeModel.dedupeWith ?: ""
                tvBranch.text = dedupeModel.branchCode ?: ""
                if (dedupeModel.isTransfer == "1") {
                    btnTransfer.visible()
                } else {
                    btnTransfer.gone()
                }
                btnTransfer.setOnClickListener {
                    dismiss()
                    dedupeModel.leadID?.let { subject.onNext(TransferLeadEvent(it)) }
                }
                btnClose.setOnClickListener { dismiss() }
            }

        }
            .show()
    }

    override fun onTransferLeadEventFired(): Observable<TransferLeadEvent> {
        return subject.ofType()
    }

    override fun onTransferLeadPaymentFired(): Observable<TransferLeadPayment> {
        return subject.ofType()
    }

    override fun getMobile(): String {
        return binding.etMobile.text.toString()
    }

    override fun renderTransferLead(uiModel: UiModel<TransferLead>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            if (it.isPaymentDone) {
                MSFDialog.alertDialog(
                    activity, MOSLConstants.LOGOUT_CODE,
                    getString(string.MOSL), it.message, "OK", true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        binding.etMobile.setText(emptyString())
                        binding.etEmailId.setText(emptyString())
                    }
                }
                    .setCancelable(false)

            } else {
                val alertDialog =
                    MSFDialog.alertDialog(
                        activity, MOSLConstants.LOGOUT_CODE,
                        getString(string.MOSL), it.message, "OK", true
                    ) { action: Action, _: Array<Any?>? ->
                        if (action == OK) {
                            subject.onNext(TransferLeadPayment(it.leadIdForTransfer))
                        }
                    }
                alertDialog.setCancelable(false)
            }
        }
    }

    override fun renderStageList(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spStage.setSpinnerData(stageAdapter, it)
        }
    }

    override fun onCallAccountTypeFired(): Observable<CallAccountType> {
       return subject.ofType()
    }


    override fun getLeadCreationUserInput(): IndirectLeadCreationUserInput {

        val resAdd = AddressDetailModel(
            RESIDENCEADDRESS,
            binding.etAdd1.text.toString(),
            binding.etAdd2.text.toString(),
            binding.etAdd3.text.toString(),
            (binding.spCountry.selectedItem as? RowData)?.key
                ?: IndirectLeadCreationView.INVALID_SELECTION_KEY,
            (binding.spState.selectedItem as? RowData)?.key
                ?:  IndirectLeadCreationView.INVALID_SELECTION_KEY,
            (binding.spCity.selectedItem as? RowData)?.key
                ?:  IndirectLeadCreationView.INVALID_SELECTION_KEY,
            binding.etPincode.text.toString(),
            binding.cbPrimaryAddress.isChecked,
            userManager.getUser()?.currentBusinessProfile?.businessUnitId ?: "0"
        )

        val offAdd = AddressDetailModel(
            OFFICEADDRESS,
            binding.etOffAdd1.text.toString(),
            binding.etOffAdd2.text.toString(),
            binding.etOffAdd3.text.toString(),
            (binding.spOffCountry.selectedItem as? RowData)?.key
                ?: IndirectLeadCreationView.INVALID_SELECTION_KEY,
            (binding.spOffState.selectedItem as? RowData)?.key
                ?:  IndirectLeadCreationView.INVALID_SELECTION_KEY,
            (binding.spOffCity.selectedItem as? RowData)?.key
                ?:  IndirectLeadCreationView.INVALID_SELECTION_KEY,
            binding.etOffPincode.text.toString(),
            binding.cbOffPrimaryAddress.isChecked,
            userManager.getUser()?.currentBusinessProfile?.businessUnitId ?: "0"
        )

        return IndirectLeadCreationUserInput(
            selectedCategory = binding.spCategory.selectedItem as RowData,
            etCategoryName = binding.etName.text.toString(),
            selectedInterestIn = binding.spInterestIn.selectedItem as RowData,
            emailId = binding.etEmailId.text.toString(),
            alternateEmailId = binding.etAlternateEmailId.text.toString(),
            mobileNo =  binding.etMobile.text.toString(),
            landlineNo = binding.etLandLine.text.toString(),
            panNo = binding.etPanNum.text.toString()
                .toUpperCase(Locale.ROOT),
            sourceName = binding.spProspectSource.selectedItem.toString(),
            referCode = binding.etRefereeCode.text.toString(),
            referEmail = binding.etRefereeEmail.text.toString(),
            referName = binding.etRefereeName.text.toString(),
            referMobile = binding.etRefereeMobile.text.toString(),
            isRefereeCode = isReferee,
            pinCode = binding.etPincode.text.toString(),
            officePinCode = binding.etOffPincode.text.toString(),
            name = binding.etName.text.toString(),
            isSameAsChecked = binding.cbSameAs.isChecked,
            selectedSource = binding.spSource.selectedItem as RowData,
            selectedProspectSource = binding.spProspectSource.selectedItem as RowData,
            comment = getJsonSafeString(binding.etComments.text.toString()),
            primRM = primaryRM,
            stageId = binding.spStage.selectedItem as RowData,
            statusId = binding.spStatus.selectedItem as RowData,
            residenceAdd = resAdd,
            officeAdd = offAdd,
            isBank = userManager.getUser()?.isBank ?: false,
            branchCode_ = branchCodePreference.get(),
            expectedMargin = binding.etExpectedMargin.text.toString(),
            dob = "",
            alternateMobNo = "",
            ekycMobileNo = "",
            aadharNo = "",
            prospectSourceDetail = "",
            proReferEmail = "",
            proReferName = "",
            proReferMob = "",
            professionalId =  LeadCreationView.SELECT_ROW_DATA,
            selectedProductType =  LeadCreationView.SELECT_ROW_DATA,
            secondaryRM = "",
            selectedBackground = binding.spBackground.selectedItem as RowData,
            selectedWorkExp = binding.spWorkExp.selectedItem as RowData
        )
    }

    override fun isDataEmpty(): Boolean = true

    private fun cleanupView() {
        binding.spCategory.adapter = null
        binding.spSource.adapter = null
        binding.spProspectSource.adapter = null
        binding.spStage.adapter = null
        binding.spCountry.adapter = null
        binding.spOffCountry.adapter = null
        binding.spStatus.adapter = null
        binding.spState.adapter = null
        binding.spOffState.adapter = null
        binding.spCity.adapter = null
        binding.spOffCity.adapter = null
        searchList.setAdapter(null)
        secondaryRMSearchList.setAdapter(null)
    }

    override fun onChipsApiCall(): Observable<CallChipsApi> {
        return subject.ofType()
    }


    override fun onProspectSourceChanged(): Observable<Int> {
        return binding.spProspectSource.itemSelections()
    }

    override fun getSelectedProspectSource(): RowData {
      return  binding.spProspectSource.selectedItem as RowData
    }

    override fun onDestroyView() {
        presenter.stop()
        cleanupView()
        super.onDestroyView()
    }

}