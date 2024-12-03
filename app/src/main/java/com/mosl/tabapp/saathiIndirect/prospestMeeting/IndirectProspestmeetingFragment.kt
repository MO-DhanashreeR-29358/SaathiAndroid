package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectProspestMeetingBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject


class IndirectProspestmeetingFragment: CommonFragment(),IndirectProspestMeetingView {

    internal  lateinit var binding:FragIndirectProspestMeetingBinding

    @Inject
    lateinit var presenter: IndirectProspestMeetingPresenter

    private val uiSubject = PublishSubject.create<IndirectProspestMeetingUiEvent>()

    private val TRIGGER_AUTO_COMPLETE = 100
    private val AUTO_COMPLETE_DELAY: Long = 300
    private var handler: Handler? = null
    private var autoSuggestAdapter: AutoSuggestAdapter? = null

    private var handlerFCode: Handler? = null
    private var autoSuggestAdapterFCode: AutoSuggestAdapter? = null

    override fun getLayout() = R.layout.frag_indirect_prospest_meeting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectProspestMeetingBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = AutoSuggestAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
        binding.edtClientCode.setThreshold(2)
        binding.edtClientCode.setAdapter(autoSuggestAdapter)
        binding.edtClientCode.setOnItemClickListener(
            AdapterView.OnItemClickListener { parent, view, position, id ->
                binding.edtClientCode.setText(
                    autoSuggestAdapter!!.getObject(position).leadId.toString()
                )
            })

        binding.edtClientCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                handler!!.removeMessages(TRIGGER_AUTO_COMPLETE)
                handler!!.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        handler = Handler { msg ->
            if (msg.what === TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(binding.edtClientCode.getText())) {
                    uiSubject.onNext(IndirectProspestMeetingUiEvent.SearchTextChange(binding.edtClientCode.getText().toString()))
                }
            }
            false
        }


        autoSuggestAdapterFCode = AutoSuggestAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
        binding.edtFCode.setThreshold(2)
        binding.edtFCode.setAdapter(autoSuggestAdapterFCode)
        binding.edtFCode.setOnItemClickListener(
            AdapterView.OnItemClickListener { parent, view, position, id ->
                binding.edtFCode.setText(
                    autoSuggestAdapterFCode!!.getObject(position).leadId.toString()
                )
            })

        binding.edtFCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                handlerFCode!!.removeMessages(TRIGGER_AUTO_COMPLETE)
                handlerFCode!!.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        handlerFCode = Handler { msg ->
            if (msg.what === TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(binding.edtFCode.getText())) {
                    uiSubject.onNext(IndirectProspestMeetingUiEvent.SearchFCodeTextChange(binding.edtFCode.getText().toString()))
                }
            }
            false
        }
    }

    override fun onTextSearchCalled(): Observable<IndirectProspestMeetingUiEvent.SearchTextChange> {
        return uiSubject.ofType()
    }

    override fun onTextSearchedRender(uiModel: UiModel<List<ClientSearchModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            autoSuggestAdapter?.setData(it)
            autoSuggestAdapter?.notifyDataSetChanged()
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onFCodeTextSearchCalled(): Observable<IndirectProspestMeetingUiEvent.SearchFCodeTextChange> {
        return uiSubject.ofType()
    }

    override fun onFCodeTextSearchRender(uiModel: UiModel<List<ClientSearchModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            autoSuggestAdapterFCode?.setData(it)
            autoSuggestAdapterFCode?.notifyDataSetChanged()
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)
        }
    }

    override fun onSearchButtonClicked(): Observable<Unit> {
        return binding.btnSearchLead.clicks()
    }

    override fun onResetButtonClicked(): Observable<Unit> {
        return binding.btnResetLead.clicks()
    }

    override fun onSearchButtonRender() {

    }

    override fun onResetButtonRender() {

        binding.apply {
            edtClientCode.setText("")
            edtFCode.setText("")
            edtleadId.setText("")
        }

    }
}