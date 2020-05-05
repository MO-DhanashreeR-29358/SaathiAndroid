package com.mosl.tabapp.leadcreation

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.TextView
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CustomListPickerDialog
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadCreationBinding
import com.mosl.tabapp.model.EmployeeModel
import javax.inject.Inject

class LeadCreationFragment : CommonFragment(), LeadCreationView {

  @Inject internal lateinit var presenter: LeadCreationPresenter
  private lateinit var binding: FragLeadCreationBinding

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

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}