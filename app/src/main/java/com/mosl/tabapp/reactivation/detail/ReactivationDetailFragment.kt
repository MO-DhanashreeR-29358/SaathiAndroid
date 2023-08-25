package com.mosl.tabapp.reactivation.detail

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentReactivationDetailBinding
import com.mosl.tabapp.databinding.FragmentReactivationSummaryBinding
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class ReactivationDetailFragment : CommonFragment(), ReactivationDetailView {

    lateinit var binding : FragmentReactivationDetailBinding

    @Inject
    lateinit var presenter: ReactivationDetailPresenter

    var period : String = ""
    var currentStage : String =""

    override fun getLayout(): Int {
        return R.layout.fragment_reactivation_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReactivationDetailBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)

    }

    override fun extractArguments(bundle: Bundle) {
        super.extractArguments(bundle)
        period = bundle.getString(MOSLConstants.REACTIVATION_PERIOD, "") ?: ""
        currentStage = bundle.getString(MOSLConstants.REACTIVATION_TYPE, "") ?: ""
    }

    private fun setupView(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun getPeriod(): String {
        return period
    }

    override fun getCurrentStage(): String {
        return currentStage
    }


}