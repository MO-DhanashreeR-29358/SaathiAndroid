package com.mosl.tabapp.drawermenu.digitalSalesKit

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.commons.rx.ofType
import com.mosl.tabapp.R
import com.mosl.tabapp.branch.ItemBranch
import com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentDigitalSalesKitBinding
import com.mosl.tabapp.report.ReportFragment
import com.mosl.tabapp.util.NAME
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import io.reactivex.rxjava3.core.Observable
import com.mosl.commons.rx.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class DigitalSalesKitFragment : CommonFragment(), DigitalSalesKitView {
    val TAG: String = DigitalSalesKitFragment::class.java.simpleName
    private lateinit var binding : FragmentDigitalSalesKitBinding
    lateinit var categoryName: String
    lateinit var categoryId: String
    lateinit var section: Section
    private val onRetryEvent = PublishSubject.create<Unit>()
    @Inject
    lateinit var presenter: DigitalSalesKitPresenter
    private val itemUiEvent = PublishSubject.create<String>()

    override fun getLayout(): Int {
        return R.layout.fragment_digital_sales_kit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDigitalSalesKitBinding.bind(view)
        categoryName = arguments?.getString(NAME).toString()
        categoryId = arguments?.getString(SYSTEM_NAME).toString()
        hideAppTitle()
        binding.tvMenuName.text = categoryName
        setUpRecyclerView()
        setupData()
        presenter.start()
    }

    private fun setUpRecyclerView() {
        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.rvDigitalSalesKitMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDigitalSalesKitMenu.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        binding.rvDigitalSalesKitMenu.adapter = groupAdapter
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.grey_item_decorator)!!
        )
        binding.rvDigitalSalesKitMenu.addItemDecoration(dividerItemDecoration)

        section = Section()
        groupAdapter.add(section)

    }
    private fun setupData(){
        section.clear()
        val mainMenu = userManager.getDigitalSalesKitMenuList().filter { item-> item.id.equals(categoryId) }
        val childMenu = mainMenu.get(0).menuList
        if (childMenu != null) {
            section.addAll(childMenu.map { item -> ItemDigitalSalesKit(item, itemUiEvent) })
        }
    }

    override fun onFragmentReopened(args: Bundle) {
        super.onFragmentReopened(args)
        categoryName = args.getString(NAME).toString()
        categoryId = args.getString(SYSTEM_NAME).toString()
        onRetryEvent.onNext(Unit)
    }

    override fun onViewChange(): Observable<Unit> {
        return onRetryEvent.ofType()
    }

    override fun renderViewChange() {
        binding.tvMenuName.text = categoryName
        setupData()
    }

    override fun onItemClick(): Observable<String> {
        return itemUiEvent.ofType()
    }

    override fun renderItemClick(pdfPath: String) {
        section.notifyChanged()
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(requireContext(), Uri.parse(pdfPath))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

}