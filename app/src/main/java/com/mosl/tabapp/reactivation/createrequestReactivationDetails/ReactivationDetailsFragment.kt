package com.mosl.tabapp.reactivation.createrequestReactivationDetails

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection

class ReactivationDetailsFragment : BottomSheetDialogFragment() ,ReactivationDetailsView{
    protected val dataActionSubject: PublishSubject<DataAction> = PublishSubject.create()
    lateinit var binding: LayoutEditEquityDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.layout_edit_equity_dialog, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutEditEquityDialogBinding.bind(view)
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