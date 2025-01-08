package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mosl.mobile.domain.saathiIndirect.dashboard.SalesRM
import com.mosl.tabapp.R
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import io.reactivex.rxjava3.subjects.PublishSubject

class HierarchyListAdapter(private val salesRMList: List<SalesRM>,
                           private val sysName: String,
    private val publishSubject: PublishSubject<FocoHierarchyReportUIEvent>
) : RecyclerView.Adapter<HierarchyListAdapter.SalesRMViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesRMViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.indirect_hierarchy_list_item, parent, false)
        return SalesRMViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesRMViewHolder, position: Int) {
        val salesRM = salesRMList[position]
        holder.bind(salesRM)
    }

    override fun getItemCount(): Int = salesRMList.size

    // Toggle the expanded state of a given SalesRM
    fun toggleExpandedState(position: Int) {
        val salesRM = salesRMList[position]
        salesRM.isExpanded = !salesRM.isExpanded

        // Notify that the item has changed so that the UI can update
        notifyItemChanged(position)
    }
    inner class SalesRMViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val salesRMName: TextView = itemView.findViewById(R.id.salesRMName)
        private val revenue: TextView = itemView.findViewById(R.id.revenue)
        private val subordinatesRecyclerView: RecyclerView = itemView.findViewById(R.id.subordinatesRecyclerView)
        private val toggleButton: ImageView = itemView.findViewById(R.id.toggleButton)
        private val mainContainer: LinearLayout = itemView.findViewById(R.id.mainContainer)

        @SuppressLint("SetTextI18n")
        fun bind(salesRM: SalesRM) {
            if(sysName == FocoTargetPresenter.NO_OF_CLIENT_MEETINGS){
                salesRMName.text = salesRM.TMName
                revenue.text = ("Meetings : " + salesRM.Meeting)
            }else{
                salesRMName.text = salesRM.SalesRM
                revenue.text = ("Revenue : " + salesRM.Revenue)
            }


            toggleButton.setImageResource(
                if (salesRM.isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand
            )

            // Handle toggle button visibility based on expanded state
            toggleButton.visibility = if (salesRM.subordinates.isNotEmpty()) View.VISIBLE else View.GONE
            // Set up the RecyclerView for subordinates
            if (salesRM.subordinates.isNotEmpty()) {
                subordinatesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                subordinatesRecyclerView.adapter = HierarchyListAdapter(salesRM.subordinates,sysName, publishSubject)

                // Set the visibility of the subordinates RecyclerView based on the expanded state
                subordinatesRecyclerView.visibility = if (salesRM.isExpanded) View.VISIBLE else View.GONE
            } else {
                subordinatesRecyclerView.visibility = View.GONE
            }

            // Handle toggle button click for expand/collapse
            toggleButton.setOnClickListener {
                toggleExpandedState(adapterPosition)
            }

            mainContainer.setOnClickListener {
                var employeeCode : String
                if(sysName == FocoTargetPresenter.NO_OF_CLIENT_MEETINGS){
                    employeeCode = salesRM.TMName?.substringAfter("-").toString()
                }else{
                    employeeCode = salesRM.SalesRM.substringAfter("-")
                }
                
                publishSubject.onNext(FocoHierarchyReportUIEvent.OnReportClick(sysName, employeeCode))
            }
        }
    }
}
