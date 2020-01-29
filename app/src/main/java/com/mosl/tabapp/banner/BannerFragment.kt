package com.mosl.tabapp.banner

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentBannerBinding
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BannerFragment : DialogFragment(), BannerView {

    internal lateinit var binding: FragmentBannerBinding

    @Inject
    internal lateinit var presenter: BannerPresenter

    val dataActionSubject = PublishSubject.create<DataAction>()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_banner, container, false)
    }

    override fun onDataAction(): Observable<DataAction> = dataActionSubject

    override fun isDataEmpty(): Boolean {
        return true
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBannerBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun render(data: BannerScreenData) {
        loadBannerImage(data.imageUrl)
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    private fun loadBannerImage(imageUrl: String) {
        Glide.with(requireContext()).load(imageUrl).into(binding.ivRemoteImage);
    }

    override fun onCloseButtonClicked(): Observable<Unit> {
        return binding.ivClose.clicks()
    }

    override fun onBannerImageClicked(): Observable<Unit> {
        return binding.ivRemoteImage.clicks()
    }

    override fun dismissDialog() {
        dismiss()
    }


    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.CENTER)
        }
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }
}