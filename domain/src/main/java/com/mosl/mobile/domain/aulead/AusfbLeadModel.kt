package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.mosl.mobile.domain.aulead.AusfbLeadGroupItemModel


data class AusfbLeadModel(

    @JvmField val table: List<AusfbLeadItemModel>?,

    @JvmField val table1: List<AusfbLeadItemModel>?,

    @JvmField val table2: List<AusfbLeadGroupItemModel>?
) {

    fun getXAxisList(): MutableList<String> {
        var xAxisList: MutableList<String> = mutableListOf()

        if (table?.size != 0) {
            xAxisList = getList(table!!.get(1))
        }

        return xAxisList
    }

    fun getYAxisList(): MutableList<MutableList<String>> {
        val yAxisList: MutableList<MutableList<String>> = mutableListOf()

        table1?.let {
            if (it.size != 0) {
                yAxisList.add(0, getList(it.get(0)))
                yAxisList.add(1, getList(it.get(1)))
            }
        }

        return yAxisList
    }

    fun getLabelString(): Array<String> {

        table1?.let {
            val labelString: Array<String> = arrayOf(it.get(0).stage, it.get(1).stage)

            return labelString
        }

        return arrayOf()

    }

    fun getTotal(): String {

        table1?.let {
            val total = it.get(2).total ?: "0"

            return total
        }

        return "0"

    }


    fun getList(itemModel: AusfbLeadItemModel): MutableList<String> {
        val mutableList: MutableList<String> = mutableListOf()

        itemModel.C0?.let { mutableList.add(it) }
        itemModel.C1?.let { mutableList.add(it) }
        itemModel.C2?.let { mutableList.add(it) }
        itemModel.C3?.let { mutableList.add(it) }
        itemModel.C4?.let { mutableList.add(it) }
        itemModel.C5?.let { mutableList.add(it) }
        itemModel.C6?.let { mutableList.add(it) }
        itemModel.C7?.let { mutableList.add(it) }

        return mutableList
    }
}