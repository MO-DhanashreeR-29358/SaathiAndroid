package com.mosl.tabapp.leadcreation.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.coreui.spinner.getItems
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.app.coreui.spinner.setSpinnerDataWithSelection
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SelectBank
import com.mosl.mobile.domain.bank.SelectBranch
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.DedupeModelResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogDedupeBinding
import com.mosl.tabapp.databinding.FragmentLeadCreationBinding
import com.mosl.tabapp.leadViewDetail.AttendeeAdapter
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl
import com.mosl.tabapp.leadcreation.domain.LeadCreationPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.domain.TransferLead
import com.mosl.tabapp.leadcreation.ui.LeadCreationUiEvent.*
import com.mosl.tabapp.model.AddressDetailModel
import com.mosl.tabapp.model.EmployeeModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.emptyString
import com.mosl.tabapp.util.gone
import com.mosl.tabapp.util.setErrorMessages
import com.mosl.tabapp.util.visible
import com.mosl.validator.data.ValidationError
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LeadCreationFragment : CommonFragment(),
    LeadCreationView {

    @Inject
    internal lateinit var presenter: LeadCreationPresenter
    private lateinit var binding: FragmentLeadCreationBinding
    private val subject = PublishSubject.create<LeadCreationUiEvent>()
    private var isReferee = false
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

    private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
    private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>

    private val selectedList = ArrayList<RowData>()

    override fun getLayout(): Int = R.layout.fragment_lead_creation

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeadCreationBinding.bind(view)
        createDefaultSelectList()
        setAppTitle(R.string.page_title_lead_creation)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun createDefaultSelectList() {
        selectedList.add(LeadCreationRepositoryImpl.SELECT_ROW_DATA)
    }

    private var primaryRM: String? = null
    private val searchList: CustomListPickerDialog by lazy {
        CustomListPickerDialog(requireContext())
    }

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
                        subject.onNext(AssignToSearchEvent(constraint.toString()))
                    }
                }
                searchList.setmListner(assingViewListner)
            }
        }

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
                        binding.etProspectRefereeName.text.toString()
                            .trim { it <= ' ' }
                    )
                ) {
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            binding.etProspectRefereeName.text
                                .toString()
                                .trim { it <= ' ' }
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
                if (!CRMAppUtil.isBlank(
                        binding.etContactPerson.text
                            .toString()
                            .trim { it <= ' ' }
                    )
                ) {
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            binding.etContactPerson.text
                                .toString()
                                .trim { it <= ' ' }
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
                if (!CRMAppUtil.isBlank(
                        binding.etProspectRefereerMobile.text
                            .toString()
                    )
                ) {
                    if (!CRMValidationRule.validateMobile(
                            binding.etProspectRefereerMobile.text
                                .toString()
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

        bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spBank.adapter = bankAdapter

        branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spBranch.adapter = branchAdapter

        userManager.getUser()?.apply {
            if (isBank) {
                binding.groupBank.visibility = View.VISIBLE
            } else {
                binding.groupBank.visibility = View.GONE
            }
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

    private fun FragmentLeadCreationBinding.handlingWhenPrimaryIsChecked() {
        enableField()
        cbOffPrimaryAddress.gone()
        cbSameAs.visible()
    }

    private fun FragmentLeadCreationBinding.handlingWhenPrimaryIsNotChecked() {
        disableField()
        cbOffPrimaryAddress.visible()
        cbSameAs.gone()
        cbSameAs.isChecked = false
        cbOffPrimaryAddress.isChecked = false
        unCheckedSameAsAbove()
    }

    private fun FragmentLeadCreationBinding.unCheckedSameAsAbove() {
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
                it.editable?.toString()
            }
    }

    override fun renderOnPincodeChange(pincode: String) {
        binding.etOffPincode.setText(pincode)
    }

    override fun onCategoryChanged(): Observable<Int> = binding.spCategory.itemSelections()
    override fun onAssignToSearchEventFired(): Observable<AssignToSearchEvent> = subject.ofType()
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
            .map {
                binding.spOffCity.setSpinnerData(cityAdapterOff, selectedList)
                it
            }

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
        when (category) {
            "INDIVIDUAL", "Individual" -> {
                binding.ilContactPerson.gone()
            }
            else -> {
                binding.tilContactPerson.disableErrorMessages()
                binding.ilContactPerson.visible()

            }
        }
    }

    override fun renderLeadCreationPageData(uiModel: UiModel<LeadCreationPageData>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            binding.spCategory.setSpinnerData(categoryAdapter, data.categoryRowDatas)
            bankAdapter.setSpinnerData(binding.spBank, data.listBank)
            binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
            binding.spInterestIn.setSpinnerData(interestInAdapter, data.interestInRowDatas)
            binding.spSource.setSpinnerData(sourceAdapter, data.sourceRowDatas)
            binding.spStage.setSpinnerData(stageAdapter, data.stageRowDatas)
            binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
            binding.spOffCountry.setSpinnerData(countryAdapterOff, data.countryRowDatas)
        }
    }

    override fun renderProductTypeOnInterestedProductSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { binding.spProductType.setSpinnerData(productTypeAdapter, it) }
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

    override fun renderOnAssignToSearchEventFire(uiModel: UiModel<List<EmployeeModel>>) {
        toggleSearchListProgress(uiModel.inProgress)
        uiModel.onFailure {
            val list = listOf(EmployeeModel.getNoData(getErrorOnFailureNew(it)))
            val attendeeAdapter = AttendeeAdapter(requireContext(), list)
            searchList.setAdapter(attendeeAdapter)
        }

        uiModel.onSuccess {
            val list = if (it.isEmpty()) {
                listOf(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE))
            } else {
                it
            }
            val attendeeAdapter = AttendeeAdapter(requireContext(), list)
            searchList.setAdapter(attendeeAdapter)
        }

    }

    private fun toggleSearchListProgress(inProgress: Boolean) {
        when {
            inProgress -> {
                hideKeyboard(binding.actAssignTo)
                searchList.showProgress(View.VISIBLE)
            }
            else -> searchList.showProgress(View.GONE)
        }
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
        }
    }

    override fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spCity.setSpinnerData(cityAdapter, it)
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

    private fun FragmentLeadCreationBinding.handlingWhenSameAsCheckboxClickedAndChecked() {
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

    private fun FragmentLeadCreationBinding.handlingWhenSameAsCheckboxClickedAndUnChecked() {
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
        error.error?.let {
            when (error.identifier as Int) {
                1 -> {
                    binding.tlCategory.setErrorMessages(getString(it))
                }
                27 -> {
                    binding.tlBank.setErrorMessages(getString(it))
                }
                28 -> {
                    binding.tlBranch.setErrorMessages(getString(it))
                }
                2 -> {
                    val rowData = binding.spCategory.selectedItem as RowData
                    binding.tilName.setErrorMessages(getString(it, rowData.value))
                }
                3 -> {
                    binding.tilName.setErrorMessages(getString(it))
                }
                4, 5 -> {
                    binding.tilContactPerson.setErrorMessages(getString(it))
                }
                6 -> {
                    binding.tlInterest.setErrorMessages(getString(it))
                }
                7 -> {
                    binding.tlProductType.setErrorMessages(getString(it))
                }
                8 -> {
                    binding.tilEmail.setErrorMessages(getString(it))
                }
                9, 10 -> {
                    binding.tilAlternateEmail.setErrorMessages(getString(it))
                }
                11, 12 -> {
                    binding.tilMobile.setErrorMessages(getString(it))
                }
                13, 14 -> {
                    binding.tilAlternateMobile.setErrorMessages(getString(it))
                }
                15 -> {
                    binding.tilLand.setErrorMessages(getString(it))
                }
                16 -> {
                    binding.tilEkycMob.setErrorMessages(getString(it))
                }
                17 -> {
                    binding.tilPan.setErrorMessages(getString(it))
                }
                18 -> {
                    binding.tilAadhar.setErrorMessages(getString(it))
                }
                19 -> {
                    binding.tilReferCode.setErrorMessages(getString(it))
                }
                20, 21 -> {
                    binding.tilReferName.setErrorMessages(getString(it))
                }
                22, 23 -> {
                    binding.tilReferMob.setErrorMessages(getString(it))
                }
                24 -> {
                    binding.tilReferEmail.setErrorMessages(getString(it))
                }
                25 -> {
                    binding.tilPin.setErrorMessages(getString(it))
                }
                26 -> {
                    binding.tilOffPin.setErrorMessages(getString(it))
                }
                else -> {
                }

            }
        }
    }

    private fun removeErrorMessage() {
        binding.apply {
            tlCategory.disableErrorMessages()
            tlBank.disableErrorMessages()
            tlBranch.disableErrorMessages()
            tilName.disableErrorMessages()
            tilContactPerson.disableErrorMessages()
            tilEmail.disableErrorMessages()
            tilAlternateEmail.disableErrorMessages()
            tilAlternateMobile.disableErrorMessages()
            tilMobile.disableErrorMessages()
            tilLand.disableErrorMessages()
            tilEkycMob.disableErrorMessages()
            tilPan.disableErrorMessages()
            tilAadhar.disableErrorMessages()
            tilReferCode.disableErrorMessages()
            tilReferName.disableErrorMessages()
            tilReferEmail.disableErrorMessages()
            tilReferMob.disableErrorMessages()
            tilPin.disableErrorMessages()
            tilOffPin.disableErrorMessages()
            tlInterest.disableErrorMessages()
            tlProductType.disableErrorMessages()
            tlProfession.disableErrorMessages()
        }
    }

    override fun renderOnCreateButtonClick(uiModule: UiModel<String>) {
        toggleProgressBar(uiModule.inProgress)
        removeErrorMessage()
        uiModule.onFailure {
            //showOnFailurePopup(it)
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

    override fun onBranchChange(): Observable<Int> {
        return binding.spBranch.itemSelections()
    }

    override fun onBankChange(): Observable<Int> {
        return binding.spBank.itemSelections()
    }

    override fun getBank(): BankCodeModel? {
        return binding.spBank.selectedItem as? BankCodeModel
    }

    override fun getBranch(): BranchCodeModel? {
        return binding.spBranch.selectedItem as? BranchCodeModel
    }

    override fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure { error ->
            showOnFailurePopup(error)
        }

        uiModel.onSuccess {
            branchAdapter.setSpinnerData(binding.spBranch, it)
        }
    }

    override fun getLeadCreationUserInput(): LeadCreationUserInput {

        val resAdd = AddressDetailModel(
            RESIDENCEADDRESS,
            binding.etAdd1.text.toString(),
            binding.etAdd2.text.toString(),
            binding.etAdd3.text.toString(),
            (binding.spCountry.selectedItem as? RowData)?.key
                ?: LeadCreationView.INVALID_SELECTION_KEY,
            (binding.spState.selectedItem as? RowData)?.key
                ?: "",
            (binding.spCity.selectedItem as? RowData)?.key
                ?: "",
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
                ?: LeadCreationView.INVALID_SELECTION_KEY,
            (binding.spOffState.selectedItem as? RowData)?.key
                ?: "",
            (binding.spOffCity.selectedItem as? RowData)?.key
                ?: "",
            binding.etOffPincode.text.toString(),
            binding.cbOffPrimaryAddress.isChecked,
            userManager.getUser()?.currentBusinessProfile?.businessUnitId ?: "0"
        )

        return LeadCreationUserInput(
            selectedCategory = binding.spCategory.selectedItem as RowData,
            selectedBank = binding.spBank.selectedItem as? BankCodeModel ?: SelectBank,
            selectedBranch = binding.spBranch.selectedItem as? BranchCodeModel ?: SelectBranch,
            etCategoryName = binding.etName.text.toString(),
            etContactPerson = binding.etContactPerson.text.toString(),
            selectedProfession = binding.spProfession.selectedItem as RowData,
            selectedInterestIn = binding.spInterestIn.selectedItem as RowData,
            selectedProductType = binding.spProductType.selectedItem as? RowData
                ?: LeadCreationView.SELECT_ROW_DATA,
            emailId = binding.etEmailId.text.toString(),
            alternateEmailId = binding.etAlternateEmailId.text.toString(),
            mobileNo = binding.etMobile.text.toString(),
            alternateMobileNo = binding.etAlternateMobno.text.toString(),
            landlineNo = binding.etLandLine.text.toString(),
            ekycMobileNo = binding.etEkycMobile.text.toString(),
            panNo = binding.etPanNum.text.toString()
                .toUpperCase(Locale.ROOT),
            aadharNo = binding.etAadharNum.text.toString(),
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
            prospectSourceDetail = binding.etProspectSourceDetails.text.toString(),
            proReferName = binding.etProspectRefereeName.text.toString(),
            proReferEmail = binding.etProspectRefereerEmail.text.toString(),
            proReferMob = binding.etProspectRefereerMobile.text.toString(),
            comment = getJsonSafeString(binding.etComments.text.toString()),
            professionalId = binding.spProfession.selectedItem as RowData,
            primRM = primaryRM,
            stageId = binding.spStage.selectedItem as RowData,
            statusId = binding.spStatus.selectedItem as RowData,
            residenceAdd = resAdd,
            officeAdd = offAdd,
            isBank = userManager.getUser()?.isBank ?: false
        )
    }

    override fun isDataEmpty(): Boolean = true

    private fun cleanupView() {
        binding.spCategory.adapter = null
        binding.spProfession.adapter = null
        binding.spBank.adapter = null
        binding.spBranch.adapter = null
        binding.spSource.adapter = null
        binding.spProductType.adapter = null
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
    }

    override fun onDestroyView() {
        presenter.stop()
        cleanupView()
        super.onDestroyView()
    }

}