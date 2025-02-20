package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import java.util.ArrayList;


class AutoSuggestAdapter(context: Context, resource: Int) :
    ArrayAdapter<String>(context, resource), Filterable {
    private val mlistData: MutableList<ClientSearchModel>

    init {
        mlistData = ArrayList()
    }

    fun setData(list: List<ClientSearchModel>?) {
        mlistData.clear()
        mlistData.addAll(list!!)
    }

    override fun getCount(): Int {
        return mlistData.size
    }

//    override fun getItem(position: Int): String {
//        return mlistData[position]
//    }

    override fun getItem(position: Int): String? {
        var code  = ""
        if(mlistData[position].clientCODE.isNotEmpty()){
            code = mlistData[position].clientCODE +"/"
        }else if(mlistData[position].franchiseeCode.isNotEmpty()){
            code = mlistData[position].franchiseeCode +"/"
        }
        return  code + mlistData[position].leadId
    }
    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    fun getObject(position: Int): ClientSearchModel {
        return mlistData[position]
    }

    @NonNull
    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mlistData
                    filterResults.count = mlistData.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}