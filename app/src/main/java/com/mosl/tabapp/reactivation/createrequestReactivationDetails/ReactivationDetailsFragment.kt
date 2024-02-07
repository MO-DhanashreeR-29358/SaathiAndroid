package com.mosl.tabapp.reactivation.createrequestReactivationDetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.LayoutReactivationViewDetailsBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ReactivationDetailsFragment : BottomSheetDialogFragment() ,ReactivationDetailsView{
    protected val dataActionSubject: PublishSubject<DataAction> = PublishSubject.create()
    lateinit var binding: LayoutReactivationViewDetailsBinding
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


    }
}