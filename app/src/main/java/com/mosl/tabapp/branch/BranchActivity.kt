package com.mosl.tabapp.branch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.prefs.StringPreference
import com.mosl.app.coreui.base.LeanBaseActivity
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.mobile.user.annotation.BranchCode
import com.mosl.shared.BranchSelectNeeded
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.BranchActivityBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BranchActivity : LeanBaseActivity(), BranchContract.View {

    lateinit var binding: BranchActivityBinding
    private val dataActionSubject = PublishSubject.create<DataAction>()
    lateinit var section: Section

    @Inject
    @field:BranchCode
    lateinit var branchCodePreference: StringPreference
    override fun getLayout(): Int {
        return R.layout.branch_activity
    }

    @Inject
    lateinit var presenter: BranchPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BranchActivityBinding.bind(findViewById(R.id.root_coordinator))
        findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                finish()
            }
            title = getString(R.string.PAGE_TITLE_BRANCH_LIST)
        }
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>()
        binding.rvBranch.layoutManager = LinearLayoutManager(this)
        binding.rvBranch.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.rvBranch.adapter = groupAdapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.grey_item_decorator)!!
        )
        binding.rvBranch.addItemDecoration(dividerItemDecoration)
        groupAdapter.setOnItemClickListener { item, _ ->
            if (item is ItemBranch) {
                branchCodePreference.set(item.branchItemResponse.branchCode)
                updateBranch(item.branchItemResponse.branchCode)
            }
        }

        section = Section()
        groupAdapter.add(section)
    }

    override fun renderBranch(uiModel: UiModel<List<BranchItemResponse>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            it.showOnFailurePopup()
        }
        uiModel.onSuccess {
            if (it.isNotEmpty()) {
                section.addAll(it.map { item -> ItemBranch(item) })
            }
        }
    }

    fun Throwable.showOnFailurePopup() {
        val errorOnFailure = this.getErrorOnFailure()
        alert(message = errorOnFailure.getResolvedMessage(applicationContext))
    }

    fun toggleProgressBar(inProgress: Boolean) {
        if (inProgress) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }


    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }


    private fun updateBranch(profileUrl: String?) {
        val intent = Intent()
        intent.putExtra(BRANCHCODE, profileUrl)
        this.setResult(BranchSelectNeeded, intent)
    }


    override fun isDataEmpty(): Boolean {
        return true
    }

    override fun onDataAction(): Observable<DataAction> {
        return dataActionSubject
    }

    companion object {
        const val BRANCHCODE = "BranchCode"

    }

}