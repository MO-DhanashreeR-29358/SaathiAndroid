package com.mosl.tabapp.reactivation.createrequestReactivationDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.mosl.app.core.events.DataAction
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.LayoutReactivationViewDetailsBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ReactivationDetailsFragment : BottomSheetDialogFragment() ,ReactivationDetailsView{
    protected val dataActionSubject: PublishSubject<DataAction> = PublishSubject.create()
    lateinit var binding: LayoutReactivationViewDetailsBinding
    var objReactivationClientSearchResponseModel: ReactivationClientSearchResponseModel?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.layout_reactivation_view_details, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutReactivationViewDetailsBinding.bind(view)
        // presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        fnSetView()
    }

    fun fnSetData(strData:String)
    {
        objReactivationClientSearchResponseModel= Gson().fromJson(strData,ReactivationClientSearchResponseModel::class.java)
        Log.d("passed data",Gson().toJson(objReactivationClientSearchResponseModel))
    }
    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun getTheme(): Int {
        return R.style.EkycAppBottomSheetDialogTheme
    }
    override fun isDataEmpty(): Boolean {
        return false
    }

    override fun onDataAction(): Observable<DataAction> {
        return dataActionSubject
    }

    fun fnSetView() {

        binding.apply {
            edtClientName.setText(objReactivationClientSearchResponseModel?.clientName?:"")
            edtFinalSegment.setText(objReactivationClientSearchResponseModel?.finalSegment?:"")
            edtsubSegment.setText(objReactivationClientSearchResponseModel?.subSegment?:"")
            edtBranch.setText(objReactivationClientSearchResponseModel?.branch?:"")
            edtFinalActDate.setText(objReactivationClientSearchResponseModel?.finalActivationDate?:"")
            edtLastTradeDate.setText(objReactivationClientSearchResponseModel?.lastTradedDate?:"")
            edtCurrentRM.setText(objReactivationClientSearchResponseModel?.currentRMCode?:"")
            edtCurrentRMName.setText(objReactivationClientSearchResponseModel?.currentRMName?:"")
            edtMappedAdvisor.setText(objReactivationClientSearchResponseModel?.mappedAdvisorCode?:"")
            edtMappedAdvisorName.setText(objReactivationClientSearchResponseModel?.mappedAdvisorName?:"")
            edtMargin.setText(objReactivationClientSearchResponseModel?.marginLast15Days?:"")
            edtMarginCollection.setText(objReactivationClientSearchResponseModel?.totalMarginCollected?:"")
            edtTgs.setText(objReactivationClientSearchResponseModel?.tGSEdumoRevenue?:"")

        }
        binding.btnClosePopup.setOnClickListener {
            dismiss()
        }
        binding.appCompatImageView3.setOnClickListener {
            dismiss()
        }


    }
}