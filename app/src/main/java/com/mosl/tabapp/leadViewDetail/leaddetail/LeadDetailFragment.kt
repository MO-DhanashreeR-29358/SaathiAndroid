package com.mosl.tabapp.leadViewDetail.leaddetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.getItems
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.app.coreui.spinner.setSpinnerDataWithSelection
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.model.lead.*
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.common.*
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogLeadviewAddProductBinding
import com.mosl.tabapp.databinding.DialogUpdateProfileBinding
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.leadViewDetail.*
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leaddetail.*
import com.mosl.tabapp.leaddetail.Contact
import com.mosl.tabapp.model.EmployeeModel
import com.mosl.tabapp.model.LeadFieldSectionModel
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel
import com.mosl.tabapp.model.leaddetail.LeadDetailModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.*
import com.mosl.validator.exception.ValidationException
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LeadDetailFragment : CommonFragment(), LeadDetailView {

    @Inject
    internal lateinit var presenter: LeadDetailPresenter
    private lateinit var binding: FragmentLeadDetailBinding
    private var isReferee = false

    private var leadId = "0"
    private var ucId = "0"

    private var stage: String? = null
    private var status: String? = null

    private val subject = PublishSubject.create<LeadDetailUiEvent>()

    private var interestedDatas = emptyList<RowData>()
    private var statusDatas = emptyList<RowData>()
    private var stageDatas = emptyList<RowData>()

    var pmsList: List<String> = emptyList()


    private val professionAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val profileAdapter: ArrayAdapter<RowData> by lazy {

        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val interestedAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stageAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val statusAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val pmsProductAdapter: ArrayAdapter<String> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<String>(requireContext())
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

    override fun getLayout(): Int {
        return R.layout.fragment_lead_detail
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeadDetailBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun setupView() {
        with(binding) {
            val arrayPmsProduct =
                    requireActivity().resources.getStringArray(R.array.selectPmsProduct)
            pmsList =
                    ArrayList(Arrays.asList(*arrayPmsProduct))
            setIcon(llClientDetail, tvClientDetail)
            setIcon(llLeadDetail, tvLeadDetail)
            setIcon(llClientPortfolio, tvClientPortfolioDetails)
            etDob.inputType = InputType.TYPE_NULL
            etAnniversary.inputType = InputType.TYPE_NULL
            etPanNumber.filters = arrayOf(
                    InputFilter.LengthFilter(10),
                    InputFilter.AllCaps()
            )
            includePortfolio.apply {
                btnBrokPortfolio.gone()
                btnAssetPortfolio.gone()
                llPortfolio.visible()
                llBorkingPortfolio.visible()
                llCash.visible()
                llAssestPortfolio.visible()
                setIcon(llPortfolio, txtPortfolioSnapshot, "OUTER")
                setIcon(llProfitLoss, txtPorfitLoss, "OUTER")
                setIcon(llAvailableLimits, txtAvailableLimits, "OUTER")

                setIcon(llBorkingPortfolio, txtBrokingPortfolio, "INNER")
                setIcon(llAssestPortfolio, txtAssestPortfolio, "INNER")
                setIcon(llCash, txtCash, "INNER")

                setIcon(llBorkingProducts, txtBrokingProducts, "INNER")
                setIcon(llAssestProducts, txtAssestProducts, "INNER")
            }
        }
    }

    private fun setIcon(
            parent: View,
            icon: TextView,
            Section: String
    ) {
        if (Section.equals("OUTER", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            }
        } else if (Section.equals("INNER", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.down_button, 0)
            }
        }
    }

    override fun renderArguments(data: LeadDetailArgument) {
        leadId = data.leadId
        ucId = data.ucId
    }

    override fun onNestedScrollViewTouched(): Observable<MotionEvent> {
        return binding.nestedScrollView.touches { false }
    }

    override fun renderOnNestedScrollViewTouch() {
        binding.nestedScrollView.requestFocusFromTouch()
    }

    override fun onPrimaryAddCheckboxClicked(): Observable<Unit> {
        return binding.cbPrimaryAddress.clicks()
    }

    override fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit> {
        return binding.cbOffPrimaryAddress.clicks()
    }

    override fun onSameAsCheckboxClicked(): Observable<Unit> {
        return binding.cbSameAs.clicks()
    }

    override fun onSubmitButtonClicked(): Observable<Unit> {
        return binding.btnSubmit.clicks()
    }

    override fun onProfileLayoutClicked(): Observable<Unit> {
        return binding.llProfile.clicks()
    }

    override fun onUpdateProfileEventFired(): Observable<UpdateProfile> {
        return subject.ofType()
    }

    override fun renderUpdateProfile(uiModel: UiModel<Pair<String, String>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            if (it.first.isEmpty())
                binding.tvProfile.text = it.second
            else
                MSFDialog.alertDialog(
                        requireContext(),
                        0,
                        getString(R.string.motilal_oswal),
                        it.first,
                        "OK",
                        true,
                        null
                )
        }
    }

    override fun renderProfileClick() {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogUpdateProfileBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.spProfile.apply {
                adapter = profileAdapter
            }
            dialogBinding.ivClose.setOnClickListener { dismiss() }
            dialogBinding.btnUpdate.setOnClickListener {
                val selectedProfile = dialogBinding.spProfile.selectedItem as RowData
                subject.onNext(UpdateProfile(selectedProfile))
                dismiss()
            }

        }
                .show()
    }

    override fun onAddProductClicked(): Observable<Unit> {
        return binding.rlAddProduct.clicks()
    }

    override fun isLeadEditable(): Boolean = isLeadEditable

    private var specialistAdd: String? = null
    private var primaryRm: String? = null

    private var addProductDialogBinding: DialogLeadviewAddProductBinding? = null

    private var productSpecialDialog: CustomListPickerDialog? = null
    private var assignToDialog: CustomListPickerDialog? = null

    override fun onProductSpecialEventFired(): Observable<ProductSpecialistEvent> {
        return subject.ofType()
    }

    override fun renderAssignTo(uiModel: UiModel<List<LeadEmployees>>) {
        if (uiModel.inProgress) {
            assignToDialog?.showProgress(View.VISIBLE)
        } else {
            assignToDialog?.showProgress(View.GONE)
        }

        uiModel.onFailure {
            val model = listOf(
                    EmployeeModel.getNoData(getErrorOnFailureNew(it))
            )
            assignToDialog?.setAdapter(
                    AttendeeAdapter(
                            requireContext(),
                            model
                    )
            )
        }

        uiModel.onSuccess {
            val list = emptyList<EmployeeModel>()
            if (it.isNotEmpty()) {
                it.filter {
                    it.code == userManager.user.employeeCode
                }
                        .map {
                            EmployeeModel(
                                    it.name, it.code, it.branchCode, false
                            )
                        }
            } else {
                EmployeeModel.getNoData(MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE)
            }

            assignToDialog?.setAdapter(
                    AttendeeAdapter(
                            requireContext(), list
                    )
            )
        }

    }

    override fun addProductEventFired(): Observable<AddProductEvent> {
        return subject.ofType()
    }

    override fun renderAddProduct(uiModel: UiModel<List<AddProductData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG)
                    .show()

            if ("0".equals(leadId, ignoreCase = true)) {

                val leadID = it[0].leadId
                val uCID = it[0].ucId
                MSFDialog.alertDialog(
                        requireActivity(), 0,
                        resources.getString(
                                R.string.motilal_oswal
                        ), "Product Added Successfully with leadId "
                        + leadID, "OK", true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        val bundle = Bundle()
                        bundle.putString(MOSLConstants.LEAD_ID, leadID)
                        bundle.putString(MOSLConstants.UCID, uCID)
                        navigator.openPage(
                                NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                                forceReload = false
                        )
                    }
                }
            } else {
                if (it.isNotEmpty()) {
                    for (datum in it) {
                        val potentialPMSProduct = stringOrEmpty(datum.potentialPmsProduct)
                        isPotentialPMSProduct = potentialPMSProduct.isEmpty()
                        pmsProductAdapter.setSpinnerDataWithSelection(
                                binding.spPmsProduct, pmsProductArray.toList(), potentialPMSProduct
                        )
                        leadDetailAddProductList.add(getLeadDetailAddProductModel(datum))
                    }
                    leadDetailAddProductAdapter =
                            LeadDetailAddProductAdapter(
                                    saathiApi,
                                    requireContext(),
                                    leadDetailAddProductList,
                                    binding.lvAddProduct,
                                    leadId,
                                    isLeadEditable,
                                    statusDatas,
                                    statusAdapter,
                                    userManager.getUser()?.employeeCode,
                                    status,
                                    userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                                    (binding.spPmsProduct.selectedItem as? RowData)?.value
                                            ?: emptyString()
                            )
                    binding.lvAddProduct.adapter = leadDetailAddProductAdapter
                    CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail(binding.lvAddProduct)
                }
                MSFDialog.alertDialog(
                        requireActivity(), 0,
                        resources.getString(R.string.motilal_oswal),
                        "Product Added Successfully", "OK",
                        true
                ) { _: Action?, _: Array<Any?>? ->
                    //leadSection()
                }
            }
        }
    }

    private val pmsProductArray = arrayOf("Select Potential Product", "YES", "NO")

    override fun renderProductSpecial(uiModel: UiModel<List<LeadEmployees>>) {
        if (uiModel.inProgress) {
            productSpecialDialog?.showProgress(View.VISIBLE)
        } else {
            productSpecialDialog?.showProgress(View.GONE)
        }

        uiModel.onFailure {
            val model = listOf(
                    EmployeeModel.getNoData(getErrorOnFailureNew(it))
            )
            productSpecialDialog?.setAdapter(
                    AttendeeAdapter(
                            requireContext(),
                            model
                    )
            )
        }

        uiModel.onSuccess {
            val list = if (it.isNotEmpty()) {
                it.filter {
                    it.code != userManager.user.employeeCode
                }
                        .map {
                            EmployeeModel(
                                    it.name, it.code, it.branchCode, false
                            )
                        }
            } else {
                listOf(EmployeeModel.getNoData(MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE))
            }

            productSpecialDialog?.setAdapter(
                    AttendeeAdapter(
                            requireContext(), list
                    )
            )
        }

    }

    override fun onAssignToEventFired(): Observable<AssignToEvent> {
        return subject.ofType()
    }

    private var isPotentialPMSProduct = false

    override fun openAddProductDialog() {
        Dialog(requireContext()).apply {
            addProductDialogBinding = DialogLeadviewAddProductBinding.inflate(layoutInflater)
            val dialogBinding = addProductDialogBinding!!
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.dacProductSpecialist.setHint(resources.getString(R.string.str_prod_spl))
            dialogBinding.actAssignTo.setHint(resources.getString(R.string.str_assing))
            dialogBinding.dacProductSpecialist.setListener {
                specialistAdd = ""
            }
            dialogBinding.dacProductSpecialist.setOnClickListener {

                requireActivity().window
                        .setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                        or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        )

                val searchListProductSpecialist =
                        CustomListPickerDialog(requireContext())

                searchListProductSpecialist.threshold = MOSLConstants.SEARCH_LENTH

                searchListProductSpecialist.show()
                searchListProductSpecialist.setCustomTitle(
                        resources.getString(R.string.str_prod_spl)
                )
                val productSpecialistViewListner: ListPickerListneable =
                        object : ListPickerListneable {
                            override fun onListItemSelect(data: Parcelable) {
                                val clientModel =
                                        data as EmployeeModel
                                if (clientModel.isNoData) {
                                    return
                                }
                                dialogBinding.dacProductSpecialist.setText(clientModel.attendeeName)
                                specialistAdd = clientModel.attendeeCode
                                dialogBinding.dacProductSpecialist.setClearIconVisible(true)
                                searchListProductSpecialist.dismiss()
                            }

                            override fun onSearchTextChanged(constraint: CharSequence?) {
                                if (constraint != null) {

                                    primaryRm = if (CRMAppUtil.isBlank(leadOwne.trim())) {
                                        advisoryCode
                                    } else {
                                        leadOwne
                                    }
                                    val rm = primaryRm!!
                                    productSpecialDialog = searchListProductSpecialist
                                    subject.onNext(
                                            ProductSpecialistEvent(
                                                    selectedProduct = constraint.toString(),
                                                    primaryRM = rm
                                            )
                                    )

                                    //searchProductSpecialist(constraint, searchListProductSpecialist)
                                }
                            }
                        }
                searchListProductSpecialist.setmListner(productSpecialistViewListner)

            }
            dialogBinding.actAssignTo.setListener {
                primaryRm = ""
            }
            dialogBinding.actAssignTo.setOnClickListener {
                requireActivity().window
                        .setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                        or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        )
                val searchList = CustomListPickerDialog(requireContext())
                searchList.show()
                searchList.threshold = MOSLConstants.SEARCH_LENTH
                searchList.setCustomTitle(resources.getString(R.string.str_assing))
                val assingViewListner: ListPickerListneable = object : ListPickerListneable {
                    override fun onListItemSelect(data: Parcelable) {
                        val clientModel =
                                data as EmployeeModel
                        if (clientModel.isNoData) {
                            return
                        }
                        dialogBinding.actAssignTo.setText(clientModel.attendeeName)
                        dialogBinding.actAssignTo.isEnabled = true
                        primaryRm = clientModel.attendeeCode
                        dialogBinding.actAssignTo.setClearIconVisible(true)
                        searchList.dismiss()
                    }

                    override fun onSearchTextChanged(constraint: CharSequence?) {

                        if (constraint != null) {
                            assignToDialog = searchList
                            subject.onNext(AssignToEvent(constraint.toString()))

                            //searchUserBusinessProfile(constraint, searchList)
                        }
                    }
                }
                searchList.setmListner(assingViewListner)
            }
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            dialogBinding.spInterestedIn.adapter = interestedAdapter
            dialogBinding.spStage.adapter = stageAdapter
            dialogBinding.spStatus.adapter = statusAdapter
            interestedAdapter.setSpinnerData(dialogBinding.spInterestedIn!!, interestedDatas)

            dialogBinding.spInterestedIn.itemSelections()
                    .filter(InvalidSelectionPredicate)
                    .map { dialogBinding.spInterestedIn.selectedItem as RowData }
                    .filter { it.key != LeadDetailView.INVALID_SELECTION_KEY }
                    .subscribe {
                        subject.onNext(SelectInterestedProductEvent(it))
                    }
                    .addTo(disposable)

            dialogBinding.spStage.itemSelections()
                    .filter(InvalidSelectionPredicate)
                    .map { dialogBinding.spStage.selectedItem as RowData }
                    .filter { it.key != LeadDetailView.INVALID_SELECTION_KEY }
                    .subscribe {
                        subject.onNext(SelectStageEvent(it))
                    }
                    .addTo(disposable)




            dialogBinding.btnSave.setOnClickListener {

                //Mandatory check
                if (dialogBinding.spInterestedIn.selectedItemPosition < 1) {
                    dialogBinding.tvspIntrestError.visibility = View.VISIBLE
                    dialogBinding.tvspIntrestError.text = "Select Interested Product"

                }
                /*if (dialogBinding.spStage.selectedItemPosition < 1) {
                          dialogBinding.tvspStageError.visibility = View.VISIBLE
                          dialogBinding.tvspStageError.text = "Select Stage"
                        }
                        if (dialogBinding.spStatus.selectedItemPosition < 1) {
                          dialogBinding.tvspStatusError.visibility = View.VISIBLE
                          dialogBinding.tvspStatusError.text = "Select Status"
                        }*/
                ///////////

                //Mandatory chec end
                val interestedInKey =
                        (dialogBinding.spInterestedIn.selectedItem as RowData).key
                val interestedInDisplayText =
                        (dialogBinding.spInterestedIn.selectedItem as RowData).value
                if (leadDetailAddProductList.size == 0) {
                    leadDetailAddProductAdapter =
                            LeadDetailAddProductAdapter(
                                    saathiApi,
                                    requireContext(),
                                    leadDetailAddProductList,
                                    binding.lvAddProduct,
                                    leadId,
                                    isLeadEditable,
                                    statusDatas,
                                    statusAdapter,
                                    userManager.getUser()?.employeeCode,
                                    status,
                                    userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                                    (binding.spPmsProduct.selectedItem as? RowData)?.value
                                            ?: emptyString()
                            )
                }
                if (leadDetailAddProductAdapter?.checkProduct(interestedInKey) ?: false) {
                    MSFDialog.alertDialog(
                            requireContext(), 0,
                            resources.getString(R.string.motilal_oswal),
                            "$interestedInDisplayText is already selected", "OK", true, null
                    )
                } else {
                    if (isPotentialPMSProduct) {
                        MSFDialog.alertDialog(
                                requireActivity(),
                                0,
                                resources.getString(R.string.motilal_oswal),
                                MOSLConstants.POTENTIAL_INVEST_IN_PRODUCT,
                                MOSLConstants.YES,
                                MOSLConstants.NO,
                                true
                        ) { action: Action, data: Array<Any?>? ->
                            if (action == Action.OK) {
                                addProduct(
                                        (dialogBinding.spInterestedIn.getSelectedItem() as RowData).key,
                                        (dialogBinding.spStage.getSelectedItem() as RowData).key,
                                        (dialogBinding.spStatus.getSelectedItem() as RowData).key,
                                        MOSLConstants.YES
                                )
                            } else {
                                addProduct(
                                        (dialogBinding.spInterestedIn.getSelectedItem() as RowData).key,
                                        (dialogBinding.spStage.getSelectedItem() as RowData).key,
                                        (dialogBinding.spStatus.getSelectedItem() as RowData).key,
                                        MOSLConstants.NO
                                )
                            }
                        }
                    } else {
                        addProduct(
                                (dialogBinding.spInterestedIn.getSelectedItem() as RowData).key,
                                (dialogBinding.spStage.getSelectedItem() as RowData).key,
                                (dialogBinding.spStatus.getSelectedItem() as RowData).key, ""
                        )
                    }
                }
                dismiss()
            }

            dialogBinding.btnClose.setOnClickListener {
                hideKeyboard(binding.root)
                dismiss()
            }
        }
                .show()
    }

    private fun addProduct(
            interestedIn: String,
            stage: String,
            status: String,
            productMapping: String
    ) {
        //data
        val data = InsertLeadProductRequest(
                leadId,
                ucId,
                interestedIn,
                interestedIn,
                primaryRm ?: "",
                specialistAdd ?: "",
                userManager.getUser()?.employeeCode.toString(),
                CRMAppUtil.getDBDate(),
                stage,
                status,
                productMapping
        )
        //event
        subject.onNext(AddProductEvent(data))
    }

    override fun onEditLeadDetailClicked(): Observable<Unit> {
        return binding.llEditLeadDetail.clicks()
    }

    private var editableFlag = false

    override fun renderEditLeadDetailClick() {
        if (leadId != "0" && isLeadEditable) {
            if (editableFlag) {
                setEnableDisable(true)
                editableFlag = false
            } else {
                setEnableDisable(false)
                editableFlag = true
            }
        }
    }

    override fun renderPrimaryAddCheckBoxClicked() {
        binding.run {
            if (cbPrimaryAddress.isChecked) {
                enableField(llResAdd)
                enableField(llOffAdd)
                cbOffPrimaryAddress.gone()
                cbSameAs.visible()
            } else {
                cbOffPrimaryAddress.visible()
                cbSameAs.gone()
                disableField(llResAdd)
                disableField(llOffAdd)
                cbOffPrimaryAddress.isChecked = false
                cbSameAs.isChecked = false
            }
        }
    }

    override fun renderOffPrimaryAddCheckBoxClicked() {
        binding.run {
            if (cbOffPrimaryAddress.isChecked) {
                enableField(llResAdd)
                enableField(llOffAdd)
            } else {
                disableField(llResAdd)
                disableField(llOffAdd)
            }
        }
    }

    override fun renderSameAsAboveClicked() {
        binding.run {
            when {
                cbSameAs.isChecked -> handlingWhenSameAsCheckboxClickedAndChecked()
                else -> handlingWhenSameAsCheckboxClickedAndUnChecked()
            }
        }
    }

    private fun handlingWhenSameAsCheckboxClickedAndUnChecked() {
        binding.run {
            enableField(llOffAdd)
            etOffAdd1.setText("")
            etOffAdd2.setText("")
            etOffAdd3.setText("")
            etOffPincode.setText("")
            spOffCountry.setSelection(0)
            spOffState.setSelection(0)
            spOffCity.setSelection(0)
        }
    }


    private fun handlingWhenSameAsCheckboxClickedAndChecked() {
        binding.run {
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
    }

    override fun onClientDetailClicked(): Observable<Unit> {
        return binding.tvClientDetail.clicks()
    }

    override fun onClientPortfolioDetailClicked(): Observable<Unit> {
        return binding.tvClientPortfolioDetails.clicks()
    }

    override fun onLeadDetailClicked(): Observable<Unit> {
        return binding.tvLeadDetail.clicks()
    }

    override fun onDocumentClicked(): Observable<Unit> {
        return binding.tvDocuments.clicks()
    }

    override fun onDOBClicked(): Observable<Unit> {
        return binding.etDob.clicks()
    }

    override fun renderDOBClick() {
        CustomDatePickerDialog(
                requireContext(),
                false,
                binding.etDob.text.toString(),
                true
        )
        { date ->
            binding.tvAnniversaryError.visibility = View.GONE
            binding.etDob.setText(date)
            binding.etDob.setClearIconVisible(true)
        }.show()
    }

    override fun renderAnniversaryClick() {
        CustomDatePickerDialog(
                requireContext(),
                false,
                binding.etAnniversary.text.toString(),
                true
        )
        { date ->
            binding.tvAnniversaryError.visibility = View.GONE
            binding.etAnniversary.setText(date)
            binding.etAnniversary.setClearIconVisible(true)
        }.show()
    }

    override fun onAnniversaryClicked(): Observable<Unit> {
        return binding.etAnniversary.clicks()
    }

    override fun onRefereeCodeFocusChanged(): Observable<Boolean> {
        return binding.etRefereeCode.focusChanges()
    }

    override fun onClientDetailSectionClicked(): Observable<Unit> {
        return binding.tvClientDetail.clicks()
    }

    override fun onClientPortfolioSectionClicked(): Observable<Unit> {
        return binding.tvClientPortfolioDetails.clicks()
    }

    override fun onLeadDetailSectionClicked(): Observable<Unit> {
        return binding.tvLeadDetail.clicks()
    }

    override fun toggleClientDetailSection() {
        toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
    }

    override fun toggleLeadDetailSection() {
        toggleSectionExpansion(binding.llLeadDetail, binding.tvLeadDetail)
    }

    override fun toggleClientPortfolioSection() {
        toggleSectionExpansion(binding.llClientPortfolio, binding.tvClientPortfolioDetails)
    }

    override fun onPortfolioSnapshotClicked(): Observable<Unit> {
        return binding.includePortfolio.txtPortfolioSnapshot.clicks()
    }

    override fun togglePortfolio() {
        toggleSectionExpansion(
                binding.includePortfolio.llPortfolio,
                binding.includePortfolio.txtPortfolioSnapshot,
                OUTER
        )
    }

    override fun onProfitLossSnapshotClicked(): Observable<Unit> {
        return binding.includePortfolio.txtPorfitLoss.clicks()
    }

    override fun onAvailableLimitSnapshotClicked(): Observable<Unit> {
        return binding.includePortfolio.txtAvailableLimits.clicks()
    }

    override fun toggleProfitLoss() {
        toggleSectionExpansion(
                binding.includePortfolio.llProfitLoss,
                binding.includePortfolio.txtPorfitLoss,
                OUTER
        )
    }

    override fun toggleAvailableLimit() {
        toggleSectionExpansion(
                binding.includePortfolio.llAvailableLimits,
                binding.includePortfolio.txtAvailableLimits,
                OUTER
        )
    }

    override fun onBrokingPortfolioClicked(): Observable<Unit> {
        return binding.includePortfolio.txtBrokingPortfolio.clicks()
    }

    override fun toggleBrokingPortfolio() {
        toggleSectionExpansion(
                binding.includePortfolio.llBorkingPortfolio,
                binding.includePortfolio.txtBrokingPortfolio,
                INNER
        )
    }

    override fun onAssetPortfolioClicked(): Observable<Unit> {
        return binding.includePortfolio.txtAssestPortfolio.clicks()

    }

    override fun toggleAssetPortfolio() {
        toggleSectionExpansion(
                binding.includePortfolio.llAssestPortfolio,
                binding.includePortfolio.txtAssestPortfolio,
                INNER
        )
    }

    override fun onLedgerClicked(): Observable<Unit> {
        return binding.includePortfolio.txtCash.clicks()
    }

    override fun toggleLedger() {
        toggleSectionExpansion(
                binding.includePortfolio.llCash,
                binding.includePortfolio.txtCash,
                INNER
        )
    }

    override fun onBrokingProductClicked(): Observable<Unit> {
        return binding.includePortfolio.txtBrokingProducts.clicks()
    }

    override fun toggleBrokingProduct() {
        toggleSectionExpansion(
                binding.includePortfolio.llBorkingProducts,
                binding.includePortfolio.txtBrokingProducts,
                INNER
        )
    }

    override fun onAssetProductClicked(): Observable<Unit> {
        return binding.includePortfolio.txtAssestProducts.clicks()
    }

    override fun toggleAssetProduct() {
        toggleSectionExpansion(
                binding.includePortfolio.llAssestProducts,
                binding.includePortfolio.txtAssestProducts,
                INNER
        )
    }

    override fun renderOnRefereeCodeFocusChange() {
        if (binding.etRefereeCode.string().length < 4) {
            binding.etRefereeName.setText(emptyString())
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeNumber.setText(emptyString())
        }
    }

    override fun onRefereeNameFocusChanged(): Observable<Boolean> {
        return binding.etRefereeName.focusChanges()
    }

    override fun renderOnRefereeNameFocusChange() {
        if (binding.etRefereeName.string().length < 3) {
            binding.etRefereeCode.setText(emptyString())
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeNumber.setText(emptyString())
        }
    }

    override fun onLeadNameAfterTextChanged(): Observable<String> {
        return binding.etLeadName.afterTextChangeEvents()
                .map { it.editable?.toString() ?: emptyString() }
    }

    override fun renderOnLeadNameAfterTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            text
                    ) -> binding.tlLeadName.disableErrorMessages()
                    else -> {
                        binding.tlLeadName.setErrorMessages(MOSLConstants.ErrorMsgName)
                    }
                }
            }
            else -> {
                binding.tlLeadName.disableErrorMessages()
            }
        }
    }

    override fun onProsRefereeNameAfterTextChanged(): Observable<String> {
        return binding.etProspectRefereeName.afterTextChangeEvents()
                .map { it.editable?.toString() ?: emptyString() }
    }

    override fun renderOnProsRefereeNameAfterTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            text
                    ) -> binding.tlProspectRefereeName.disableErrorMessages()
                    else -> {
                        binding.tlProspectRefereeName.setErrorMessages(MOSLConstants.ErrorMsgName)
                    }
                }
            }
            else -> {
                binding.tlProspectRefereeName.disableErrorMessages()
            }
        }
    }

    override fun onProsRefereeMobileTextChanged(): Observable<String> {
        return binding.etProspectRefereerMobile.textChanges()
                .map { it.toString() }
    }

    override fun renderOnProsRefereeMobileTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            text
                    ) -> binding.tlProspectRefereeMob.disableErrorMessages()
                    else -> {
                        binding.tlProspectRefereeMob.setErrorMessages(MOSLConstants.ErrorMsgName)
                    }
                }
            }
            else -> {
                binding.tlProspectRefereeMob.disableErrorMessages()
            }
        }
    }

    override fun onProsRefereeEmailTextChanged(): Observable<String> {
        return binding.etProspectRefereerEmail.textChanges()
                .map { it.toString() }
    }

    override fun renderOnProsRefereeEmailTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMValidationRule.isValidEmail(
                            text
                    ) -> binding.tlProspectRefereeEmail.disableErrorMessages()
                    else -> {
                        binding.tlProspectRefereeEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
                    }
                }
            }
            else -> {
                binding.tlProspectRefereeEmail.disableErrorMessages()
            }
        }
    }

    override fun onContactPersonAfterTextChanged(): Observable<String> {
        return binding.etContactPerson.afterTextChangeEvents()
                .map { it.editable?.toString() ?: emptyString() }
    }

    override fun renderOnContactPersonAfterTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                            text
                    ) -> binding.tlContactPerson.disableErrorMessages()
                    else -> {
                        binding.tlContactPerson.setErrorMessages(MOSLConstants.ErrorMsgName)
                    }
                }
            }
            else -> {
                binding.tlContactPerson.disableErrorMessages()
            }
        }
    }

    override fun onLandLineAfterTextChanged(): Observable<String> {
        return binding.etLandlineNumber.afterTextChangeEvents()
                .map { it.editable?.toString() ?: emptyString() }
    }

    override fun renderOnLandLineAfterTextChange(text: String) {
        if (binding.etLandlineNumber.isEnabled) {
            when {
                text.isNotBlank() -> {
                    when {
                        CRMValidationRule.validateTel(
                                text
                        ) -> binding.tlLand.disableErrorMessages()
                        else -> {
                            binding.tlLand.setErrorMessages(MOSLConstants.INVALID_LAND_LINE)
                        }
                    }
                }
                else -> {
                    binding.tlLand.disableErrorMessages()
                }
            }
        }
    }

    override fun onAlterMobileTextChanged(): Observable<String> {
        return binding.etAlternateMobileNumber.textChanges()
                .map { it.toString() }
    }

    override fun renderOnAlterMobileTextChange(text: String) {
        if (binding.etAlternateMobileNumber.isEnabled) {
            when {
                text.isNotBlank() -> {
                    when {
                        CRMValidationRule.validateMobile(
                                text
                        ) -> binding.tlAlternateMobile.disableErrorMessages()
                        else -> {
                            binding.tlAlternateMobile.setErrorMessages(MOSLConstants.INVALID_MOBILE_NO)
                        }
                    }
                }
                else -> {
                    binding.tlAlternateMobile.disableErrorMessages()
                }
            }
        }
    }

    override fun onAlterEmailTextChanged(): Observable<String> {
        return binding.etAlternateEmail.textChanges()
                .map { it.toString() }
    }

    override fun renderOnAlterEmailTextChange(text: String) {
        if (binding.llEditLeadDetail.isEnabled) {
            when {
                text.isNotBlank() -> {
                    when {
                        CRMValidationRule.isValidEmail(
                                text
                        ) -> binding.tlAlternateEmail.disableErrorMessages()
                        else -> {
                            binding.tlAlternateEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
                        }
                    }
                }
                else -> {
                    binding.tlAlternateEmail.disableErrorMessages()
                }
            }
        }
    }

    override fun onEmailTextChanged(): Observable<String> {
        return binding.etEmail.textChanges()
                .map { it.toString() }
    }

    override fun renderOnEmailTextChange(text: String) {
        if (binding.llEditLeadDetail.isEnabled) {
            when {
                text.isNotBlank() -> {
                    when {
                        CRMValidationRule.isValidEmail(
                                text
                        ) -> binding.tlEmail.disableErrorMessages()
                        else -> {
                            binding.tlEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
                        }
                    }
                }
                else -> {
                    binding.tlEmail.disableErrorMessages()
                }
            }
        }
    }

    override fun onPanTextChanged(): Observable<String> {
        return binding.etPanNumber.textChanges()
                .map { it.toString() }
    }

    override fun renderOnPanTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMValidationRule.isValidPannumber(
                            text
                    ) -> binding.tlPan.disableErrorMessages()
                    else -> {
                        binding.tlPan.setErrorMessages(MOSLConstants.INVALID_PAN)
                    }
                }
            }
            else -> {
                binding.tlPan.disableErrorMessages()
            }
        }
    }

    private var advisoryCode = ""

    override fun renderClientDetail(uiModel: UiModel<ClientDetail>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->

            if (data.products.isNotEmpty()) {
                val product = data.products[0]
                binding.tvClientCode.text = product.clientCode
                binding.tvUCID.text = product.ucId
                binding.tvBranchCode.text = product.branchCode
                binding.tvChannel.text = product.channel
                advisoryCode = product.advisorCode
            }

            binding.tvClientAddress.text = data.address
            binding.tvClientName.text = data.name
            binding.tvPanCard.text = data.pan
            binding.tvAccountType.text = data.accountType
            binding.tvFamilyUCID.text = data.familyUcid
            binding.tvAdvisoryType.text = data.advisoryType
            binding.tvAadharCard.text = data.aadhar
            binding.tvAccountNumber.text = data.account
            binding.tvProfile.text = data.profile
            binding.tvDefaultBank.text = data.defaultBank

            binding.llClient.visibility = View.VISIBLE
            binding.llClientPortfolioDetails.visibility = View.VISIBLE
            toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
        }
    }

    private fun ClientDetail.renderClient() {
        if (products.isNotEmpty()) {
            val product = products[0]
            binding.tvClientCode.text = product.clientCode
            binding.tvUCID.text = product.ucId
            binding.tvBranchCode.text = product.branchCode
            binding.tvChannel.text = product.channel
            advisoryCode = product.advisorCode
        }

        binding.tvClientAddress.text = address
        binding.tvClientName.text = name
        binding.tvPanCard.text = pan
        binding.tvAccountType.text = accountType
        binding.tvFamilyUCID.text = familyUcid
        binding.tvAdvisoryType.text = advisoryType
        binding.tvAadharCard.text = aadhar
        binding.tvAccountNumber.text = account
        binding.tvProfile.text = profile
        binding.tvDefaultBank.text = defaultBank

        binding.llClient.visibility = View.VISIBLE
        binding.llClientPortfolioDetails.visibility = View.VISIBLE
        toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
    }

    override fun renderClientDetailWithPortfolioAndProfitLoss(uiModel: UiModel<ClientDetailWithPortfolioAndProfitLoss>) {
        toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            data.detail.renderClient()
            data.portfolio.renderPortfolio()
            data.profitLoss.renderProfit()

        }
    }

    private fun ClientPortfolio.renderPortfolio() {
        binding.includePortfolio.apply {
            networth.text = StringStuff.commaDecorator(netWorth)
            daychgamt.text = StringStuff.commaDecorator(dayChangeAmount)
            daychgperst.text = "${StringStuff.commaDecorator(dayChangePer)} %"

            overallchgamt.text = StringStuff.commaDecorator(overallChangeAmt)
            overallchgperst.text = "${StringStuff.commaDecorator(overallChangePer)} %"

            val bgColor = if (dayChangeAmount.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            daychgamt.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))

            val bgColorPer = if (dayChangePer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            daychgperst.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorPer))

            val bgColorOverall = if (overallChangeAmt.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            overallchgamt.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            bgColorOverall
                    )
            )

            val bgColorOverallPer = if (overallChangePer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            overallchgperst.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), bgColorOverallPer)
            )

            ///////////broking portfolio

            val brokingMapList = mutableListOf<HashMap<String, String>>()
            brokingPortfolios.forEach {
                val map = hashMapOf(
                        MOSLConstants.PRODUCT to it.productName,
                        MOSLConstants.INVEST_AMT to StringStuff.commaDecorator(it.investmentValue),
                        MOSLConstants.PURCHASE_VALUE to StringStuff.commaDecorator(it.marketValue),
                        MOSLConstants.OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
                        MOSLConstants.PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                                "${it.percentageGainLoss} %"
                        ),
                        MOSLConstants.PRODUCT_ID to it.productId
                )
                brokingMapList.add(map)
            }

            val brokingAdapter =
                    PortfolioSnapshotBrokAdapter(requireContext(), brokingMapList as ArrayList)
            listBrokingPortfolio.adapter = brokingAdapter
            brokingAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listBrokingPortfolio)

            //////////asset portfolio

            val assetMapList = mutableListOf<HashMap<String, String>>()
            assetPortfolios.forEach {
                val map = hashMapOf(
                        MOSLConstants.PRODUCT to it.productName,
                        MOSLConstants.INVEST_AMT to StringStuff.commaDecorator(it.marketValue),
                        MOSLConstants.PURCHASE_VALUE to StringStuff.commaDecorator(it.purchaseValue),
                        MOSLConstants.OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
                        MOSLConstants.PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                                "${it.percentageGainLoss} %"
                        ),
                        MOSLConstants.PRODUCT_ID to it.productId,
                        MOSLConstants.PRODUCT_TYPE to it.productType
                )
                assetMapList.add(map)
            }

            val assetAdapter =
                    PortfolioSnapshotAssetAdapter(requireContext(), assetMapList as ArrayList)
            listAssestPortfolio.adapter = assetAdapter
            assetAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listAssestPortfolio)

            ///////////Ledger

            val ledgerMapList = mutableListOf<HashMap<String, String>>()
            availableLedgers.forEach {
                val map = hashMapOf(
                        MOSLConstants.PRODUCT to it.productName,
                        MOSLConstants.TOTAL to StringStuff.commaDecorator(it.balanceLedger)
                )
                ledgerMapList.add(map)
            }

            val ledgerAdapter =
                    PortfolioSnapshotCashAdapter(requireContext(), ledgerMapList as ArrayList)
            listAssestPortfolio.adapter = ledgerAdapter
            ledgerAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listAssestPortfolio)
        }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun ClientProfitLoss.renderProfit() {

        binding.includePortfolio.apply {
            tvTotalGainLoss.text = StringStuff.commaDecorator(totalGainLoss)

            tvLTGainLoss.text = StringStuff.commaDecorator(ltGainLoss)
            tvLTGainLossPer.text = "${StringStuff.commaDecorator(ltGainLossPer)} %"

            val bgColor = if (ltGainLoss.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvLTGainLoss.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))

            val bgColorPer = if (ltGainLossPer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvLTGainLossPer.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorPer))

            tvSTGainLoss.text = StringStuff.commaDecorator(stGainLoss)
            tvSTGainLossPer.text = "${StringStuff.commaDecorator(stGainLossPer)} %"

            val bgColorSt = if (stGainLoss.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvSTGainLoss.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorSt))

            val bgColorPerSt = if (stGainLossPer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvSTGainLossPer.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            bgColorPerSt
                    )
            )

            ///////////broking product

            val brokingMapList = mutableListOf<HashMap<String, String>>()
            brokingProducts.forEach {
                val map = hashMapOf(
                        MOSLConstants.PRODUCT_ID to it.productId,
                        MOSLConstants.PRODUCT to it.productName,
                        MOSLConstants.STGL to StringStuff.commaDecorator(it.stGainLoss),
                        MOSLConstants.LTGL to StringStuff.commaDecorator(it.ltGainLoss),
                        MOSLConstants.TOTAL_GAIN_LOSS to StringStuff.commaDecorator(it.totalGainLoss)
                )
                brokingMapList.add(map)
            }

            val brokingAdapter =
                    PortfolioPLBrokingAdapter(requireContext(), brokingMapList as ArrayList)
            lvPLBrok.adapter = brokingAdapter
            brokingAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLBrok)

            //////////asset product

            val assetMapList = mutableListOf<HashMap<String, String>>()
            assetProducts.forEach {
                val map = hashMapOf(
                        MOSLConstants.PRODUCT_ID to it.productId,
                        MOSLConstants.PRODUCT to it.productName,
                        MOSLConstants.STGL to StringStuff.commaDecorator(it.stGainLoss),
                        MOSLConstants.LTGL to StringStuff.commaDecorator(it.ltGainLoss),
                        MOSLConstants.TOTAL_GAIN_LOSS to StringStuff.commaDecorator(it.totalGainLoss)
                )
                assetMapList.add(map)
            }

            val assetAdapter = PortfolioPLAssetAdapter(requireContext(), assetMapList as ArrayList)
            lvPLAsset.adapter = assetAdapter
            assetAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLAsset)

            ///////////Limit

            val limitMapList = mutableListOf<HashMap<String, String>>()
            availableLimits.forEach {
                val map = hashMapOf(
                        MOSLConstants.CASH to it.cashType,
                        MOSLConstants.AMOUNT to StringStuff.commaDecorator(it.amount)
                )
                limitMapList.add(map)
            }

            val limitAdapter = PortfolioPLLimitsAdapter(requireContext(), limitMapList as ArrayList)
            lvPLLimits.adapter = limitAdapter
            limitAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLLimits)
        }

    }

    private fun toggleSectionExpansion(
            parent: View,
            icon: TextView,
            expansion: String
    ) {
        if (expansion.equals("OuterSectionExpansion", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                parent.visibility = View.GONE
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            } else {
                parent.visibility = View.VISIBLE
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            }
        } else if (expansion.equals("InnerSectionExpansion", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                parent.visibility = View.GONE
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.down_button, 0)
            } else {
                parent.visibility = View.VISIBLE
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
            }
        }
    }

    override fun onAadharAfterTextChanged(): Observable<String> {
        return binding.etAadharNumber.afterTextChangeEvents()
                .map { it.editable?.toString() ?: emptyString() }
    }

    override fun renderOnAadharAfterTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    CRMValidationRule.validateAadhar(
                            text
                    ) -> binding.tlAadhar.disableErrorMessages()
                    else -> {
                        binding.tlAadhar.setErrorMessages(MOSLConstants.INVALID_AADHAR)
                    }
                }
            }
            else -> {
                binding.tlAadhar.disableErrorMessages()
            }
        }
    }

    override fun onPinTextChanged(): Observable<String> {
        return binding.etPincode.textChanges()
                .map { it.toString() }
    }

    override fun renderOnPinTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    text.length < 6 -> binding.tlPin.setErrorMessages(MOSLConstants.INVALID_PINCODE_NUMBER)
                    else -> binding.tlPin.disableErrorMessages()
                }
            }
            else -> {
                binding.tlPin.disableErrorMessages()
            }

        }
        if (binding.cbSameAs.isChecked) {
            binding.etOffPincode.setText(text)
        }
    }

    override fun onOfficePinTextChanged(): Observable<String> {
        return binding.etOffPincode.textChanges()
                .map { it.toString() }
    }

    override fun renderOnOfficePinTextChange(text: String) {
        when {
            text.isNotBlank() -> {
                when {
                    text.length < 6 -> binding.tlOffPin.setErrorMessages(MOSLConstants.INVALID_PINCODE_NUMBER)
                    else -> binding.tlOffPin.disableErrorMessages()
                }
            }
            else -> {
                binding.tlOffPin.disableErrorMessages()
            }

        }
    }

    override fun isCbSameAsChecked(): Boolean {
        return binding.cbSameAs.isChecked
    }

    override fun onAdd1AfterTextChanged(): Observable<String> {
        return binding.etAdd1.textChanges()
                .map { it.toString() }
    }

    override fun renderOnAdd1AfterTextChange(text: String) {
        binding.etOffAdd1.setText(text)
    }

    override fun onAdd2AfterTextChanged(): Observable<String> {
        return binding.etAdd2.textChanges()
                .map { it.toString() }
    }

    override fun renderOnAdd2AfterTextChange(text: String) {
        binding.etAdd2.setText(text)
    }

    override fun onAdd3AfterTextChanged(): Observable<String> {
        return binding.etAdd3.textChanges()
                .map { it.toString() }
    }

    override fun renderOnAdd3AfterTextChange(text: String) {
        binding.etAdd3.setText(text)
    }

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
            etRefereeNumber.setText(emptyString())
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
            etRefereeNumber.setText(emptyString())
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
            binding.etRefereeNumber.setText(emptyString())
        }
        uiModel.onSuccess {
            when (it.type) {
                "ByCode" -> binding.etRefereeName.setText(it.name)
                else -> binding.etRefereeCode.setText(it.code)
            }
            binding.etRefereeEmail.setText(it.email)
            binding.etRefereeNumber.setText(it.mobile)
            isReferee = true
        }
    }

    private infix fun ProgressBar.toggle(inProgress: Boolean) {
        if (inProgress) visible() else gone()
    }

    override fun isRefereeCodeFocused(): Boolean {
        return binding.etRefereeCode.isFocused
    }

    override fun isRefereeNameFocused(): Boolean {
        return binding.etRefereeName.isFocused
    }

    override fun render(uiModel: UiModel<LeadDetailPageData>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            interestedDatas.toMutableList()
                    .clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess { data ->
            profileAdapter.addAll(data.profileRowDatas)
            binding.spProfile.setSpinnerData(profileAdapter, data.profileRowDatas)
            interestedDatas = data.interestInRowDatas
            interestedAdapter.addAll(data.interestInRowDatas)
            // binding.sp.setSpinnerData(interestInAdapter, data.interestInRowDatas)
            binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
            binding.spOffCountry.setSpinnerData(countryAdapterOff, data.countryRowDatas)
            binding.llEditLeadDetail.performClick()
        }
    }

    override fun renderLeadDetailAndProfession(uiModel: UiModel<LeadDetailAndProfession>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
            data.detail.renderLeadDetail()
        }
    }

    private var leadDetailAddProductList = ArrayList<LeadDetailAddProductModel>()

    private fun setEnableDisable(status: Boolean) {
        binding.apply {
            etLeadName.isEnabled = status
            etPanNumber.isEnabled = status
            etAadharNumber.isEnabled = status
            etContactPerson.isEnabled = status
            etLandlineNumber.isEnabled = status
            etRefereeCode.isEnabled = status
            etRefereeName.isEnabled = status
            etProspectRefereeName.isEnabled = status
            etProspectRefereerMobile.isEnabled = status
            etProspectRefereerEmail.isEnabled = status
            etLandlineNumber.isEnabled = status
            etDob.isEnabled = status
            etAnniversary.isEnabled = status
            etAlternateMobileNumber.isEnabled = status
            etAlternateEmail.isEnabled = status
            etEmail.isEnabled = status
            etContactPerson.isEnabled = status
            etComments.isEnabled = status
            btnSubmit.isEnabled = status
            spProfile.isEnabled = status
            spProfession.isEnabled = status
            cbSameAs.isEnabled = status
            cbPrimaryAddress.isEnabled = status
            cbOffPrimaryAddress.isEnabled = status
            spPmsProduct.isEnabled = status
            if (status) {
                if (cbPrimaryAddress.isChecked) {
                    enableField(llResAdd)
                    enableField(llOffAdd)
                }
                if (cbOffPrimaryAddress.isChecked) {
                    enableField(llOffAdd)
                    enableField(llResAdd)
                }
            } else {
                disableField(llOffAdd)
                disableField(llResAdd)
            }
            if (mLeadFieldSectionModelList.isNotEmpty()) {
                for (mSection in mLeadFieldSectionModelList) {
                    if (mSection.selectionList != null) {
                        for (modal in mSection.selectionList) {
                            val cObj = modal.controlObject
                            if (cObj != null) {
                                if (cObj is EditText) {
                                    cObj.isEnabled = status
                                } else if (cObj is Spinner) {
                                    cObj.isEnabled = status
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val mLeadFieldSectionModelList = emptyList<LeadFieldSectionModel>()

    private var leadOwne = ""

    private fun LeadDetail.renderLeadDetail() {
        isLeadEditable = when {
            canEditLead == "0" || isProductSpecialist.equals("Y", true) -> false
            else -> true
        }

        binding.spPmsProduct.apply {
            adapter = pmsProductAdapter
        }

        leadOwne = leadOwner


        pmsProductAdapter.setSpinnerDataWithSelection(
                binding.spPmsProduct, pmsList, isPotentialPMSProduct
        )
        binding.etProspectRefereerEmail.setText(prospectReferEmail)
        binding.etProspectRefereeName.setText(prospectReferName)
        binding.etRefereeCode.setText(refereeCode)
        binding.etRefereeName.setText(refereeName)
        binding.etRefereeEmail.setText(referEmail)
        binding.etRefereeNumber.setText(referMobile)
        binding.etCreatedBy.setText(createdBy)
        binding.etCreatedThrough.setText(createdThrough)
        binding.etLastModifiedBy.setText(lastModifiedBy)
        try {
            binding.etLastModifiedDate.setText(
                    CRMAppUtil.getDateFromString(
                            lastModifiedDate,
                            "yyyy-MM-dd'T'kk:mm:ss", MOSLConstants.DATE_FORMAT_TO_DISPLAY
                    )
            )
        } catch (e: Exception) {
            binding.etLastModifiedDate.setText(lastModifiedDate.replace("T", " "))
        }

        binding.etProspectSource.setText(prospectSource)
        binding.etProspectSourceDetail.setText(leadSourceDetails)
        binding.etComments.setText(comments)
        binding.etContactPerson.visibility =
                if (leadCategoryText.equals("Individual", ignoreCase = true)) {
                    isIndividualsFlag = true
                    View.GONE
                } else {
                    isIndividualsFlag = false
                    View.VISIBLE
                }
        binding.etLeadID.setText(leadId) //Himanshu
        binding.etLeadName.setText(contact?.name) //Himanshu
        try {//Jeet
            binding.etCreationDate.setText(
                    CRMAppUtil.getDateFromString(
                            createdDate,
                            "yyyy-MM-dd'T'kk:mm:ss", MOSLConstants.DATE_FORMAT_TO_DISPLAY
                    )
            )
        } catch (e: Exception) {
            binding.etCreationDate.setText(createdDate.replace("T", " "))
        }//Jeet
        binding.etSource.setText(sourceName) //Himanshu

        binding.etMobileNumber.setText(contact?.mobile) //Himanshu
        binding.etAlternateMobileNumber.setText(contact?.alternateMobileNo) //Himanshu
        binding.etEmail.setText(contact?.emailId) //Himanshu
        binding.etAlternateEmail.setText(contact?.alternateEmailId) //Himanshu

        binding.etLeadCategory.setText(leadCategoryText)

        if (leadProductMapping != null && leadProductMapping.isNotEmpty()) {
            leadProductMapping.renderMapping()
        }

        /*contact?.run {
          renderContact(leadCategoryText)
        }*/

    }

    private fun Contact.renderContact(category: String) {

    }

    private var leadDetailAddProductAdapter: LeadDetailAddProductAdapter? = null

    private fun List<LeadProductMappingItem>.renderMapping() {
        mapNotNull {
            stage = it.stageID?.toString() ?: emptyString()
            status = it.statusID?.toString() ?: emptyString()
            binding.tvClientCode.setText(it.clientCode)
            leadDetailAddProductList.add(getLeadDetailAddProductModel(it))
        }
        leadDetailAddProductAdapter =
                LeadDetailAddProductAdapter(
                        saathiApi,
                        requireContext(),
                        leadDetailAddProductList,
                        binding.lvAddProduct,
                        leadId,
                        isLeadEditable,
                        statusDatas,
                        statusAdapter,
                        userManager.getUser()?.employeeCode,
                        status,
                        userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                        (binding.spPmsProduct.selectedItem as? RowData)?.value ?: emptyString()
                )
        binding.lvAddProduct.adapter = leadDetailAddProductAdapter
        CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail(binding.lvAddProduct)
        binding.nestedScrollView.apply {
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }

    private fun getLeadDetailAddProductModel(
            item: LeadProductMappingItem
    ): LeadDetailAddProductModel {
        val crossSellingProducts: List<CrossSellingProductsModel> =
                getCrossSellingProductsModel(item.crossSellingProducts)
        return LeadDetailAddProductModel(
                item.stageID.toString(),
                stringOrEmpty(item.stageName), item.statusID.toString(),
                stringOrEmpty(item.statusName), item.interestedIn.toString(),
                stringOrEmpty(item.productName),
                stringOrEmpty(item.primaryRMName),
                stringOrEmpty(item.productSpecialistName),
                stringOrEmpty(item.assignedBy),
                stringOrEmpty(item.assignedByDate),
                stringOrEmpty(item.clientCode), item.leadProductMappingID.toString(),
                item.isDefault,
                crossSellingProducts,
                item.isCrossSellingProduct,
                stringOrEmpty(item.isProductSpecialistEditDisabled),
                stringOrEmpty(item.isProductDisabled),
                stringOrEmpty(item.productSpecialist)
        )
    }

    private fun getLeadDetailAddProductModel(
            item: AddProductData
    ): LeadDetailAddProductModel {
        val crossSellingProducts: List<CrossSellingProductsModel> =
                getCrossSellingProductsModel(item.crossSellingProducts)
        return LeadDetailAddProductModel(
                item.stageId,
                stringOrEmpty(item.stageName), item.statusId,
                stringOrEmpty(item.statusName), item.interestedIn,
                stringOrEmpty(item.prodName),
                stringOrEmpty(item.primRMName),
                stringOrEmpty(item.productSpecialistName),
                stringOrEmpty(item.assignedBy),
                stringOrEmpty(item.assignedByDate),
                stringOrEmpty(item.clientCode), item.leadProductMappingId,
                item.isDefault,
                crossSellingProducts,
                item.isCrossSellingProduct,
                stringOrEmpty(item.isProdSpecEditDisabled),
                stringOrEmpty(item.isProdDisabled),
                stringOrEmpty(item.prodSpecialist)
        )
    }

    private fun getCrossSellingProductsModel(
            products: List<CrossSellingProduct>?
    ): List<CrossSellingProductsModel> {
        val crossSellingProducts: MutableList<CrossSellingProductsModel> =
                ArrayList()
        if (products != null && products.size > 0) {
            for ((leadProductId, productId, statusId, productName, statusName) in products) {
                crossSellingProducts.add(
                        CrossSellingProductsModel(
                                leadProductId,
                                productId,
                                statusId,
                                productName,
                                statusName
                        )
                )
            }
        }
        return crossSellingProducts
    }

/*
  private fun getLeadDetailAddProductModel(
    item: LeadProductMappingItem
  ): LeadDetailAddProductModel? {
    val crossSellingProducts: List<CrossSellingProductsModel> =
      getCrossSellingProductsModel(item.crossSellingProducts)
    return LeadDetailAddProductModel(
        item.stageID.toString(),
        stringOrEmpty(item.stageName), item.statusID.toString(),
        stringOrEmpty(item.statusName), item.interestedIn.toString(),
        stringOrEmpty(item.productName),
        stringOrEmpty(item.primaryRMName),
        stringOrEmpty(item.productSpecialistName),
        stringOrEmpty(item.assignedBy),
        stringOrEmpty(item.assignedByDate),
        stringOrEmpty(item.clientCode), item.leadProductMappingID.toString(),
        item.isDefault,
        crossSellingProducts,
        item.isCrossSellingProduct,
        stringOrEmpty(item.isProductSpecialistEditDisabled),
        stringOrEmpty(item.isProductDisabled),
        stringOrEmpty(item.productSpecialist)
    )
  }*/

    private var isLeadEditable = true
    var isIndividualsFlag = false

    override fun isSameAsAboveChecked(): Boolean = binding.cbSameAs.isChecked
    override fun onCountrySelected(): Observable<Int> = binding.spCountry.itemSelections()
    override fun onOfficeCountrySelected(): Observable<Int> = binding.spOffCountry.itemSelections()

    override fun onStateSelected(): Observable<Int> = binding.spState.itemSelections()
    override fun onOfficeStateSelected(): Observable<Int> = binding.spOffState.itemSelections()
    override fun onCitySelected(): Observable<Int> = binding.spCity.itemSelections()

    override fun renderOnCitySelectAndSameAsAboveChecked(position: Int) {
        binding.spOffCity.setSelection(position)
    }

    override fun getSelectedCountry(): RowData = binding.spCountry.selectedItem as RowData
    override fun getSelectedOfficeCountry(): RowData = binding.spOffCountry.selectedItem as RowData
    override fun getSelectedState(): RowData {
        Timber.i("SelectedState=>>>>%s", binding.spState.selectedItem)
        return binding.spState.selectedItem as RowData
    }

    override fun getSelectedOfficeState(): RowData = binding.spOffState.selectedItem as RowData

    override fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spState.setSpinnerData(stateAdapter, it)
            if (binding.cbSameAs.isChecked) {
                binding.spOffCountry.setSelection(binding.spCountry.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spOffState.setSpinnerData(stateAdapterOff, it)
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

    override fun interestedProductSelected(): Observable<SelectInterestedProductEvent> {
        return subject.ofType()
    }

    override fun selectedStage(): Observable<SelectStageEvent> {
        return subject.ofType()
    }

    override fun renderStage(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            stageDatas.toMutableList()
                    .clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            stageDatas.toMutableList()
                    .clear()
            stageDatas = it
            stageAdapter.setSpinnerData(requireNotNull(addProductDialogBinding).spStage, stageDatas)
        }
    }

    override fun renderStatus(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            statusDatas.toMutableList()
                    .clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            statusDatas.toMutableList()
                    .clear()
            statusDatas = it
            statusAdapter.setSpinnerData(
                    requireNotNull(addProductDialogBinding).spStatus,
                    statusDatas
            )
        }
    }

    override fun onDestroyView() {
        presenter.stop()
        addProductDialogBinding = null
        productSpecialDialog = null
        assignToDialog = null
        super.onDestroyView()
    }

    private var pager: ViewPager? = null
    fun setViewPager(viewPager: ViewPager) {
        pager = viewPager
    }

    companion object {
        @JvmStatic
        fun newInstance(
                position: Int,
                type: String,
                leadID: String,
                ucId: String
        ): LeadDetailFragment {
            return LeadDetailFragment().apply {
                val args = Bundle().apply {
                    putInt(MOSLConstants.POSITION, position)
                    putString(MOSLConstants.TYPE, type)
                    putString(MOSLConstants.LEADID, leadID);
                    putString(MOSLConstants.UCID, ucId);
                }
                arguments = args
            }
        }

        const val OUTER = "OuterSectionExpansion"
        const val INNER = "InnerSectionExpansion"

    }
}